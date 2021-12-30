package github.yuanlin.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * Json 工具类
 *
 * @author yuanlin
 * @date 2021/12/30/10:33
 */
@Slf4j
public class JsonUtils {

    public static final ObjectMapper JACKSON_OBJECT_MAPPER = new ObjectMapper();

    static {
        JACKSON_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        JACKSON_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许json中包含非引号控制字符
        JACKSON_OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        JACKSON_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JACKSON_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> T toObject(byte[] content, Class<T> cls) throws IOException {
        return JACKSON_OBJECT_MAPPER.readValue(content, cls);
    }

    public static String toJson(Object obj) throws IOException {
        return JACKSON_OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static String toJsonWithOutException(Object object) {
        try {
            return toJson(object);
        } catch (IOException e) {
            log.warn("obj to str fail: {}", object, e);
        }
        return "";
    }

    public static <T> T toObject(String content, Class<T> cls) throws IOException {
        return JACKSON_OBJECT_MAPPER.readValue(content, cls);
    }

    public static <T> T toObjectWithoutException(String content, Class<T> cls) {
        try {
            return JACKSON_OBJECT_MAPPER.readValue(content, cls);
        } catch (IOException e) {
            log.warn("str to obj fail: {}", content, e);
        }
        return null;
    }

    public static <T> T mapToObj(Map<String, Object> map, Class<T> classz) {
        try {
            return JACKSON_OBJECT_MAPPER.readValue(toJson(map), classz);
        } catch (IOException e) {
            log.warn("map to obj error: {}", map, e);
        }
        return null;
    }
}
