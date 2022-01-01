package github.yuanlin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化方法枚举类
 *
 * @author yuanlin
 * @date 2022/01/01/11:26
 */
@Getter
@AllArgsConstructor
public enum SerializationEnum {

    HESSIAN((byte) 0x01, "hessian"),
    JSON((byte) 0x02, "json"),
    KRYO((byte) 0x03, "kryo"),
    PROTOSTUFF((byte) 0x04, "protostuff");

    private final byte code;
    private final String name;

    public static String lookup(byte code) {
        for (SerializationEnum c : SerializationEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
