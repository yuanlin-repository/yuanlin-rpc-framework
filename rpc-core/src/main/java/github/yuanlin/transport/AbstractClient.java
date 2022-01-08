package github.yuanlin.transport;

import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.registry.ServiceDiscovery;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * RPC 客户端抽象类
 *
 * @author yuanlin
 * @date 2022/01/08/19:58
 */
public abstract class AbstractClient implements RpcClient {

    /**
     * 数据报ID生成
     */
    protected final AtomicInteger requestIdProvider = new AtomicInteger();
    /**
     * 服务发现
     */
    protected final ServiceDiscovery serviceDiscovery;

    public AbstractClient() {
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("nacos");
    }
}
