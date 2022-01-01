package github.yuanlin.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理 netty 的 channel
 *
 * @author yuanlin
 * @date 2021/12/28/13:09
 */
@Slf4j
public class NettyChannelProvider {

    /**
     * 用于连接服务器（构造 Netty 客户端时传入）
     */
    private final Bootstrap bootstrap;
    /**
     * key : value => inetSocketAddress : channel
     */
    private final ConcurrentHashMap<String, Channel> channels;

    public NettyChannelProvider(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
        channels = new ConcurrentHashMap<>();
    }

    public Channel getChannel(InetSocketAddress serviceAddress) {
        String address = serviceAddress.toString();
        Channel channel = null;
        if (channels.contains(address)) {
            channels.get(address);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(address);
                channel = doConnect(serviceAddress);
                channels.put(address, channel);
            }
        }
        return channel;
    }

    @SneakyThrows
    private Channel doConnect(InetSocketAddress serviceAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(serviceAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", serviceAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

}
