package github.yuanlin.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.yuanlin.exception.SerializeException;
import github.yuanlin.serialize.Serializer;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:22
 */
public class KryoSerializer  implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Output output = new Output(outputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException(e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             Input input = new Input(inputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            T result = kryo.readObject(input, cls);
            kryoThreadLocal.remove();
            return cls.cast(result);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage());
        }
    }
}
