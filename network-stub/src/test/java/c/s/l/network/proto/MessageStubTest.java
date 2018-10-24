package c.s.l.network.proto;

import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.TypeParameterMatcher;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MessageStubTest {

    @Test
    public void test() throws IOException {
        MessageStub.Message message = MessageStub.Message.newBuilder()
                .setCmd(MessageStub.Message.CMD.CLOSE)
                .setUserId(123)
                .setMsg("hello")
                .setData("hello").build();

        System.out.println(message);
        message.writeDelimitedTo(System.out);
        JsonFormat.Printer printer = JsonFormat.printer().printingEnumsAsInts().omittingInsignificantWhitespace();
        String json = printer.print(message);
        System.out.println();
        System.out.println(json);
        MessageStub.Message.Builder builder = MessageStub.Message.newBuilder();
        JsonFormat.parser().merge(json, builder);
        System.out.println(builder.build());

    }

    @Test
    public void test2() {
        class Mydecoder extends MessageToMessageDecoder<String> {

            @Override
            protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) {

            }
        }
        Mydecoder mydecoder = new Mydecoder();
        TypeParameterMatcher matcher = TypeParameterMatcher.find(mydecoder, MessageToMessageDecoder.class, "I");
        System.out.println(matcher);
        System.out.println(matcher.match("hello"));
        System.out.println(matcher.match(123));
    }

}