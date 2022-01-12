package github.yuanlin.loadbalance.loadbalancer;

import github.yuanlin.loadbalance.AbstractLoadBalancer;
import github.yuanlin.loadbalance.LoadBalancer;
import github.yuanlin.transport.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author yuanlin
 * @date 2021/12/28/12:19
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    @Override
    protected String select(List<String> serviceAddresses, RpcRequest rpcRequest) {
        return serviceAddresses.get(new Random().nextInt(serviceAddresses.size()));
    }
}
