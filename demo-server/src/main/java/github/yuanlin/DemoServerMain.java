package github.yuanlin;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.service.HiServiceImpl;
import github.yuanlin.transport.RpcServer;
import github.yuanlin.transport.netty.server.NettyServer;

/**
 * 不使用 Spring
 *
 * @author yuanlin
 * @date 2022/01/12/18:33
 */
public class DemoServerMain {

    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 8002);
        HiService hiService = new HiServiceImpl();
        ServiceConfig config = ServiceConfig.builder()
                .service(hiService)
                .group("test")
                .version("01")
                .build();
        server.publishService(config);
        server.start();
    }
}
