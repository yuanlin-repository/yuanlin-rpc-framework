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
    RIGISTER_SERVICE_FAILED("注册服务失败"),
    SERIALIZE_FAILED("序列化时发生错误"),
    ENCODE_FRAME_ERROR("编码时发生错误"),
    DECODE_FRAME_ERROR("解码时发生错误"),
    UNKNOWN_PROTOCAL("协议包无法识别"),
    UNSUPPORTED_PROTOCAL_VERSION("不支持的协议版本"),
    CALL_SERVICE_ERROR("调用服务失败");

    private final String message;
}
