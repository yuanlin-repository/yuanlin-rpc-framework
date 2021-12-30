package github.yuanlin.transport.dto;

import github.yuanlin.enums.ResponseCodeEnum;
import lombok.*;

/**
 * RPC 响应
 *
 * @author yuanlin
 * @date 2021/12/28/11:37
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> {

    private String requestId;

    private Integer statusCode;

    private String message;

    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> rpcResponse = new RpcResponse<>();
        rpcResponse.setRequestId(requestId);
        rpcResponse.setStatusCode(ResponseCodeEnum.SUCCESS.getCode());
        rpcResponse.message = ResponseCodeEnum.SUCCESS.getMessage();
        if (data != null) {
            rpcResponse.setData(data);
        }
        return rpcResponse;
    }

    public static <T> RpcResponse<T> fail(ResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setStatusCode(rpcResponseCodeEnum.getCode());
        rpcResponse.setMessage(rpcResponseCodeEnum.getMessage());
        return rpcResponse;
    }
}
