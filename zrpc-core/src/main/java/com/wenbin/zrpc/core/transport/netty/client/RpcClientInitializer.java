package com.wenbin.zrpc.core.transport.netty.client;

import com.wenbin.zrpc.core.client.DefaultServiceContext;
import com.wenbin.zrpc.core.transport.netty.beat.BeatChannelHandler;
import com.wenbin.zrpc.core.transport.netty.beat.HeartBeatConst;
import com.wenbin.zrpc.core.transport.netty.codec.RequestEncoder;
import com.wenbin.zrpc.core.transport.netty.codec.ResponseDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

/**
 * The initializer for rpc server
 * @author wenbin
 * Date：2023/3/29
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    private final DefaultServiceContext defaultServiceContext;

    public RpcClientInitializer(DefaultServiceContext defaultServiceContext) {
        this.defaultServiceContext = defaultServiceContext;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(0, 0, HeartBeatConst.BEAT_TIME_OUT, TimeUnit.SECONDS))
                .addLast(new BeatChannelHandler()).addLast(new RequestEncoder()).addLast(new ResponseDecoder())
                .addLast(new RpcClientHandler(defaultServiceContext));
    }
}
