package github.yuanlin.serialize;

/**
 * 序列化接口
 *
 * @author yuanlin
 * @date 2021/12/28/11:23
 */
public interface Serializer {

    /**
     * 序列化方法
     *
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化方法
     *
     * @param data 字节数组
     * @param cls 目标类
     * @param <T> 目标类的类型
     * @return 反序列化对象
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
