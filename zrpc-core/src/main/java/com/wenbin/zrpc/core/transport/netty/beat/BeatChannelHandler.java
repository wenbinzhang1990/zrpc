package com.wenbin.zrpc.core.transport.netty.beat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description
 * @author wenbin
 * Date：2023/4/14
 */
public class BeatChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(IdleChannelHandler.class);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().writeAndFlush(HeartBeatConst.BEAT_PING).addListener(future -> {
                if (!future.isSuccess()) {
                    logger.warn("Client send beat ping failed");
                }
            });
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
