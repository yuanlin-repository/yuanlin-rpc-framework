package github.yuanlin.registry.zk;

import github.yuanlin.registry.ServiceRegistry;
import github.yuanlin.registry.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * 服务注册（基于 ZooKeeper 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:01
 */
public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress serviceAddress) {
        String servicePath = CuratorUtils.ZK_REGISTERED_ROOT_PATH + "/" + serviceName + serviceAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
