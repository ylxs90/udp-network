package c.s.l.network.proto;

import com.google.protobuf.util.JsonFormat;
import org.junit.Test;

import java.io.IOException;

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

}