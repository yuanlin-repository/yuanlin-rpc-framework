package github.yuanlin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  RPC 响应码枚举类
 *
 * @author yuanlin
 * @date 2021/12/30/10:00
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS(200, "远程调用成功"),
    FAIL(500, "远程调用失败");

    private final int code;
    private String message;
}
