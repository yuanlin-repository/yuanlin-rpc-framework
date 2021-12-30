package github.yuanlin.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 服务注册（基于 Nacos 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/13:59
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress serviceAddress) {
        try {
            NacosUtils.registerService(serviceName, serviceAddress);
        } catch (NacosException e) {
            log.error("register service [{}] fail", serviceName, e);
            throw new RpcException(ErrorEnum.RIGISTER_SERVICE_FAILED);
        }
    }
}
