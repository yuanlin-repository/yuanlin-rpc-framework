package github.yuanlin.transport.netty.codec;

import github.yuanlin.transport.constants.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Netty 消息解码器
 *
 * @author yuanlin
 * @date 2021/12/28/12:32
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
