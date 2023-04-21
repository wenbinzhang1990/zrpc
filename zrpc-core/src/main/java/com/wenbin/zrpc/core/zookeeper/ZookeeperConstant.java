package com.wenbin.zrpc.core.zookeeper;

/**
 * The zookeeper constant
 * @author wenbin
 * @date 2023/4/10
 */
public interface ZookeeperConstant {

    int ZK_SESSION_TIMEOUT = 5000;
    int ZK_CONNECTION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    String ZK_NAMESPACE = "zrpc";
}
