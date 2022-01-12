package github.yuanlin;

import github.yuanlin.config.ServiceConfig;
import github.yuanlin.proxy.RpcDynamicProxy;
import github.yuanlin.transport.netty.client.NettyClient;

/**
 * 不使用 Spring
 *
 * @author yuanlin
 * @date 2022/01/12/18:46
 */
public class DemoClientMain {

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        ServiceConfig config = ServiceConfig.builder()
                .group("test")
                .version("01")
                .build();
        RpcDynamicProxy proxy = new RpcDynamicProxy(client, config);
        HiService hiService = proxy.getProxy(HiService.class);
        System.out.println(hiService.hi("帅哥~"));
    }
}
