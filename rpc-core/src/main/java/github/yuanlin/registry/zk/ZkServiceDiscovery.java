package github.yuanlin.registry.zk;

import github.yuanlin.registry.ServiceDiscovery;
import github.yuanlin.transport.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:01
 */
public class ZkServiceDiscovery implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        return null;
    }
}
