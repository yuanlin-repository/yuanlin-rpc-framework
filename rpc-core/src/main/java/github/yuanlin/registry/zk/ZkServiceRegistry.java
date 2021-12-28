package github.yuanlin.registry.zk;

import github.yuanlin.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * 服务注册（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:01
 */
public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress serviceAddress) {

    }
}
