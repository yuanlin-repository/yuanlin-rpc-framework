package github.yuanlin.registry;

import github.yuanlin.registry.nacos.NacosServiceDiscovery;
import github.yuanlin.registry.nacos.NacosServiceRegistry;
import github.yuanlin.registry.zk.ZkServiceDiscovery;
import github.yuanlin.registry.zk.ZkServiceRegistry;
import github.yuanlin.transport.dto.RpcRequest;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * 测试注册中心
 *
 * @author yuanlin
 * @date 2021/12/28/19:40
 */
public class TestRegistryAndDiscovery {

    /**
     * 测试样例：
     *  1.注册中心未启动
     *  2.注册中心启动
     *  3.注册不同接口实现类
     *  4.注册同一接口不同实现类
     *  5.未找到服务
     *  6.重复注册
     */

    @Test
    public void testZooKeeperRegistryAndDiscovery() {
        ServiceRegistry registry = new ZkServiceRegistry();
        ServiceDiscovery discovery = new ZkServiceDiscovery();
        // --------------------- 测试样例 ----------------------- //
        // 1.注册中心未启动
        // 2.注册中心启动
        // 3.注册不同接口实现类
        RpcRequest rpcRequest1 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("")
                .version("")
                .build();
        RpcRequest rpcRequest2 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HiService")
                .group("")
                .version("")
                .build();
        registry.registerService(rpcRequest1.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        registry.registerService(rpcRequest2.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        InetSocketAddress address1 = discovery.lookupService(rpcRequest1);
        InetSocketAddress address2 = discovery.lookupService(rpcRequest2);
        System.out.println("rpcRequest1 服务地址: " + address1);
        System.out.println("rpcRequest2 服务地址: " + address2);
        // 4.注册同一接口不同实现类
        RpcRequest rpcRequest3 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("test")
                .version("01")
                .build();
        RpcRequest rpcRequest4 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("test")
                .version("02")
                .build();
        registry.registerService(rpcRequest3.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        registry.registerService(rpcRequest4.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        InetSocketAddress address3 = discovery.lookupService(rpcRequest3);
        InetSocketAddress address4 = discovery.lookupService(rpcRequest4);
        System.out.println("rpcRequest3 服务地址: " + address3);
        System.out.println("rpcRequest4 服务地址: " + address4);
        // 5.重复注册
        registry.registerService(rpcRequest1.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        // 6.未找到服务
        rpcRequest1.setInterfaceName("github.yuanlin.ok");
        InetSocketAddress address5 = discovery.lookupService(rpcRequest1);
        // --------------------- 测试样例 ----------------------- //
    }

    @Test
    public void testNacosRegistryAndDiscovery() {
        ServiceRegistry registry = new NacosServiceRegistry();
        ServiceDiscovery discovery = new NacosServiceDiscovery();
        // --------------------- 测试样例 ----------------------- //
        // 1.注册中心未启动
        // 2.注册中心启动
        // 3.注册不同接口实现类
        RpcRequest rpcRequest1 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("")
                .version("")
                .build();
        RpcRequest rpcRequest2 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HiService")
                .group("")
                .version("")
                .build();
        registry.registerService(rpcRequest1.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        registry.registerService(rpcRequest2.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        InetSocketAddress address1 = discovery.lookupService(rpcRequest1);
        InetSocketAddress address2 = discovery.lookupService(rpcRequest2);
        System.out.println("rpcRequest1 服务地址: " + address1);
        System.out.println("rpcRequest2 服务地址: " + address2);
        // 4.注册同一接口不同实现类
        RpcRequest rpcRequest3 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("test")
                .version("01")
                .build();
        RpcRequest rpcRequest4 = RpcRequest.builder()
                .interfaceName("github.yuanlin.HelloService")
                .group("test")
                .version("02")
                .build();
        registry.registerService(rpcRequest3.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        registry.registerService(rpcRequest4.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        InetSocketAddress address3 = discovery.lookupService(rpcRequest3);
        InetSocketAddress address4 = discovery.lookupService(rpcRequest4);
        System.out.println("rpcRequest3 服务地址: " + address3);
        System.out.println("rpcRequest4 服务地址: " + address4);
        // 5.重复注册
        registry.registerService(rpcRequest1.getServiceName(), new InetSocketAddress("127.0.0.1", 8001));
        // 6.未找到服务
        rpcRequest1.setInterfaceName("github.yuanlin.ok");
        InetSocketAddress address5 = discovery.lookupService(rpcRequest1);
        // --------------------- 测试样例 ----------------------- //
    }
}
