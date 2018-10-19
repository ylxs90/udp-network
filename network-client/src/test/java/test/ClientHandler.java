package test;

import c.s.l.network.udp.Message;
import c.s.l.network.udp.cmd.NetworkCMD;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        if (msg.getCmd() != NetworkCMD.HEART_BLOOD) {
            System.out.println(String.format("[%d]: %s", msg.getUserId(), msg.getData()));
        } else {
            System.out.println(msg.getMsg());
        }
    }

}
