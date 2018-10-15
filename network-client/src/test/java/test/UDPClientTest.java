package test;

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
        Channel channel = bootstrap.bind(9002).sync().channel();
        channel.writeAndFlush("hello").sync();

        channel.closeFuture().sync();
        //7.关闭group

    }

    private static class MyUdpEncoder extends MessageToMessageEncoder<String> {
        //这里是广播的地址和端口
        private InetSocketAddress remoteAddress = new InetSocketAddress("localhost", 9001);

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) {
            byte[] bytes = s.getBytes(CharsetUtil.UTF_8);
            ByteBuf buf = channelHandlerContext.alloc().buffer(bytes.length);
            buf.writeBytes(bytes);
            DatagramPacket packet = new DatagramPacket(buf, remoteAddress);
            list.add(packet);
        }
    }

    private static class MyUdpDecoder extends MessageToMessageDecoder<DatagramPacket> {

        @Override
        protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
            out.add(msg.content().toString(CharsetUtil.UTF_8));
        }
    }
}

