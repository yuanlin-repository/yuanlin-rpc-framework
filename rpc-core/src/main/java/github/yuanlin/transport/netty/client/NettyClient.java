package github.yuanlin.transport.netty.client;

import github.yuanlin.enums.SerializationEnum;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.factory.SingletonFactory;
import github.yuanlin.registry.ServiceDiscovery;
import github.yuanlin.transport.RpcClient;
import github.yuanlin.transport.constants.RpcConstants;
import github.yuanlin.transport.dto.RpcMessage;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;
import github.yuanlin.transport.netty.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * RPC 客户端（基于 Netty 实现）
 *
 * @author yuanlin
 * @date 2021/12/28/12:44
 */
@Slf4j
public class NettyClient implements RpcClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    /**
     * 服务发现
     */
    private final ServiceDiscovery serviceDiscovery;
    /**
     * 管理 Netty 的 channel
     */
    private final NettyChannelProvider channelProvider;
    /**
     * 未处理的请求（还未被服务器响应）
     */
    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new NettyMessageEncoder());
                        p.addLast(new NettyMessageEncoder());
                        p.addLast(new NettyClientHandler());
                    }
                });
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        channelProvider = SingletonFactory.getInstance(NettyChannelProvider.class, bootstrap);
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse<Object>> sendRequest(RpcRequest rpcRequest) {
        // 调用结果
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 服务发现，寻找服务地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        // 获取到服务地址的连接
        Channel channel = channelProvider.getChannel(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            // 向服务器发送 RPC 请求
            RpcMessage rpcMessage = RpcMessage.builder()
                    .payload(rpcRequest)
                    .codec(SerializationEnum.PROTOSTUFF.getCode())
                    .messageType(RpcConstants.REQUEST_TYPE)
                    .build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("send failed: ", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
