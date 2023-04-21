package com.wenbin.zrpc.core.transport.netty.client;

import com.wenbin.zrpc.core.client.DefaultServiceContext;
import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcResponse;
import com.wenbin.zrpc.core.transport.serialize.SerializationSupport;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The rpc client handler
 * @author wenbin
 * Date：2023/4/10
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    private final DefaultServiceContext defaultServiceContext;

    public RpcClientHandler(DefaultServiceContext defaultServiceContext) {
        this.defaultServiceContext = defaultServiceContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {
        logger.trace("receive response from server, requestId: {}", msg.getHeader().getRequestId());
        CompletableFuture<Object> cf = defaultServiceContext.getPending(msg.getHeader().getRequestId());
        if (cf != null) {
            defaultServiceContext.pendingDone(msg.getHeader().getRequestId());
            cf.complete(SerializationSupport.getSerializer(defaultServiceContext.getSerializationTypeEnum())
                    .deserialize(msg.getBody(), RpcResponse.class).getResult());
        } else {
            logger.error("requestId: {} not found", msg.getHeader().getRequestId());
        }
    }
}
