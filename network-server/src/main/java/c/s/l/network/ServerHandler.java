package c.s.l.network;

import c.s.l.network.udp.Message;
import c.s.l.network.udp.cmd.NetworkCMD;
import c.s.l.network.udp.util.JsonUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Set;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private GameRoom gameRoom = null;

    public ServerHandler(GameRoom room) {
        this.gameRoom = room;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        gameRoom.addUser("123", msg.sender());
        Message message = JsonUtil.deserialize(msg.content().toString(CharsetUtil.UTF_8), Message.class);
        System.out.println(message);

        if (message.getCmd() == NetworkCMD.register) {
            gameRoom.addUser(message.getMsg(), msg.sender());
            DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(JsonUtil.serialize(Message.builder().userId(0).data("connected").build()), CharsetUtil.UTF_8), msg.sender());
            ctx.writeAndFlush(packet);
            return;
        }
        if (message.getCmd() == NetworkCMD.msg) {
            Set<SocketAddress> users = gameRoom.getUsers(message.getMsg());
            System.out.println(String.format("from %s:%s", msg.sender().getHostName(), msg.sender().getPort()));

            users.stream().filter(it -> it != msg.sender()).forEach(it -> {
                DatagramPacket packet = new DatagramPacket(msg.content(),
                        (InetSocketAddress) it);
                ctx.writeAndFlush(packet);
            });
        }
        if (message.getCmd() == NetworkCMD.close) {
            DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(JsonUtil.serialize(
                    Message.builder().userId(0).msg("close").build()),
                    CharsetUtil.UTF_8), msg.sender());
            ctx.writeAndFlush(packet);
            Set<SocketAddress> users = gameRoom.getUsers(message.getMsg());
            users.stream().filter(it -> it != msg.sender()).forEach(it -> {
                DatagramPacket p = new DatagramPacket(Unpooled.copiedBuffer(JsonUtil.serialize(
                        Message.builder().userId(message.getUserId()).data("I'm leave").build()
                ), CharsetUtil.UTF_8), (InetSocketAddress) it);
                ctx.writeAndFlush(p);
            });
            ctx.close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
