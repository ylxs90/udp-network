package c.s.l.network;

import c.s.l.network.paket.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class DatagramRepacketDecoder extends DatagramPacketDecoder {

    private MessageToMessageDecoder<ByteBuf> decoder;

    /**
     * Create a {@link DatagramPacket} decoder using the specified {@link ByteBuf} decoder.
     *
     * @param decoder the specified {@link ByteBuf} decoder
     */
    public DatagramRepacketDecoder(MessageToMessageDecoder<ByteBuf> decoder) {
        super(decoder);
        this.decoder = decoder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) {
        MessagePacket packet = new MessagePacket();


    }

}
