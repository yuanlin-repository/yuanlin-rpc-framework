package github.yuanlin.transport;

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
     * @param serviceName 服务名称
     * @param serviceBean 处理请求的实例 bean
     * @param <T> 实例类型
     */
    <T> void publishService(String serviceName, T serviceBean);
}
