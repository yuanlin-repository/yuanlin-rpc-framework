package github.yuanlin.provider.nacos;

import github.yuanlin.provider.AbstractServiceProvider;
import github.yuanlin.registry.ServiceRegistry;
import github.yuanlin.registry.nacos.NacosServiceRegistry;

import java.net.InetSocketAddress;

/**
 * 服务提供者（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2022/01/08/22:10
 */
public class NacosServiceProvider extends AbstractServiceProvider {

    public NacosServiceProvider() {
        ServiceRegistry nacos = new NacosServiceRegistry();
        super.serviceRegistry = nacos;
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

    }
}
