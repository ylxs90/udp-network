package c.s.l.network;

import c.s.l.network.proto.MessageStub;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class ClientMain {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();

        Channel channel = bootstrap.group(group).channel(NioDatagramChannel.class).handler(new LoggingHandler()).bind(9002).sync().channel();



        MessageStub.Message message = MessageStub.Message.newBuilder().setCmd(MessageStub.Message.CMD.MESSAGE).setUserId(1).setMsg("hello").setData("data").build();
        DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(message.toByteArray()),
                new InetSocketAddress("localhost", 9001));
        channel.writeAndFlush(packet).await(3000);


        group.shutdownGracefully();


    }
}
