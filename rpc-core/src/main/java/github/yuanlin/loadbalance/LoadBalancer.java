package github.yuanlin.loadbalance;

import github.yuanlin.extension.SPI;
import github.yuanlin.transport.dto.RpcRequest;

import java.util.List;

/**
 * 负载均衡接口
 *
 * @author yuanlin
 * @date 2021/12/28/11:23
 */
@SPI
public interface LoadBalancer {

    String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest);

}
