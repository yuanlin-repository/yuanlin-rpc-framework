package github.yuanlin.serialize.json;

import github.yuanlin.serialize.Serializer;

/**
 * json 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
public class JsonSerializer  implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        return null;
    }
}
