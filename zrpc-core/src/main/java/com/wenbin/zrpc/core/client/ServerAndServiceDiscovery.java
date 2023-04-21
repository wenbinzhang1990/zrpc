package com.wenbin.zrpc.core.client;

import java.util.List;
import java.util.Map;

/**
 * The service discovery
 * @author wenbin
 * Date：2023/4/11
 */
public interface ServerAndServiceDiscovery {


    void addServer(String serviceAddress, List<String> serviceKeys);

    void removeServer(String serviceAddress);


    void reloadServer() throws Exception;

    void start();

    void stop() throws Exception;

}
