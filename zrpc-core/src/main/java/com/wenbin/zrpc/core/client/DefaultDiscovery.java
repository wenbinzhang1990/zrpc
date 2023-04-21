package com.wenbin.zrpc.core.client;

import com.wenbin.zrpc.core.transport.serialize.ProtostuffSerializer;
import com.wenbin.zrpc.core.transport.serialize.Serializer;
import com.wenbin.zrpc.core.zookeeper.CuratorClient;
import com.wenbin.zrpc.core.zookeeper.ServiceChangeListener;
import com.wenbin.zrpc.core.zookeeper.ZookeeperData;
import com.wenbin.zrpc.core.zookeeper.ZookeeperPathSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description
 * @author wenbin
 * Date：2023/4/12
 */
public class DefaultDiscovery implements ServerAndServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDiscovery.class);

    private Map<String, List<String>> serverAndServiceMap = new HashMap<>();

    CuratorClient curatorClient;

    DefaultServiceContext serviceManager;

    Serializer serializer = new ProtostuffSerializer();

    public DefaultDiscovery(String configServerAddress, DefaultServiceContext serviceManager) {
        curatorClient = new CuratorClient(configServerAddress);
        this.serviceManager = serviceManager;
    }

    private void discovery() {
        try {
            reloadServer();
            curatorClient.watchPathChildrenNode(ZookeeperPathSupport.getAllServicePath(), new ServiceChangeListener(this, serializer));
        } catch (Exception e) {
            logger.error("service discovery error", e);
        }
    }


    @Override
    public void addServer(String serviceAddress, List<String> serviceKeys) {
        serverAndServiceMap.put(serviceAddress, serviceKeys);
        serviceManager.addService(serviceAddress, serviceKeys);
    }

    @Override
    public void removeServer(String serviceAddress) {

        List<String> serviceKeys = serverAndServiceMap.remove(serviceAddress);
        if (serviceKeys == null) {
            return;
        }

        for (String serviceKey : serviceKeys) {
            serviceManager.removeService(serviceKey, serviceAddress);
        }
    }

    @Override
    public void reloadServer() throws Exception {
        serverAndServiceMap.clear();
        List<String> nodes = curatorClient.getChildren(ZookeeperPathSupport.getAllServicePath());
        for (String node : nodes) {
            String path = ZookeeperPathSupport.getServicePath(node);
            ZookeeperData zookeeperData = serializer.deserialize(curatorClient.getData(path), ZookeeperData.class);
            serverAndServiceMap.put(zookeeperData.getServiceAddress(), zookeeperData.getServiceKeys());
        }

        serviceManager.reloadService(new HashMap<>(serverAndServiceMap));
    }

    @Override
    public void start() {
        discovery();
    }

    @Override
    public void stop() throws Exception {
        curatorClient.close();
    }
}