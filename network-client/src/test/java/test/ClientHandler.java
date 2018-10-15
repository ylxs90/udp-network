package test;

import c.s.l.network.udp.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        System.out.println(String.format("[%d]: %s", msg.getUserId(), msg.getData()));
    }
}
