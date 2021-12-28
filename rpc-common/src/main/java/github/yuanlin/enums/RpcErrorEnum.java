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
public enum RpcErrorEnum {

    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务");

    private final String message;
}
