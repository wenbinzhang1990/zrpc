package com.wenbin.zrpc.core.transport.netty.server;

import com.wenbin.zrpc.core.transport.netty.beat.HeartBeatConst;
import com.wenbin.zrpc.core.transport.netty.beat.IdleChannelHandler;
import com.wenbin.zrpc.core.transport.netty.codec.RequestDecoder;
import com.wenbin.zrpc.core.transport.netty.codec.ResponseEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The initializer for rpc server
 * @author wenbin
 * Date：2023/3/29
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    private ThreadPoolExecutor threadPoolExecutor;

    private RequestHandler requestHandler;

    private ChannelGroup channelGroup;

    public RpcServerInitializer(ThreadPoolExecutor threadPoolExecutor, RequestHandler requestHandler, ChannelGroup channelGroup) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.requestHandler = requestHandler;
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(0, 0, HeartBeatConst.BEAT_TIME_OUT, TimeUnit.SECONDS))
                .addLast(new IdleChannelHandler())
                .addLast(new ChannelStatistics(channelGroup))
                .addLast(new RequestDecoder())
                .addLast(new ResponseEncoder())
                .addLast(new RpcServerHandler(threadPoolExecutor, requestHandler));
    }
}
