package github.yuanlin.transport;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.factory.SingletonFactory;
import github.yuanlin.provider.ServiceProvider;
import github.yuanlin.provider.AbstractServiceProvider;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * RPC 服务器抽象类
 *
 * @author yuanlin
 * @date 2021/12/30/16:38
 */
public abstract class AbstractServer implements RpcServer {

    protected String host = InetAddress.getLoopbackAddress().getHostAddress();
    protected int port = 8002;
    /**
     * 存放扫描到的标注 @RpcService 的服务
     */
    protected Set<ServiceConfig> serviceConfigs = new HashSet<>();
    /**
     * 服务提供
     */
    protected ServiceProvider serviceProvider;

    public AbstractServer() {
        serviceProvider = ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension("serviceProvider");
    }

    @Override
    public <T> void publishService(ServiceConfig config) {
        serviceProvider.addService(config.getServiceName(), config.getService(), new InetSocketAddress(host, port));
    }
}
