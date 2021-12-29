package github.yuanlin.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author yuanlin
 * @date 2021/12/28/11:24
 */
public interface ServiceRegistry {

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param serviceAddress 服务地址
     */
    void registerService(String serviceName, InetSocketAddress serviceAddress);
}
