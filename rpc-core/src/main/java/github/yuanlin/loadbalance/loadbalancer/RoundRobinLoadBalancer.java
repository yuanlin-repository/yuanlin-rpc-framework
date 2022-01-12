package github.yuanlin.loadbalance.loadbalancer;

import github.yuanlin.loadbalance.AbstractLoadBalancer;
import github.yuanlin.loadbalance.LoadBalancer;
import github.yuanlin.transport.dto.RpcRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobin (轮询)负载均衡
 *
 * @author yuanlin
 * @date 2021/12/28/12:18
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalancer {

    private final AtomicInteger nextServerCyclicCounter = new AtomicInteger(-1);

    @Override
    protected String select(List<String> serviceAddresses, RpcRequest rpcRequest) {
        int nextIdx = incrementAndGetIdx(serviceAddresses.size());
        return serviceAddresses.get(nextIdx);
    }

    private int incrementAndGetIdx(int size) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % size;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }

    }
}
