package c.s.l.network;

import c.s.l.network.proto.MessageStub;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ServerUDPDecoder extends MessageToMessageDecoder<MessageStub.Message> {


    @Override
    protected void decode(ChannelHandlerContext ctx, MessageStub.Message msg, List<Object> out) {
        System.out.println(msg);
    }
}
