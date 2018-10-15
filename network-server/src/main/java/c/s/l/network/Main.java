package c.s.l.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Main {

    public static void main(String[] args) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup(10);
        GameRoom gameRoom = new GameRoom();

        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ServerHandler(gameRoom));

            }
        });

        bootstrap.bind(9001).sync().channel().closeFuture();

        System.out.println("start....");
    }


}
