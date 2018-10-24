package c.s.l.network;

import c.s.l.network.proto.MessageStub;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.DatagramPacketEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.net.SocketAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup(10);
        GameRoom gameRoom = new GameRoom();

        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new MessageToMessageDecoder<DatagramPacket>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
                        System.out.println("datagram");
                        out.add(msg.retain());
                        out.add(msg.sender());
                    }
                });
                pipeline.addLast(new MessageToMessageDecoder<SocketAddress>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, SocketAddress msg, List<Object> out) {
                        System.out.println("socket");
                        System.out.println(msg);
                    }
                });
                pipeline.addLast(new DatagramPacketDecoder(new ProtobufDecoder(MessageStub.Message.getDefaultInstance())));

                pipeline.addLast(new DatagramPacketEncoder<>(new ProtobufEncoder()));
                pipeline.addLast(new ServerUDPDecoder());

            }
        });

        bootstrap.bind(9001).sync().channel().closeFuture();

        System.out.println("start....");
    }


}
