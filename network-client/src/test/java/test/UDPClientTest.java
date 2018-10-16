package test;

import c.s.l.network.udp.Message;
import c.s.l.network.udp.cmd.NetworkCMD;
import c.s.l.network.udp.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class UDPClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        new UDPClientTest().test();
    }


    public void test() throws InterruptedException, IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.启动器
        Bootstrap bootstrap = new Bootstrap();
        //3.配置启动器
        bootstrap.group(group)                         //3.1指定group
                .channel(NioDatagramChannel.class)     //3.2指定channel
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel nioDatagramChannel) {


                        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new MyUdpEncoder());   //3.4在pipeline中加入编码器
                        pipeline.addLast(new MyUdpDecoder());
                        pipeline.addLast(new ClientHandler());
                    }

                });

        String roomId = "123123";
        int userId = new Random().nextInt(100);
        Channel channel = bootstrap.bind(9000 + userId).sync().channel();
        channel.writeAndFlush(Message.builder().msg(roomId).userId(userId).cmd(NetworkCMD.register).build()).sync();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String msg;
            if ((msg = bufferedReader.readLine()) != null) {
                if (msg.equals("quit")) {
                    channel.writeAndFlush(Message.builder().msg(roomId).userId(userId).cmd(NetworkCMD.close).build()).channel().closeFuture().await(3000);
                    group.shutdownGracefully();
                    break;
                }
                channel.writeAndFlush(Message.builder().msg(roomId).data(msg).userId(userId).cmd(NetworkCMD.msg).build()).sync();
            }
        }


        channel.closeFuture().sync();
        //7.关闭group

    }

    private static class MyUdpEncoder extends MessageToMessageEncoder<Message> {
        //这里是广播的地址和端口
        private InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 9001);

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Message msg, List<Object> list) {
            String json = JsonUtil.serialize(msg);
            byte[] bytes = json.getBytes();
            ByteBuf buf = channelHandlerContext.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            DatagramPacket packet = new DatagramPacket(buf, remoteAddress);
            list.add(packet);
        }
    }

    private static class MyUdpDecoder extends MessageToMessageDecoder<DatagramPacket> {

        @Override
        protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
            out.add(JsonUtil.deserialize(msg.content().toString(CharsetUtil.UTF_8), Message.class));
        }
    }


}

