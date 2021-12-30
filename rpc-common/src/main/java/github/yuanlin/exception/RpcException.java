package github.yuanlin.exception;

import github.yuanlin.enums.ErrorEnum;

/**
 * RPC 运行时异常
 *
 * @author yuanlin
 * @date 2021/12/28/15:59
 */
public class RpcException extends RuntimeException {

    public RpcException(ErrorEnum errorMessageEnum) {
        super(errorMessageEnum.getMessage());
    }

    public RpcException(ErrorEnum errorMessageEnum, String detail) {
        super(errorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
