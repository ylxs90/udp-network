package test;

import c.s.l.network.udp.Message;
import c.s.l.network.udp.cmd.NetworkCMD;
import c.s.l.network.udp.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

public class UDPClientTest {

    @Test
    public void test() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.启动器
        Bootstrap bootstrap = new Bootstrap();
        //3.配置启动器
        bootstrap.group(group)                         //3.1指定group
                .channel(NioDatagramChannel.class)     //3.2指定channel
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel nioDatagramChannel) {
                        nioDatagramChannel.pipeline().addLast(new MyUdpEncoder());   //3.4在pipeline中加入编码器
                        nioDatagramChannel.pipeline().addLast(new MyUdpDecoder());
                        nioDatagramChannel.pipeline().addLast(new ClientHandler());
                    }

                });

        String rootId = "123123";
        Channel channel = bootstrap.bind(9010).sync().channel();
        channel.writeAndFlush(Message.builder().cmd(1).msg(rootId).userId(1).cmd(NetworkCMD.register).build()).sync();

        channel.writeAndFlush(Message.builder().cmd(1).msg(rootId).data("hello").userId(1).cmd(NetworkCMD.msg).build()).sync();
        channel.writeAndFlush(Message.builder().cmd(1).msg(rootId).data("nice to me you").userId(1).cmd(NetworkCMD.msg).build()).sync();
        channel.writeAndFlush(Message.builder().cmd(1).msg(rootId).data("mee too").userId(1).cmd(NetworkCMD.msg).build()).sync();


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

