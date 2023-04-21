package com.wenbin.zrpc.core.server.registry;


import com.wenbin.zrpc.core.transport.serialize.ProtostuffSerializer;
import com.wenbin.zrpc.core.transport.serialize.Serializer;
import com.wenbin.zrpc.core.zookeeper.CuratorClient;
import com.wenbin.zrpc.core.zookeeper.ZookeeperData;
import com.wenbin.zrpc.core.zookeeper.ZookeeperPathSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default service registry
 * @author wenbin
 * Date：2023/4/10
 */
public class DefaultServiceRegistry implements ServiceHolder, RemoteServiceRegistry {

    Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    Map<String, Object> serviceMap = new HashMap<>();

    CuratorClient curatorClient;

    private final String serviceAddress;

    private final List<String> paths = new ArrayList<>();

    Serializer serializer = new ProtostuffSerializer();

    public DefaultServiceRegistry(String zookeeperAddress, String serviceAddress) {
        this.curatorClient = new CuratorClient(zookeeperAddress);
        this.serviceAddress = serviceAddress;
    }


    /**
     * 无法批量操作，为了避免应用发布是多次调用zookeeper，所以将整体服务打包存到1个节点中
     * 带来的问题是client无法根据serviceKey获取到对应的服务列表，只能获取所有zookeeper中注册的服务，再选择引用的服务
     */
    @Override
    public void registerService() throws Exception {
        if (serviceMap.keySet().size() == 0) {
            return;
        }

        List<String> serviceKeys = new ArrayList<>(serviceMap.keySet());
        ZookeeperData zookeeperData = new ZookeeperData(serviceKeys, serviceAddress);
        String path = ZookeeperPathSupport.getServicePath(serviceAddress);
        curatorClient.createPathData(path, serializer.serialize(zookeeperData));
        paths.add(path);
        curatorClient.addConnectionStateListener((curatorFramework, connectionState) -> {
            if (connectionState == ConnectionState.RECONNECTED) {
                logger.info("Connection state: {}, register service after reconnected", connectionState);
                try {
                    reconnectAndRegisterService();
                } catch (Exception e) {
                    logger.error("register service error for reconnect", e);
                }
            }
        });
    }

    public void reconnectAndRegisterService() throws Exception {
        if (serviceMap.keySet().size() == 0) {
            return;
        }

        List<String> serviceKeys = new ArrayList<>(serviceMap.keySet());
        ZookeeperData zookeeperData = new ZookeeperData(serviceKeys, serviceAddress);
        String path = ZookeeperPathSupport.getServicePath(serviceAddress);
        if (!curatorClient.checkExists(path)) {
            // 程序运行期间不会因为重连而更新数据，所以只要查到没有重新创建即可，存在了就忽略
            curatorClient.createPathData(path, serializer.serialize(zookeeperData));
        }
    }


    @Override

    public void unregisterService() throws Exception {
        for (String path : paths) {
            curatorClient.deletePath(path);
        }
    }

    @Override
    public void addService(Class<?> clazz, Object service, String version) {
        this.addService(clazz.getName(), service, version);
    }

    @Override
    public void addService(String serviceName, Object service, String version) {
        serviceMap.put(ZookeeperPathSupport.getServiceKey(serviceName, version), service);
    }

    @Override
    public Object getService(String serviceName, String version) {
        return serviceMap.get(ZookeeperPathSupport.getServiceKey(serviceName, version));
    }

    @Override
    public void removeService(String serviceName, String version) {
        serviceMap.remove(ZookeeperPathSupport.getServiceKey(serviceName, version));
    }
}
