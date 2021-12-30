package github.yuanlin.transport.netty.server;

import github.yuanlin.annotation.RpcService;
import github.yuanlin.config.ServiceConfig;
import github.yuanlin.transport.AbstractRpcServer;
import github.yuanlin.transport.netty.codec.NettyMessageDecoder;
import github.yuanlin.transport.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * RPC 服务器（基于 Netty 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/12:29
 */
@Slf4j
@Component
public class NettyServer extends AbstractRpcServer implements BeanPostProcessor {

    public NettyServer() {}

    public NettyServer(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() {
        // 启动 Netty 服务器
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 默认开启 Nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 开启 Netty 心跳机制
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new NettyMessageEncoder());
                            pipeline.addLast(new NettyMessageDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind(host, port).sync();
            log.debug("netty server started on port {}", port);
            registerService();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("shutdown bossGroup and workerGroup");
        }
    }

    /**
     * 注册服务
     */
    private void registerService() {
        if (CollectionUtils.isNotEmpty(serviceConfigs)) {
            for (ServiceConfig serviceConfig : serviceConfigs) {
                publishService(serviceConfig.getServiceName(), serviceConfig.getService());
            }
        } else {
            log.warn("@RpcService set is empty");
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcServiceAno = bean.getClass().getAnnotation(RpcService.class);
        if (rpcServiceAno != null) {
            ServiceConfig serviceConfig = ServiceConfig.builder()
                    .service(bean)
                    .group(rpcServiceAno.group())
                    .version(rpcServiceAno.version())
                    .build();
            serviceConfigs.add(serviceConfig);
            log.info("find rpc service: [{}]", serviceConfig.getServiceName());
        }
        return bean;
    }

    @Override
    public <T> void publishService(String serviceName, T serviceBean) {
        super.publishService(serviceName, serviceBean);
    }
}