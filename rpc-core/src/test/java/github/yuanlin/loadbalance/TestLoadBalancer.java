package github.yuanlin.loadbalance;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.loadbalance.loadbalancer.ConsistentHashLoadBalancer;
import github.yuanlin.loadbalance.loadbalancer.RandomLoadBalancer;
import github.yuanlin.loadbalance.loadbalancer.RoundRobinLoadBalancer;
import github.yuanlin.netty.service.HelloService;
import github.yuanlin.netty.service.HelloServiceImpl;
import github.yuanlin.transport.dto.RpcRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试负载均衡
 *
 * @author yuanlin
 * @date 2022/01/12/15:39
 */
public class TestLoadBalancer {

    @Test
    public void testRoundRobinLoadBalancer() {
        LoadBalancer balancer = new RoundRobinLoadBalancer();
        // 服务地址
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:8001", "127.0.0.1:8002", "127.0.0.1:8003"));
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName(config.getInterfaceName())
                .methodName("hello")
                .parameterTypes(new Class[]{String.class})
                .parameters(new Object[]{"hello"})
                .group("test")
                .version("01")
                .build();
        System.out.println("第一次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第二次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第三次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第四次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第五次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第六次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
    }

    @Test
    public void testRandomLoadBalancer() {
        LoadBalancer balancer = new RandomLoadBalancer();
        // 服务地址
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:8001", "127.0.0.1:8002", "127.0.0.1:8003"));
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName(config.getInterfaceName())
                .methodName("hello")
                .parameterTypes(new Class[]{String.class})
                .parameters(new Object[]{"hello"})
                .group("test")
                .version("01")
                .build();
        System.out.println("第一次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第二次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第三次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第四次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第五次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第六次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
    }

    @Test
    public void testConsistentHashLoadBalancer() {
        ConsistentHashLoadBalancer balancer = new ConsistentHashLoadBalancer();
        // 服务地址
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:8001", "127.0.0.1:8002", "127.0.0.1:8003"));
        HelloService helloService = new HelloServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(helloService)
                .group("test")
                .version("01")
                .build();
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId("123456")
                .interfaceName(config.getInterfaceName())
                .methodName("hello")
                .parameterTypes(new Class[]{String.class})
                .parameters(new Object[]{"hello222234234234"})
                .group("test")
                .version("01")
                .build();
        System.out.println("第一次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第二次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第三次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第四次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第五次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
        System.out.println("第六次: " + balancer.selectServiceAddress(serviceUrlList, rpcRequest));
//        ConsistentHashLoadBalancer.ConsistentHashSelector selector = balancer.selectors.get(rpcRequest.getServiceName());
//        String serviceName = rpcRequest.getServiceName();
//        System.out.println(ConsistentHashLoadBalancer.ConsistentHashSelector.md5(serviceName + Arrays.asList(rpcRequest.getParameters())));
//        System.out.println(serviceName + Arrays.asList(rpcRequest.getParameters()));
//
//        System.out.println(ConsistentHashLoadBalancer.ConsistentHashSelector.md5(serviceName + Arrays.asList(rpcRequest.getParameters())));
//        System.out.println(serviceName + Arrays.asList(rpcRequest.getParameters()));
//
//        System.out.println(ConsistentHashLoadBalancer.ConsistentHashSelector.md5(serviceName + Arrays.asList(rpcRequest.getParameters())));
//        System.out.println(serviceName + Arrays.asList(rpcRequest.getParameters()));
//
//        System.out.println(ConsistentHashLoadBalancer.ConsistentHashSelector.md5(serviceName + Arrays.asList(rpcRequest.getParameters())));
//        System.out.println(serviceName + Arrays.asList(rpcRequest.getParameters()));
    }
}
