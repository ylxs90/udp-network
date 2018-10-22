package c.s.l.network.proto;

import com.google.protobuf.util.JsonFormat;
import org.junit.Test;

import java.io.IOException;

public class MessageOuterClassTest {

    @Test
    public void test() throws IOException {
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
                .setCmd(MessageOuterClass.Message.CMD.CLOSE)
                .setUserId(123)
                .setMsg("hello")
                .setData("hello").build();

        System.out.println(message);
        message.writeDelimitedTo(System.out);
        JsonFormat.Printer printer = JsonFormat.printer().printingEnumsAsInts().omittingInsignificantWhitespace();
        String json = printer.print(message);
        System.out.println();
        System.out.println(json);
        MessageOuterClass.Message.Builder builder = MessageOuterClass.Message.newBuilder();
        JsonFormat.parser().merge(json, builder);
        System.out.println(builder.build());

    }

}