package github.yuanlin.serialize.json;

import github.yuanlin.exception.SerializeException;
import github.yuanlin.serialize.Serializer;
import github.yuanlin.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 待完善
 * json 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
@Slf4j
public class JsonSerializer  implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] data;
        try {
            data = JsonUtils.toJson(obj).getBytes();
        } catch (IOException e) {
            log.error("error occured while serializing:", e);
            throw new SerializeException(e.getMessage());
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        T result;
        try {
            result = JsonUtils.toObject(data, cls);
        } catch (IOException e) {
            log.error("error occured while deserializing:", e);
            throw new SerializeException(e.getMessage());
        }
        return result;
    }
}
