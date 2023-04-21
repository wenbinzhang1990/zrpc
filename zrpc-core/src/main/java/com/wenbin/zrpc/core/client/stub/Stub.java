package com.wenbin.zrpc.core.client.stub;

import com.wenbin.zrpc.core.transport.protocol.RpcRequest;

/**
 * The local stub for service
 * @author wenbin
 * Date：2023/4/11
 */
public interface Stub {

    int getRefCount();

    void ref();

    void releaseRef();

    void release();

    Object call(RpcRequest toRpcRequest);


}
