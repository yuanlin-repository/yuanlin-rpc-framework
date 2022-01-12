package github.yuanlin.netty;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.netty.service.HelloService;
import github.yuanlin.netty.service.HelloServiceImpl;
import github.yuanlin.proxy.RpcDynamicProxy;
import github.yuanlin.transport.netty.client.NettyClient;
import github.yuanlin.transport.netty.server.NettyServer;
import org.junit.Test;

/**
 * 测试使用代理对象进行远程调用
 *
 * @author yuanlin
 * @date 2022/01/12/11:58
 */
public class TestRpcDynamicProxy {

    /**
     * receive msg: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=78f8031c-4611-414e-8d52-0efeb150784e, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello, parameterTypes=[class java.lang.String], parameters=[hello test ], version=01, group=test))]
     * server get result: [hello test  world]
     */
    @Test
    public void testRpcDynamicProxy_server() {
        // 1. 创建服务端
        NettyServer server = new NettyServer("127.0.0.1", 8002);
        // 2. 发布服务
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        server.publishService(config);
        // 3. 启动服务端
        server.start();
    }

    /**
     * client send message: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=78f8031c-4611-414e-8d52-0efeb150784e, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello, parameterTypes=[class java.lang.String], parameters=[hello test ], version=01, group=test))]
     * codec name: [hessian]
     * client receive response: [RpcMessage(requestId=1, messageType=2, codec=1, payload=RpcResponse(requestId=78f8031c-4611-414e-8d52-0efeb150784e, statusCode=200, message=远程调用成功, data=hello test  world))]
     * hello test  world
     */
    @Test
    public void testRpcDynamicProxy_client() {
        // 1. 创建客户端
        NettyClient client = new NettyClient();
        HelloService service = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(service)
                .group("test")
                .version("01")
                .build();
        // 2. 创建代理对象
        RpcDynamicProxy proxy = new RpcDynamicProxy(client, config);
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 3. 远程调用
        System.out.println(helloService.hello("hello "));
    }
}
