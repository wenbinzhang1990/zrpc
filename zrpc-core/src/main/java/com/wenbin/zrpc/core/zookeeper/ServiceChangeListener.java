package com.wenbin.zrpc.core.zookeeper;

import com.wenbin.zrpc.core.client.ServerAndServiceDiscovery;
import com.wenbin.zrpc.core.transport.serialize.Serializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listen the service change
 * @author wenbin
 * Date：2023/4/13
 */
public class ServiceChangeListener implements PathChildrenCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceChangeListener.class);

    ServerAndServiceDiscovery serverAndServiceDiscovery;
    Serializer serializer;

    public ServiceChangeListener(ServerAndServiceDiscovery serverAndServiceDiscovery, Serializer serializer) {
        this.serverAndServiceDiscovery = serverAndServiceDiscovery;
        this.serializer = serializer;
    }

    @Override
    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
        PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
        ZookeeperData data = null;
        switch (type) {
            // 仅需要处理以下3种情况
            // 1. 重连，重新加载服务列表
            // 2. 新增服务
            // 3. 删除服务
            // 服务一旦上下不会更新，限流熔断是另外一回事
            // 初始化已经在前置逻辑种处理
            // 丢失连接，重连，走到1走重连逻辑
            case CONNECTION_RECONNECTED:
                logger.info("Reconnected to zk, reload service list");
                serverAndServiceDiscovery.reloadServer();
                break;
            case CHILD_ADDED:
                data = toZookeeperData(pathChildrenCacheEvent.getData());
                serverAndServiceDiscovery.addServer(data.getServiceAddress(), data.getServiceKeys());
                break;
            case CHILD_REMOVED:
                data = toZookeeperData(pathChildrenCacheEvent.getData());
                serverAndServiceDiscovery.removeServer(data.getServiceAddress());
                break;
            default:
                break;
        }
    }

    private ZookeeperData toZookeeperData(ChildData childData) {
        return serializer.deserialize(childData.getData(), ZookeeperData.class);
    }
}
