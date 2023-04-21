package com.wenbin.zrpc.core.server.registry;

/**
 * The service holder
 * @author wenbin
 * Date：2023/3/29
 */
public interface ServiceHolder {

    /**
     * 增加服务
     */
    void addService(Class<?> clazz, Object service, String version);

    /**
     * 增加服务
     */
    void addService(String serviceName, Object service, String version);

    /**
     * 获取服务
     */
    Object getService(String serviceName, String version);

    /**
     * 取消服务
     */
    void removeService(String serviceName, String version);

}
