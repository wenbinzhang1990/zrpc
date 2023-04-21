package com.wenbin.zrpc.core.client;

import java.util.List;
import java.util.Map;

/**
 * The service manager
 * @author wenbin
 * Date：2023/4/12
 */
public interface ServiceManager {

    void addService(String serviceAddress, List<String> serviceKeys);

    void removeService(String serviceKey, String serviceAddress);

    void reloadService(Map<String, List<String>> serverAndServiceConfig);
}
