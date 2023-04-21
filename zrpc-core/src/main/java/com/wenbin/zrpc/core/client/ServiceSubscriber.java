package com.wenbin.zrpc.core.client;

/**
 * The service subscriber
 * @author wenbin
 * Date：2023/4/11
 */
public interface ServiceSubscriber {

    /**
     * Subscribe the service
     */
    Object subscribe(Class<?> clazz, String version);
}
