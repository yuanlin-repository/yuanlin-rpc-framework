package github.yuanlin.registry.zk;

import github.yuanlin.enums.RpcErrorEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.loadbalance.LoadBalancer;
import github.yuanlin.registry.ServiceDiscovery;
import github.yuanlin.transport.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:01
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    // private final LoadBalancer loadBalancer;

    public ZkServiceDiscovery() {
        // this.loadBalancer = ExtensionLoader.getExtensionLoader(LoadBalancer.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceAddresses = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        if (CollectionUtils.isEmpty(serviceAddresses)) {
            throw new RpcException(RpcErrorEnum.SERVICE_CAN_NOT_BE_FOUND, serviceName);
        }
//        String targetServiceAddress = loadBalancer.selectServiceAddress(serviceAddresses, rpcRequest);
//        log.info("find service [{}] address successfully", targetServiceAddress);
        String targetServiceAddress = serviceAddresses.get(0);
        String[] hostAndPort = targetServiceAddress.split(":");
        String host = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        return new InetSocketAddress(host, port);
    }
}
