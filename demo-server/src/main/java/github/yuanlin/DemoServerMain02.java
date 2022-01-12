package github.yuanlin;

import github.yuanlin.transport.netty.server.NettyServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 使用 Spring 自动发布服务
 * 要求：
 * 1. spring 配置文件（扫描包路径）
 * 2. HiServiceImpl 上添加 @RpcService 注解
 *
 * @author yuanlin
 * @date 2022/01/12/19:01
 */
public class DemoServerMain02 {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        NettyServer server = applicationContext.getBean(NettyServer.class);
        server.start();
    }
}
