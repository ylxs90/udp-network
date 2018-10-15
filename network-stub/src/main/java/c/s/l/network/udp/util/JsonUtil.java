package c.s.l.network.udp.util;

import com.google.gson.Gson;

import java.io.Serializable;

public class JsonUtil {
    private static final Gson gson = new Gson();


    public static String serialize(Serializable obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

}
