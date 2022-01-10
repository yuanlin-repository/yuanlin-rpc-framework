package github.yuanlin.transport;

import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;

import java.util.concurrent.CompletableFuture;

/**
 * RPC 客户端接口
 *
 * @author yuanlin
 * @date 2021/12/28/12:47
 */
public interface RpcClient {

    /**
     * 发送 RPC 请求
     * @param rpcRequest RPC 请求
     * @return RPC 响应
     */
    RpcResponse<Object> sendRequest(RpcRequest rpcRequest);
}
