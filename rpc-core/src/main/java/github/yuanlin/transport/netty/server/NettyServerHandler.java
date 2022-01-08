package github.yuanlin.transport.netty.server;

import github.yuanlin.transport.dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 服务端处理器
 *
 * @author yuanlin
 * @date 2021/12/30/17:18
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            log.info("receive msg: [{}]", msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("server catch exception：", cause);
        ctx.close();
    }
}
