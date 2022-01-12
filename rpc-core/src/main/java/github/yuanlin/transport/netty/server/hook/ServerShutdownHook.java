package github.yuanlin.transport.netty.server.hook;

import github.yuanlin.registry.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 服务器关闭前，清理已经注册的服务
 *
 * @author yuanlin
 * @date 2022/01/12/20:34
 */
@Slf4j
public class ServerShutdownHook {

    private static final ServerShutdownHook SHUTDOWN_HOOK = new ServerShutdownHook();

    public static ServerShutdownHook getServerShutdownHook() {
        return SHUTDOWN_HOOK;
    }

    public void clearAllOnClose(InetSocketAddress seriveAddress) {
        log.info("add shutdownHook to clearService");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), seriveAddress);
        }));
    }
}
