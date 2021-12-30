package github.yuanlin.transport.netty.codec;

import github.yuanlin.transport.dto.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty 消息编码器
 *
 * @author yuanlin
 * @date 2021/12/28/12:33
 */
public class NettyMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {

    }
}
