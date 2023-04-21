package com.wenbin.zrpc.core.zookeeper;

import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * The zookeeper client
 * @author wenbin
 * Date 2023/4/10
 */
public class CuratorClient {

    private CuratorFramework client;

    public CuratorClient(String connectString, String namespace, int sessionTimeout, int connectionTimeout) {
        client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString).sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout).retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        client.start();
    }

    public CuratorClient(String connectString) {
        this(connectString, ZookeeperConstant.ZK_NAMESPACE, ZookeeperConstant.ZK_SESSION_TIMEOUT, ZookeeperConstant.ZK_CONNECTION_TIMEOUT);
    }


    public String createPathData(String path, byte[] bytes) throws Exception {
        return client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
    }

    public boolean checkExists(String path) throws Exception {
        return client.checkExists().forPath(path) != null;
    }

    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }

    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathChildrenCache.getListenable().addListener(listener);
    }


    public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
        client.getConnectionStateListenable().addListener(connectionStateListener);
    }

    public void close() {
        client.close();
    }
}
