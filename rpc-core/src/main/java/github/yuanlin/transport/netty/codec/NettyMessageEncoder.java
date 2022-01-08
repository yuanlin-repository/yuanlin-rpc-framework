package github.yuanlin.transport.netty.codec;

import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.enums.SerializationEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.serialize.Serializer;
import github.yuanlin.transport.constants.RpcConstants;
import github.yuanlin.transport.dto.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.Rpc;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty 消息编码器
 *
 * @author yuanlin
 * @date 2021/12/28/12:33
 */
@Slf4j
public class NettyMessageEncoder extends MessageToByteEncoder<RpcMessage> {

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
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) {
        try {
            // 魔数
            byteBuf.writeBytes(RpcConstants.MAGIC_NUMBER);
            // 空出整体长度字段
            // 整体长度的偏移量
            int lengthOffset = byteBuf.writerIndex();
            byteBuf.writerIndex(lengthOffset + RpcConstants.FULL_LENGTH_FIELD_LENGTH);
            // 头长度
            byteBuf.writeShort(RpcConstants.HEAD_LENGTH);
            // 协议版本
            byteBuf.writeByte(RpcConstants.VERSION);
            // 消息类型
            byte messageType = rpcMessage.getMessageType();
            byteBuf.writeByte(messageType);
            // 序列化方式
            byteBuf.writeByte(rpcMessage.getCodec());
            // 请求ID
            byteBuf.writeInt(rpcMessage.getRequestId());
            byte[] data = null;
            // 心跳请求和响应交给 NettyClientHandler 和 NettyServerHandler 处理
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // 序列化
                String codecName = SerializationEnum.lookup(rpcMessage.getCodec());
                log.info("codec name: [{}]", codecName);
                Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                        .getExtension(codecName);
                Object payload = rpcMessage.getPayload();
                data = serializer.serialize(payload);
            }
            // 写 payload
            if (data != null) {
                byteBuf.writeBytes(data);
            }
            // 记录末尾下标，写完整体长度后恢复
            int tailWriterIndex = byteBuf.writerIndex();
            int fullLength = (tailWriterIndex - lengthOffset) + RpcConstants.MAGIC_NUMBER.length;
            // 调正写指针下标，写整体长度
            byteBuf.writerIndex(lengthOffset).writeInt(fullLength);
            // 恢复写指针到末尾
            byteBuf.writerIndex(tailWriterIndex);
        } catch (Exception e) {
            log.error("encode request error", e);
            throw new RpcException(ErrorEnum.ENCODE_FRAME_ERROR, e.getMessage());
        }

    }
}
