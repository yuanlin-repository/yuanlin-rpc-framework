package github.yuanlin.transport;

import github.yuanlin.config.ServiceConfig;

/**
 * RPC 服务器接口
 *
 * @author yuanlin
 * @date 2021/12/28/12:47
 */
public interface RpcServer {

    /**
     * 启动服务器
     */
    void start();

    /**
     * 发布服务
     * @param config 服务配置（serviceName, bean）
     * @param <T>
     */
    <T> void publishService(ServiceConfig config);
}
