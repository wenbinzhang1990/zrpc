package com.wenbin.zrpc.core.zookeeper;

/**
 * The zookeeper path support
 * @author wenbin
 * Date：2023/4/12
 */
public class ZookeeperPathSupport {

    public static String getServiceKey(String serviceName, String version) {
        return serviceName + "_" + version;
    }

    public static String getAllServicePath() {
        return ZookeeperConstant.ZK_DATA_PATH;
    }

    public static String getServicePath(String key) {
        return ZookeeperConstant.ZK_DATA_PATH + "/" + key;
    }
}
