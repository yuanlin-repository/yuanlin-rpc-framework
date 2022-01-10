package github.yuanlin.proxy;

import github.yuanlin.annotation.RpcAutowire;
import github.yuanlin.config.ServiceConfig;
import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.enums.ResponseCodeEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.transport.RpcClient;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * RPC 动态代理类
 *
 * @author yuanlin
 * @date 2022/01/10/21:13
 */
public class RpcDynamicProxy implements InvocationHandler {

    /**
     * Rpc 客户端
     */
    private final RpcClient rpcClient;
    /**
     * 服务信息
     */
    private final ServiceConfig config;

    public RpcDynamicProxy(RpcClient rpcClient, ServiceConfig config) {
        this.rpcClient = rpcClient;
        this.config = config;
    }

    public <T> Object getProxy(Class<T> interfaceClass, RpcAutowire rpcAutowire) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), interfaceClass.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .interfaceName(config.getInterfaceName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .group(config.getGroup())
                .version(config.getVersion())
                .build();
        RpcResponse<Object> rpcResponse = rpcClient.sendRequest(rpcRequest);
        checkForResponse(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }

    private void checkForResponse(RpcRequest rpcRequest, RpcResponse<Object> rpcResponse) {
        if (rpcResponse == null) {
            throw new RpcException(ErrorEnum.RPC_INVOCATION_FAILURE, String.format("service interface: [%s]", rpcRequest.getInterfaceName()));
        }
        if (!(rpcRequest.getRequestId().equals(rpcResponse.getRequestId()))) {
            throw new RpcException(ErrorEnum.REQUEST_NOT_MATCH_RESPONSE, String.format("service interface: [%s]", rpcRequest.getInterfaceName()));
        }
        if (rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(ErrorEnum.RPC_INVOCATION_FAILURE, String.format("service interface: [%s]", rpcRequest.getInterfaceName()));
        }
    }
}
