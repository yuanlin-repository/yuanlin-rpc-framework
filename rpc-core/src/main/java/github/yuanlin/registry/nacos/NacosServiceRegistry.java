package github.yuanlin.registry.nacos;

import github.yuanlin.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * 服务注册（基于 Nacos 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/13:59
 */
public class NacosServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress serviceAddress) {

    }
}
