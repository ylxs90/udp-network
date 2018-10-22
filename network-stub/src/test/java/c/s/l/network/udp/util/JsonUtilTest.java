package c.s.l.network.udp.util;

import c.s.l.network.udp.Message;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.io.Serializable;

public class JsonUtilTest {

    @Test
    public void test() {

        Message msg = Message.builder().cmd((byte) 1).data("123").build();
        String json = JsonUtil.serialize(msg);
        System.out.println(json);
        Message deserialize = JsonUtil.deserialize(json, Message.class);
        System.out.println(deserialize);
        System.out.println(deserialize.getData().getClass());
        Gson gson = new Gson();
        Message fromJson = gson.fromJson(json, new TypeToken<Message>() {
        }.getType());


        System.out.println(fromJson);
        System.out.println(fromJson.getData().getClass());
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class User implements Serializable {
    private static final long serialVersionUID = -2102920162784668288L;
    private int id;
    private String username;
}