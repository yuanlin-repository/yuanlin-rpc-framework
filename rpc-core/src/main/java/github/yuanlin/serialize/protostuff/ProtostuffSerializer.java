package github.yuanlin.serialize.protostuff;

import github.yuanlin.serialize.Serializer;

/**
 * protostuff 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
public class ProtostuffSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        return null;
    }
}
