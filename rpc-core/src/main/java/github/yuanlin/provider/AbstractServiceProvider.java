package github.yuanlin.provider;

import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.provider.ServiceProvider;
import github.yuanlin.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceProvider 接口实现
 *
 * @author yuanlin
 * @date 2021/12/28/13:23
 */
@Slf4j
public abstract class AbstractServiceProvider implements ServiceProvider {

    /**
     * key : value => serviceName : serviceBean
     */
    protected final Map<String, Object> registeredService = new ConcurrentHashMap<>();
    /**
     * 注册中心
     */
    protected ServiceRegistry serviceRegistry;

    @Override
    public <T> void addService(String serviceName, T service, InetSocketAddress serviceAddress) {
        if (registeredService.containsKey(serviceName)) {
            return;
        }
        String host = "127.0.0.1";
        serviceRegistry.registerService(serviceName, serviceAddress);
        registeredService.put(serviceName, service);
        log.info("publish service {} => {}", serviceName, serviceAddress);
    }

    @Override
    public Object getService(String serviceName) {
        Object serviceBean = registeredService.get(serviceName);
        if (null == serviceBean) {
            throw new RpcException(ErrorEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return serviceBean;
    }
}
