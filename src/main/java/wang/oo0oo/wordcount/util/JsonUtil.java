package wang.oo0oo.wordcount.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    /**
     * 将JSON转换为集合
     */
    public static List fromJson(String json, Class itemClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(List.class, itemClass);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(boolean success, String key, Integer val) {
        Map<String, Integer> jsonMap = new LinkedHashMap<>();
        jsonMap.put(key, val);
        return toJson(success, jsonMap);
    }

    public static String toJson(boolean success, Map<String, Integer> jsonMap) {
        StringBuffer buffer = new StringBuffer();
        if (success) {
            buffer.append("{\"success\":true,\"data\":[");
        } else {
            buffer.append("{success:false}");
        }
        if (jsonMap.size() > 0) {
            for (String key : jsonMap.keySet()) {
                if (!key.equals(("class"))) {
                    buffer.append("{" + '"' + key + '"' + ":" + jsonMap.get(key) + "},");
                }
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]}");
        return buffer.toString();
    }
}
