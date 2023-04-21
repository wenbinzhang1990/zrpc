package com.wenbin.zrpc.core.client.stub;

import com.wenbin.zrpc.core.client.DefaultServiceContext;
import com.wenbin.zrpc.core.transport.netty.client.RequestIdSupport;
import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcHeader;
import com.wenbin.zrpc.core.transport.protocol.RpcRequest;
import com.wenbin.zrpc.core.transport.serialize.SerializationSupport;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service stub
 * @author wenbin
 * Date：2023/4/14
 */
public class ServiceStub implements Stub {

    private static final Logger logger = LoggerFactory.getLogger(ServiceStub.class);

    AtomicInteger refCount = new AtomicInteger(0);

    private final Channel channel;

    private final DefaultServiceContext defaultServiceContext;

    SerializationTypeEnum serializationTypeEnum;

    public ServiceStub(Channel channel, DefaultServiceContext defaultServiceContext, SerializationTypeEnum serializationTypeEnum) {
        this.channel = channel;
        this.defaultServiceContext = defaultServiceContext;
        this.serializationTypeEnum = serializationTypeEnum;
    }


    @Override
    public int getRefCount() {
        return refCount.get();
    }

    @Override
    public void ref() {
        refCount.incrementAndGet();
    }

    @Override
    public void releaseRef() {
        refCount.decrementAndGet();
    }

    @Override
    public void release() {
        refCount.getAndSet(0);
        channel.close();
    }

    @Override
    public Object call(RpcRequest toRpcRequest) {
        CompletableFuture<Object> cf = new CompletableFuture<>();
        try {
            RpcCommand rpcCommand = new RpcCommand(new RpcHeader(RequestIdSupport.next(), serializationTypeEnum.getByte()),
                    SerializationSupport.getSerializer(serializationTypeEnum).serialize(toRpcRequest));
            ChannelFuture channelFuture = channel.writeAndFlush(rpcCommand).sync();
            if (!channelFuture.isSuccess()) {
                logger.error("call error,request:{}", toRpcRequest.toString());
                throw new RuntimeException("call error,request:" + rpcCommand.getHeader().getRequestId());
            }
            defaultServiceContext.pending(rpcCommand.getHeader().getRequestId(), cf);
        } catch (InterruptedException e) {
            logger.error("call error,request:{}", toRpcRequest.toString(), e);
            throw new RuntimeException(e);
        }

        try {
            return cf.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
