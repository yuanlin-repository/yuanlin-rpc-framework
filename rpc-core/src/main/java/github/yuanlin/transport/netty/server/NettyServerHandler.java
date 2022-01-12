package github.yuanlin.transport.netty.server;

import github.yuanlin.enums.ErrorEnum;
import github.yuanlin.enums.ResponseCodeEnum;
import github.yuanlin.exception.RpcException;
import github.yuanlin.extension.ExtensionLoader;
import github.yuanlin.provider.ServiceProvider;
import github.yuanlin.transport.constants.RpcConstants;
import github.yuanlin.transport.dto.RpcMessage;
import github.yuanlin.transport.dto.RpcRequest;
import github.yuanlin.transport.dto.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Netty 服务端处理器
 *
 * @author yuanlin
 * @date 2021/12/30/17:18
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private ServiceProvider serviceProvider;

    public NettyServerHandler() {
        serviceProvider = ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension("serviceProvider");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof RpcMessage) {
                log.info("receive msg: [{}]", msg);
                RpcMessage rpcMessage = (RpcMessage) msg;
                byte messageType = rpcMessage.getMessageType();
                RpcMessage responseMsg = RpcMessage.builder()
                        .requestId(rpcMessage.getRequestId())
                        .codec(rpcMessage.getCodec())
                        .build();
                // 如果是心跳
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    log.info("heart: [{}]", rpcMessage.getPayload());
                    responseMsg.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    responseMsg.setPayload(RpcConstants.PONG);
                } else {
                    RpcRequest rpcRequest = (RpcRequest) rpcMessage.getPayload();
                    Object result = callService(rpcRequest);
                    log.info("server get result: [{}]", result);
                    responseMsg.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        responseMsg.setPayload(RpcResponse.success(result, rpcRequest.getRequestId()));
                    } else {
                        responseMsg.setPayload(RpcResponse.fail(ResponseCodeEnum.FAIL));
                        log.error("channel is not writable now, message dropped");
                    }
                }
                ctx.writeAndFlush(responseMsg).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    private Object callService(RpcRequest rpcRequest) throws NoSuchMethodException {
        Object result;
        try {
            String serviceName = rpcRequest.getServiceName();
            Object service = serviceProvider.getService(serviceName);
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            result = method.invoke(service, rpcRequest.getParameters());
        } catch (Exception e) {
            throw new RpcException(ErrorEnum.CALL_SERVICE_ERROR, e.getMessage());
        }
        return result;
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
