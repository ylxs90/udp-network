package c.s.l.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Main {

    public static void main(String[] args) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup(10);

        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ServerHandler());

        bootstrap.bind(9001).sync().channel().closeFuture();

        System.out.println("start....");
    }


}
