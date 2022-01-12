package github.yuanlin.provider.zk;

import github.yuanlin.provider.AbstractServiceProvider;
import github.yuanlin.provider.ServiceProvider;
import github.yuanlin.registry.ServiceRegistry;
import github.yuanlin.registry.utils.CuratorUtils;
import github.yuanlin.registry.zk.ZkServiceRegistry;

import java.net.InetSocketAddress;

/**
 * 服务提供者（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2022/01/08/21:32
 */
public class ZkServiceProvider extends AbstractServiceProvider {

    public ZkServiceProvider() {
        ServiceRegistry zk = new ZkServiceRegistry();
        super.serviceRegistry = zk;
    }

    @Override
    public Object getService(String serviceName) {
        return super.getService(serviceName);
    }

    @Override
    public <T> void addService(String serviceName, T service, InetSocketAddress serviceAddress) {
        super.addService(serviceName, service, serviceAddress);
    }

    @Override
    public void clearService(InetSocketAddress serviceAddress) {
        CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), serviceAddress);
    }
}
