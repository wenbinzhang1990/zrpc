package com.wenbin.zrpc.core.transport.netty.beat;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The idle channel handler
 * Close the channel which is idle over for beat time out seconds
 * @author wenbin
 * Date：2023/3/30
 */
public class IdleChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(IdleChannelHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ((msg instanceof RpcCommand) && ((RpcCommand) msg).getHeader().getRequestId() == HeartBeatConst.BEAT_PING_ID) {
            ReferenceCountUtil.release(msg);
            return;
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            logger.warn("Channel idle in last {} seconds, close it", HeartBeatConst.BEAT_TIME_OUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
