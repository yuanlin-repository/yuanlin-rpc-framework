package github.yuanlin.registry.zk;

import github.yuanlin.enums.RpcConfigEnum;
import github.yuanlin.transport.constants.RpcConstants;
import github.yuanlin.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 操作 ZooKeeper 的工具类（基于 curator 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/14:54
 */
@Slf4j
public class CuratorUtils {

    /**
     * 重试间隔时间
     */
    private static final int BASE_SLEEP_TIME = 1000;
    /**
     * 重试次数
     */
    private static final int MAX_RETRIES = 3;
    /**
     * 注册根路径
     */
    public static String ZK_REGISTERED_ROOT_PATH = "/yuanlin-registered";
    /**
     * zookeeper 默认启动地址
     */
    private static String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    /**
     * 缓存一个服务的所有可用地址
     * key : value => 服务名称 : 服务地址列表
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    /**
     * 缓存已经注册过的路径
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    /**
     * zookeeper 客户端
     */
    private static CuratorFramework zkClient;

    static {
        Properties properties = PropertiesUtils.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getValue());
        if (properties != null) {
            String zookeeperRegisteredRootPath = properties.getProperty(RpcConfigEnum.ZK_REGISTERED_ROOT_PATH.getValue());
            if (StringUtils.isNotEmpty(zookeeperRegisteredRootPath)) {
                ZK_REGISTERED_ROOT_PATH = zookeeperRegisteredRootPath;
            }
            String defaultZooKeeperAddress = properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getValue());
            if (StringUtils.isNotEmpty(defaultZooKeeperAddress)) {
                DEFAULT_ZOOKEEPER_ADDRESS = defaultZooKeeperAddress;
            }
        }
    }

    /**
     * 指定路径创建持久节点
     * @param zkClient
     * @param path 路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node exists. The node is: [{}]", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is: [{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            log.error("create persistent node for path [{}] failed", path);
        }
    }

    /**
     * 获取 zookeeper 一个节点的所有子节点（根据服务名称获取一个服务的所有可用地址）
     * @param zkClient
     * @param serviceName 服务名称
     * @return
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String serviceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(serviceName)) {
            return SERVICE_ADDRESS_MAP.get(serviceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTERED_ROOT_PATH + "/" + serviceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, result);
            registerWatcher(serviceName, zkClient);
        } catch (Exception e) {
            log.error("get children nodes for path [{}] failed", servicePath);
        }
        return result;
    }

    /**
     * 注册监听器（如果服务节点发生变化，比如服务节点宕机，新增节点，同步变化后的服务节点列表）
     */
    private static void registerWatcher(String serviceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTERED_ROOT_PATH + "/" + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = ((curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(serviceName, serviceAddresses);
        });
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    /**
     * 获取 zookeeper 客户端
     */
    public static CuratorFramework getZkClient() {
        // 检查用户是否配置了 ZooKeeper 地址
//        Properties properties = PropertiesUtils.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getValue());
//        String zookeeperAddress = "";
//        if (properties != null && StringUtils.isNotEmpty(properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getValue()))) {
//            zookeeperAddress = properties.getProperty(RpcConfigEnum.ZK_ADDRESS.getValue());
//        } else {
//            zookeeperAddress = DEFAULT_ZOOKEEPER_ADDRESS;
//        }
        String zookeeperAddress = DEFAULT_ZOOKEEPER_ADDRESS;
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        // 创建 zkClient
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // 等待 zkClient 连接成功，失败则抛出异常
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out when connect to ZK!");
            }
        } catch (Exception e) {}
        log.debug("connect to zookeeper: {}", zookeeperAddress);
        return zkClient;
    }

    /**
     * 清除特定地址的节点
     * @param zkClient
     * @param inetSocketAddress 地址
     */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        for (String path : REGISTERED_PATH_SET) {
            try {
                if (path.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(path);
                }
            } catch (Exception e) {
                log.error("clear registry for path [{}] fail", path);
            }
            log.info("All registered services on the server are cleared: [{}]", REGISTERED_PATH_SET);
        }
    }
}
