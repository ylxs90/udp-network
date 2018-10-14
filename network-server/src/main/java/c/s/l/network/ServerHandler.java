package c.s.l.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        System.out.println(msg.content().toString(CharsetUtil.UTF_8));
        System.out.println(String.format("from %s:%s", msg.sender().getHostName(), msg.sender().getPort()));
        ctx.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer("Hello，我是Server，我的时间戳是" + System.currentTimeMillis(),
                        CharsetUtil.UTF_8), msg.sender()));

    }

}
