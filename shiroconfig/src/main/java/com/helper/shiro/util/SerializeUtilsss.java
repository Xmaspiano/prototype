package com.helper.shiro.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/9/30 - 上午8:48
 * Created by IntelliJ IDEA.
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtilsss {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtilsss.class);

    public SerializeUtilsss() {
    }

    /**
     * FastJson反序列化对象
     * @param bytes
     * @return
     */
    public static<T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (isEmpty(bytes)) {
            return null;
        }

        System.out.println("===");
        System.out.println(new String(bytes));
        System.out.println("===");

        JSONObject jsonObj = (JSONObject) JSON.parse(new String(bytes));
        return JSONObject.toJavaObject(jsonObj, clazz);
    }

    public static boolean isEmpty(byte[] data) {

        return data == null || data.length == 0;
    }

    /**
     * Gson序列化对象
     * @param object
     * @return
     */
    public static<T> byte[] serialize(T object) {
        System.out.println("===>>");
        System.out.println(JSONObject.toJSON(object));
        if (object == null) {
            return new byte[0];
        }
        Gson gson = new Gson();
        String json = JSONObject.toJSON(object).toString();
//        String json = gson.toJson(object);
        System.out.println(json);
        System.out.println("===>>");
        return json.getBytes();
    }
}
