package com.wenbin.zrpc.core.transport.netty.server;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The rpc server handler
 * @author wenbin
 * Date：2023/4/10
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    ThreadPoolExecutor threadPoolExecutor;

    RequestHandler requestHandler;

    public RpcServerHandler(ThreadPoolExecutor threadPoolExecutor, RequestHandler requestHandler) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.requestHandler = requestHandler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {
        logger.trace("Server receive request, requestId:{}", msg.getHeader().getRequestId());
        threadPoolExecutor.execute(() -> {
            try {
                RpcCommand rpcCommand = requestHandler.handle(msg);
                ctx.channel().writeAndFlush(rpcCommand).addListener(future -> {
                    if (!future.isSuccess()) {
                        logger.warn("Server send response failed,request id:{}", rpcCommand.getHeader().getRequestId());
                    }
                });
            } catch (Exception e) {
                logger.error("Server handle request error", e);
            }
        });
    }
}
