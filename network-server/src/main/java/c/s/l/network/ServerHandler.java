package c.s.l.network;

import c.s.l.network.udp.Message;
import c.s.l.network.udp.cmd.NetworkCMD;
import c.s.l.network.udp.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Set;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private GameRoom gameRoom;

    public ServerHandler(GameRoom room) {
        this.gameRoom = room;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        Message message = JsonUtil.deserialize(msg.content().toString(CharsetUtil.UTF_8), Message.class);
        System.out.println(String.format("from %s:%s", msg.sender().getHostName(), msg.sender().getPort()));


        if (message.getCmd() == NetworkCMD.REGISTER) {
            gameRoom.addUser(message.getMsg(), message.getUserId(), msg.sender());
            DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(JsonUtil.serialize(Message.builder().userId(0).data("connected").build()), CharsetUtil.UTF_8), msg.sender());
            ctx.writeAndFlush(packet);
            return;
        }
        if (message.getCmd() == NetworkCMD.MSG) {
            Set<SocketAddress> users = gameRoom.getUsers(message.getMsg());


            users.stream().filter(it -> !it.equals(msg.sender())).forEach(it -> {
                System.out.println(String.format("send to %s", it.toString()));
                DatagramPacket packet = new DatagramPacket(msg.content().retain(),
                        (InetSocketAddress) it);
                ctx.writeAndFlush(packet);
            });
        }
        if (message.getCmd() == NetworkCMD.CLOSE) {
            DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(JsonUtil.serialize(
                    Message.builder().userId(0).data("you left...").build()
            ), CharsetUtil.UTF_8), msg.sender());
            ctx.writeAndFlush(packet);
            Set<SocketAddress> users = gameRoom.getUsers(message.getMsg());

            ByteBuf data = Unpooled.copiedBuffer(JsonUtil.serialize(
                    Message.builder().userId(message.getUserId()).data("I'm leave").build()
            ), CharsetUtil.UTF_8);
            System.out.println(users);
            users.stream().filter(it -> !it.equals(msg.sender())).forEach(it -> {
                System.out.println(String.format("send to %s", it.toString()));
                DatagramPacket p = new DatagramPacket(data.retain(), (InetSocketAddress) it);
                ctx.writeAndFlush(p);
            });
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
