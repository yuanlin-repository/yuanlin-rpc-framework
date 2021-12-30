package github.yuanlin.provider;

import java.net.InetSocketAddress;

/**
 * 服务对象实例提供（服务器端使用）
 *
 * @author yuanlin
 * @date 2021/12/28/13:16
 */
public interface ServiceProvider {

    /**
     * 添加服务 key : value => serviceName : serviceBean
     * @param serviceName 服务名称
     * @param serviceBean 服务实例
     * @param serviceAddress 服务地址
     * @param <T> 实例类型
     */
    <T> void addService(String serviceName, T serviceBean, InetSocketAddress serviceAddress);

    /**
     * 根据服务名称获取服务实例
     * @param serviceName 服务名称
     * @return 服务实例
     */
    Object getService(String serviceName);
}
