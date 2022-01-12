package github.yuanlin.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.loadbalance.LoadBalancer;
import github.yuanlin.registry.ServiceDiscovery;
import github.yuanlin.registry.utils.NacosUtils;
import github.yuanlin.transport.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现（基于 Nacos 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:00
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery() {
        this.loadBalancer = ExtensionLoader.getExtensionLoader(LoadBalancer.class).getExtension("loadbalancer");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        InetSocketAddress address = null;
        try {
            List<String> serviceAddresses = NacosUtils.getAllInstance(serviceName);
            if (CollectionUtils.isEmpty(serviceAddresses)) {
                log.error("can't find service: [{}]", serviceName);
                throw new RpcException(ErrorEnum.SERVICE_CAN_NOT_BE_FOUND);
            } else {
//            String targetServiceAddress = loadBalancer.selectServiceAddress(instanceList, rpcRequest);
//            String[] hostAndPort = targetServiceAddress.split(":");
                String targetServiceAddress = serviceAddresses.get(0);
                String[] hostAndPort = targetServiceAddress.split(":");
                String host = hostAndPort[0];
                int port = Integer.parseInt(hostAndPort[1]);
                address = new InetSocketAddress(host, port);
            }
        } catch (NacosException e) {
            log.error("error occured while finding for service: [{}]", serviceName);
        }
        return address;
    }
}
