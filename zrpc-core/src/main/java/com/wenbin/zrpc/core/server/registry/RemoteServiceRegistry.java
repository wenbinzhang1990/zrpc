package com.wenbin.zrpc.core.server.registry;

/**
 * The remote service registry
 * @author wenbin
 * Date：2023/4/10
 */
public interface RemoteServiceRegistry {

    void registerService() throws Exception;

    void unregisterService() throws Exception;
}
