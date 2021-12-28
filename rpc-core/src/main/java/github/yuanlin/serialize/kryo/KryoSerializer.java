package github.yuanlin.serialize.kryo;

import github.yuanlin.serialize.Serializer;

/**
 * kryo 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
public class KryoSerializer  implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        return null;
    }
}
