package github.yuanlin.transport.netty.codec;

import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.enums.SerializationEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.serialize.Serializer;
import github.yuanlin.transport.constants.RpcConstants;
import github.yuanlin.transport.dto.RpcMessage;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 消息解码器
 *
 * @author yuanlin
 * @date 2021/12/28/12:32
 */
@Slf4j
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * RPC 数据包
     *
     *      4byte       4byte       2byte       1byte       1byte        1byte       4byte
     * +------------+------------+---------+-----------+-------------+-----------+------------+
     * |    魔术位   |  整体长度   |  头长度  |   协议版本  |   消息类型   |  序列化方式 |    请求ID   |
     * +-----------+------------+---------+-----------+-------------+-----------+-------------+
     * |                                     协议头扩展字段                                     |
     * +--------------------------------------------------------------------------------------+
     * |                                                                                      |
     * |                                     payload                                          |
     * |                                                                                      |
     * +--------------------------------------------------------------------------------------+
     *
     * 注: 整体长度 = header长度 + payload长度
     */
    /**
     * LengthFieldBasedFrameDecoder：自定义长度解码器，用来解决粘包黏包问题
     * 构造参数：
     * 1.maxFrameLength: 帧的最大字节数
     * 2.lengthFieldOffset: 帧长度域的偏移量(帧长度域就是指整体长度)  4
     * 3.lengthFieldLength: 帧长度域的字节数，int类型   4
     * 4.lengthAdjustment:  lengthAdjustment = 数据包长度 - 长度域的值 - lengthFieldOffset - lengthFieldLength = -8
     * 5.initialBytesToStrip: 解码过程中，没有丢弃任何数据  0
     */
    public NettyMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, RpcConstants.FULL_LENGTH_FIELD_OFFSET, 4,
                -8, 0);
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object frame = super.decode(ctx, in);
        if (frame instanceof ByteBuf) {
            ByteBuf frameData = (ByteBuf) frame;
            if (frameData.readableBytes() >= RpcConstants.HEAD_LENGTH) {
                try {
                    return decodeFrame(frameData);
                } catch (Exception e) {
                    log.error("decode frame error", e);
                    throw new RpcException(ErrorEnum.DECODE_FRAME_ERROR, e.getMessage());
                }
            }
        }
        return frame;
    }

    private Object decodeFrame(ByteBuf frameData) {
        checkMagicAndVersion(frameData);
        int fullLength = frameData.readInt();
        short headLength = frameData.readShort();
        // 跳过版本字段
        frameData.readerIndex(frameData.readerIndex() + 1);
        byte messageType = frameData.readByte();
        byte codecType = frameData.readByte();
        int requestId = frameData.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(messageType)
                .codec(codecType)
                .requestId(requestId)
                .build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setPayload(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setPayload(RpcConstants.PONG);
            return rpcMessage;
        }
        int payloadLength = fullLength - headLength;
        if (payloadLength > 0) {
            byte[] payload = new byte[payloadLength];
            frameData.readBytes(payload);
            String codecName = SerializationEnum.lookup(codecType);
            log.info("codec name: [{}]", codecName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(codecName);
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest rpcRequest = serializer.deserialize(payload, RpcRequest.class);
                rpcMessage.setPayload(rpcRequest);
            } else {
                RpcResponse rpcResponse = serializer.deserialize(payload, RpcResponse.class);
                rpcMessage.setPayload(rpcResponse);
            }
        }
        return rpcMessage;
    }

    private void checkMagicAndVersion(ByteBuf frameData) {
        // 检验魔数
        int magicNumberLen = RpcConstants.MAGIC_NUMBER.length;
        byte[] magicNumber = new byte[magicNumberLen];
        frameData.readBytes(magicNumber);
        for (int i = 0; i < magicNumber.length; i++) {
            if (magicNumber[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new RpcException(ErrorEnum.UNKNOWN_PROTOCAL);
            }
        }
        int fullLengthOffset = frameData.readerIndex();
        frameData.readerIndex(fullLengthOffset + 4 + 2);
        // 检验版本
        byte version = frameData.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RpcException(ErrorEnum.UNSUPPORTED_PROTOCAL_VERSION);
        }
        // 调整读指针
        frameData.readerIndex(fullLengthOffset);
    }
}
