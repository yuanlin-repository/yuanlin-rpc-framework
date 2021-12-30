package github.yuanlin.serialize.protostuff;

import github.yuanlin.serialize.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
public class ProtostuffSerializer implements Serializer {
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    @Override
    public <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        byte[] data = new byte[0];
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        Schema<T> schema = (Schema<T>) getSchema(cls);
        T message = schema.newMessage();
        try {
            ProtostuffIOUtil.mergeFrom(data, message, schema);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return message;
    }

    private <T> Schema getSchema(Class<T> cls) {
        if (cachedSchema.containsKey(cls)) {
            return cachedSchema.get(cls);
        }
        Schema<T> schema = RuntimeSchema.getSchema(cls);
        if (schema != null) {
            cachedSchema.put(cls, schema);
        }
        return schema;
    }
}
