package com.wenbin.zrpc.core.transport.netty.server;

import com.wenbin.zrpc.core.server.registry.ServiceHolder;
import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcRequest;
import com.wenbin.zrpc.core.transport.protocol.RpcResponse;
import com.wenbin.zrpc.core.transport.protocol.RpcResponseHeader;
import com.wenbin.zrpc.core.transport.serialize.SerializationSupport;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;
import com.wenbin.zrpc.core.transport.serialize.Serializer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The default request handler
 * @author wenbin
 * Date：2023/4/10
 */
public class DefaultRequestHandler implements RequestHandler {

    private static final int SUCCESS = 0;
    private static final int FAIL = -1;

    private ServiceHolder serviceHolder;

    public DefaultRequestHandler(ServiceHolder serviceHolder) {
        this.serviceHolder = serviceHolder;
    }

    @Override
    public RpcCommand handle(RpcCommand rpcCommand) {
        SerializationTypeEnum serializationTypeEnum = SerializationTypeEnum.getSerializationTypeEnum(
                rpcCommand.getHeader().getSerializationType());
        long requestId = rpcCommand.getHeader().getRequestId();
        Serializer serializer = SerializationSupport.getSerializer(serializationTypeEnum);
        RpcRequest rpcRequest = serializer.deserialize(rpcCommand.getBody(), RpcRequest.class);
        Object serviceBean = serviceHolder.getService(rpcRequest.getClassName(), rpcRequest.getVersion());
        if (serviceBean == null) {
            return new RpcCommand(toFailRpcResponseHeader(requestId, serializationTypeEnum, "service not found"), null);
        }

        return doHandle(serviceBean, rpcRequest, requestId, serializationTypeEnum, serializer);
    }

    private RpcCommand doHandle(Object serviceBean, RpcRequest rpcRequest, long requestId, SerializationTypeEnum serializationTypeEnum,
            Serializer serializer) {
        Object object;
        Class<?> serviceClass = serviceBean.getClass();
        try {
            Method method = serviceClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            method.setAccessible(true);
            object = method.invoke(serviceBean, rpcRequest.getParameters());
        } catch (NoSuchMethodException e) {
            return new RpcCommand(toFailRpcResponseHeader(requestId, serializationTypeEnum, "method not found"), null);
        } catch (IllegalAccessException e) {
            return new RpcCommand(toFailRpcResponseHeader(requestId, serializationTypeEnum, "method not accessible"), null);
        } catch (InvocationTargetException e) {
            return new RpcCommand(toFailRpcResponseHeader(requestId, serializationTypeEnum, "method invoke error"), null);
        }

        return new RpcCommand(toSuccessRpcResponseHeader(requestId, serializationTypeEnum), serializer.serialize(new RpcResponse(object)));
    }

    private RpcResponseHeader toFailRpcResponseHeader(long requestId, SerializationTypeEnum serializationTypeEnum, String errorMsg) {
        return new RpcResponseHeader(DefaultRequestHandler.FAIL, errorMsg, requestId, serializationTypeEnum.getByte());
    }

    private RpcResponseHeader toSuccessRpcResponseHeader(long requestId, SerializationTypeEnum serializationTypeEnum) {
        return new RpcResponseHeader(DefaultRequestHandler.SUCCESS, null, requestId, serializationTypeEnum.getByte());
    }
}