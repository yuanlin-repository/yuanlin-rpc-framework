package github.yuanlin.registry;

import com.alibaba.nacos.api.exception.NacosException;
import github.yuanlin.transport.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 *
 * @author yuanlin
 * @date 2021/12/28/11:24
 */
public interface ServiceDiscovery {

    /**
     * 查找服务地址
     *
     * @param rpcRequest RPC 请求
     * @return 服务地址
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
