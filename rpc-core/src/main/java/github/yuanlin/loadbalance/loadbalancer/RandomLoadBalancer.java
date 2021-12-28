package github.yuanlin.loadbalance.loadbalancer;

import github.yuanlin.loadbalance.LoadBalancer;
import github.yuanlin.transport.dto.RpcRequest;

import java.util.List;

/**
 * 随机负载均衡
 *
 * @author yuanlin
 * @date 2021/12/28/12:19
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        return null;
    }
}
