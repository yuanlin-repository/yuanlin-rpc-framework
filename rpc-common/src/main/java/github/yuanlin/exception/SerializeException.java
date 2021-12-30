package github.yuanlin.exception;

/**
 * 序列化异常
 *
 * @author yuanlin
 * @date 2021/12/30/10:22
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String message) {
        super(message);
    }
}
