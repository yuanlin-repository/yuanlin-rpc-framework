package github.yuanlin.loadbalance;

import github.yuanlin.transport.dto.RpcRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 负载均衡抽象类
 *
 * @author yuanlin
 * @date 2022/01/12/14:15
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (CollectionUtils.isEmpty(serviceAddresses)) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return select(serviceAddresses, rpcRequest);
    }

    protected abstract String select(List<String> serviceAddresses, RpcRequest rpcRequest);
}
