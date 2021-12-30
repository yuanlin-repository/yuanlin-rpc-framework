package github.yuanlin.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例对象工厂
 *
 * @author yuanlin
 * @date 2021/12/30/16:21
 */
public class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        String key = cls.toString();
        if (OBJECT_MAP.containsKey(key)) {
            return cls.cast(OBJECT_MAP.get(key));
        } else {
            return cls.cast(OBJECT_MAP.computeIfAbsent(key, k -> {
                try {
                    return cls.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));
        }
    }
}
