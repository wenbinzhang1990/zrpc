package com.wenbin.zrpc.core.server;

import com.wenbin.zrpc.core.server.registry.ServiceHolder;
import com.wenbin.zrpc.core.transport.DefaultThreadPoolExecutor;
import com.wenbin.zrpc.core.transport.netty.server.DefaultRequestHandler;
import com.wenbin.zrpc.core.transport.netty.server.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A server implemented by netty
 * @author wenbin
 * Date 2023/3/15
 */
public class NettyServer implements NetServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    // only one thread to accept client
    NioEventLoopGroup boss = new NioEventLoopGroup(1);
    EventLoopGroup worker = new NioEventLoopGroup();

    String host;

    int port;

    ChannelGroup channelGroup;

    private final ThreadPoolExecutor threadPoolExecutor;

    public NettyServer(String host, int port) {
        this(host, port, DefaultThreadPoolExecutor.getInstance());
    }

    public NettyServer(String host, int port, ThreadPoolExecutor threadPoolExecutor) {
        this.host = host;
        this.port = port;
        this.channelGroup = new DefaultChannelGroup(new DefaultEventExecutor());
        this.threadPoolExecutor = threadPoolExecutor;
    }



    @Override
    public void start(ServiceHolder serviceHolder) {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new RpcServerInitializer(this.threadPoolExecutor, new DefaultRequestHandler(serviceHolder), channelGroup))
                    .bind(host, port)
                    .sync();
            logger.info("Netty server start success");
        } catch (Exception e) {
            logger.error("Netty server start error", e);
            if (boss != null) {
                boss.shutdownGracefully();
            }

            if (worker != null) {
                worker.shutdownGracefully();
            }
        }
    }

    @Override
    public void stop() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        logger.info("Netty server stop success");
    }

    @Override
    public List<Channel> getAllClients() {
        return Arrays.asList(channelGroup.toArray(new Channel[0]));
    }

    @Override
    public int getClientCount() {
        return channelGroup.size();
    }
}
