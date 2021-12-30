package github.yuanlin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC 错误枚举类
 *
 * @author yuanlin
 * @date 2021/12/28/16:00
 */
@AllArgsConstructor
@Getter
public enum ErrorEnum {

    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),
    FAILED_TO_CONNECT_TO_REGISTRY("连接注册中心失败"),
    RIGISTER_SERVICE_FAILED("注册服务失败");

    private final String message;
}
