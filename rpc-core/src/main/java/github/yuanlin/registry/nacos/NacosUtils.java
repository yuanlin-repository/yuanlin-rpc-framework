package github.yuanlin.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import github.yuanlin.enums.RpcConfigEnum;
import github.yuanlin.enums.RpcErrorEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 操作 Nacos 的工具类
 *
 * @author yuanlin
 * @date 2021/12/28/22:52
 */
@Slf4j
public class NacosUtils {

    /**
     * 操作 Nacos
     */
    private static final NamingService namingService;
    /**
     * 保存注册的服务
     */
    private static final Set<String> serviceNames = new HashSet<>();
    private static String DEFAULT_NACOS_ADDRESS = "127.0.0.1:8848";

    static {
        namingService = getNacosNamingService();
        Properties properties = PropertiesUtils.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getValue());
        if (properties != null) {
            String defaultNacosAddress = properties.getProperty(RpcConfigEnum.NACOS_ADDRESS.getValue());
            if (StringUtils.isNotEmpty(defaultNacosAddress)) {
                DEFAULT_NACOS_ADDRESS = defaultNacosAddress;
            }
        }
    }

    private static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(DEFAULT_NACOS_ADDRESS);
        } catch (NacosException e) {
            log.error("connect to nacos [{}] fail", DEFAULT_NACOS_ADDRESS);
            throw new RpcException(RpcErrorEnum.FAILED_TO_CONNECT_TO_REGISTRY);
        }
    }

    /**
     * 根据服务名称和地址注册服务
     * @param serviceName 服务名称
     * @param address 服务地址
     * @throws NacosException
     */
    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        serviceNames.add(serviceName);
    }

    /**
     * 根绝服务名称获取服务的所有实例
     * @param serviceName 服务名称
     * @return 服务实例集合
     * @throws NacosException
     */
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    /**
     * 根据服务地址清理 Nacos
     * @param address 服务地址
     */
    public static void clearRegistry(InetSocketAddress address) {
        if (!serviceNames.isEmpty()) {
            String host = address.getHostName();
            int port = address.getPort();
            for (String serviceName : serviceNames) {
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    log.error("clear registry for service [{}] fail", serviceName, e);
                }
            }
            log.info("All registered services on the server are cleared: [{}]", serviceNames);
        }
    }
}
