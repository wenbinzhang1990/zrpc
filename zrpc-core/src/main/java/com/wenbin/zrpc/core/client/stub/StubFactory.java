package com.wenbin.zrpc.core.client.stub;



import com.wenbin.zrpc.core.client.DefaultServiceContext;
import com.wenbin.zrpc.core.transport.netty.client.RpcClientInitializer;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The stub factory
 * @author wenbin
 * Date：2023/4/14
 */
public class StubFactory {

    private static final Logger logger = LoggerFactory.getLogger(StubFactory.class);

    SerializationTypeEnum serializationTypeEnum;

    public StubFactory(SerializationTypeEnum serializationTypeEnum) {
        this.serializationTypeEnum = serializationTypeEnum;
    }

    public Stub createStub(String serverAddress, DefaultServiceContext defaultServiceContext) throws InterruptedException {
        String[] address = serverAddress.split(":");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 4);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer(defaultServiceContext)).connect(address[0], Integer.parseInt(address[1]));
        cf.addListener(future -> {
            logger.info((future.isSuccess() ? "连接成功" : "连接失败") + ",Ip:" + address[0] + ",Port:" + address[1] + "");
        });
        cf.sync();

        return new ServiceStub(cf.channel(), defaultServiceContext, serializationTypeEnum);
    }
}
