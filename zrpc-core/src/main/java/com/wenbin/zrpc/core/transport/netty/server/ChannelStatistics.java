package com.wenbin.zrpc.core.transport.netty.server;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

/**
 * The  channel statistics
 * @author wenbin
 * Date：2023/4/10
 */
public class ChannelStatistics extends ChannelInboundHandlerAdapter {

    public ChannelGroup channelGroup;

    public ChannelStatistics(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channelGroup.add(ctx.channel());
        ctx.fireChannelActive();
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.channelGroup.remove(ctx.channel());
        ctx.fireChannelInactive();
    }
}
