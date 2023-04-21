package com.wenbin.zrpc.core.transport.netty.server;

import com.wenbin.zrpc.core.transport.protocol.RpcCommand;
import com.wenbin.zrpc.core.transport.protocol.RpcRequest;

/**
 * The request handler
 * @author wenbin
 * Date：2023/4/10
 */
public interface RequestHandler {

    RpcCommand handle(RpcCommand rpcCommand);
}
