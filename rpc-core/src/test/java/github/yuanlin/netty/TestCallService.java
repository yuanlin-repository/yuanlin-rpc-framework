package github.yuanlin.netty;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.netty.model.Student;
import github.yuanlin.netty.service.HelloService;
import github.yuanlin.netty.service.HelloServiceImpl;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;
import github.yuanlin.transport.netty.client.NettyClient;
import github.yuanlin.transport.netty.server.NettyServer;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 测试客户端发送请求，服务端反射调用返回结果
 * 测试样例：
 *      1.方法一个参数
 *      2.方法无参
 *      3.方法多个参数
 *      4.异常情况：没有找到服务，找到服务但没有相应方法
 *
 * @author yuanlin
 * @date 2022/01/09/20:22
 */
public class TestCallService {

    /*------------ 测试样例1: 方法包含一个参数------------------*/

    /**
     * receive msg: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello, parameterTypes=[class java.lang.String], parameters=[hello], version=01, group=test))]
     * server get result: [hello world]
     */
    @Test
    public void testCallService_Server1() throws ExecutionException, InterruptedException {
        // 1. 创建服务端
        NettyServer server = new NettyServer("127.0.0.1", 8002);
        // 2. 发布服务
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        server.publishService(config.getServiceName(), config.getService());
        // 3. 启动服务端
        server.start();
    }

    /**
     * client send message: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello, parameterTypes=[class java.lang.String], parameters=[hello], version=01, group=test))]
     * receive msg: [RpcMessage(requestId=1, messageType=2, codec=1, payload=RpcResponse(requestId=123456, statusCode=200, message=远程调用成功, data=hello world))]
     */
    @Test
    public void testCallService_Client1() throws ExecutionException, InterruptedException {
        // 1. 创建客户端
        NettyClient client = new NettyClient();
        // 2. 发送RPC请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName("github.yuanlin.netty.service.HelloService")
                .methodName("hello")
                .parameterTypes(new Class[]{String.class})
                .parameters(new Object[]{"hello"})
                .group("test")
                .version("01")
                .build();
        CompletableFuture<RpcResponse<Object>> future = client.sendRequest(rpcRequest);
        // 3. 阻塞等待服务端响应
        RpcResponse<Object> response = future.get();
    }

    /*------------ 测试样例2: 方法无参 ------------------*/

    /**
     * receive msg: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello2, parameterTypes=[], parameters=[], version=01, group=test))]
     * server get result: [你好 世界~]
     */
    @Test
    public void testCallService_Server2() throws ExecutionException, InterruptedException {
        // 1. 创建服务端
        NettyServer server = new NettyServer("127.0.0.1", 8002);
        // 2. 发布服务
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        server.publishService(config.getServiceName(), config.getService());
        // 3. 启动服务端
        server.start();
    }

    /**
     * client send message: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello2, parameterTypes=[], parameters=[], version=01, group=test))]
     * receive msg: [RpcMessage(requestId=1, messageType=2, codec=1, payload=RpcResponse(requestId=123456, statusCode=200, message=远程调用成功, data=你好 世界~))]
     */
    @Test
    public void testCallService_Client2() throws Exception {
        // 1. 创建客户端
        NettyClient client = new NettyClient();
        // 2. 发送RPC请求
        Method method = HelloService.class.getMethod("hello2");
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName("github.yuanlin.netty.service.HelloService")
                .methodName("hello2")
                .parameterTypes(method.getParameterTypes())
                .parameters(new Object[]{})
                .group("test")
                .version("01")
                .build();
        CompletableFuture<RpcResponse<Object>> future = client.sendRequest(rpcRequest);
        // 3. 阻塞等待服务端响应
        RpcResponse<Object> response = future.get();
    }

    /*------------ 测试样例3: 方法有多个参数 ------------------*/

    /**
     * receive msg: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello3, parameterTypes=[class java.lang.String, class java.lang.Integer, class github.yuanlin.netty.model.Student], parameters=[你好, 100, Student(name=张三, age=18)], version=01, group=test))]
     * server get result: [你好::100::Student(name=张三, age=18)]
     */
    @Test
    public void testCallService_Server3() throws ExecutionException, InterruptedException {
        // 1. 创建服务端
        NettyServer server = new NettyServer("127.0.0.1", 8002);
        // 2. 发布服务
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        server.publishService(config.getServiceName(), config.getService());
        // 3. 启动服务端
        server.start();
    }

    /**
     * client send message: [RpcMessage(requestId=1, messageType=1, codec=1, payload=RpcRequest(requestId=123456, interfaceName=github.yuanlin.netty.service.HelloService, methodName=hello3, parameterTypes=[class java.lang.String, class java.lang.Integer, class github.yuanlin.netty.model.Student], parameters=[你好, 100, Student(name=张三, age=18)], version=01, group=test))]
     * receive msg: [RpcMessage(requestId=1, messageType=2, codec=1, payload=RpcResponse(requestId=123456, statusCode=200, message=远程调用成功, data=你好::100::Student(name=张三, age=18)))]
     */
    @Test
    public void testCallService_Client3() throws Exception {
        // 1. 创建客户端
        NettyClient client = new NettyClient();
        // 2. 发送RPC请求
        Method method = HelloService.class.getMethod("hello3", String.class, Integer.class, Student.class);
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName("github.yuanlin.netty.service.HelloService")
                .methodName("hello3")
                .parameterTypes(method.getParameterTypes())
                .parameters(new Object[]{"你好", 100, Student.builder().name("张三").age(18).build()})
                .group("test")
                .version("01")
                .build();
        CompletableFuture<RpcResponse<Object>> future = client.sendRequest(rpcRequest);
        // 3. 阻塞等待服务端响应
        RpcResponse<Object> response = future.get();
    }
}