package github.yuanlin.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import github.yuanlin.exception.SerializeException;
import github.yuanlin.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian 序列化实现
 *
 * @author yuanlin
 * @date 2021/12/28/12:21
 */
@Slf4j
public class HessianSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("error occured while serializing:", e);
            throw new SerializeException(e.getMessage());
        } finally {
            if (hessianOutput != null) {
                try {
                    hessianOutput.close();
                } catch (IOException e) {
                    log.error("error occured while serializing:", e);
                    throw new SerializeException(e.getMessage());
                }
            }
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            hessianInput = new HessianInput(byteArrayInputStream);
            return (T) hessianInput.readObject();
        } catch (IOException e) {
            log.error("error occured while deserializing:", e);
            throw new SerializeException(e.getMessage());
        } finally {
            if (hessianInput != null) hessianInput.close();
        }
    }
}
