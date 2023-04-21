package com.wenbin.zrpc.core.server;

import com.wenbin.zrpc.core.server.registry.ServiceHolder;
import io.netty.channel.Channel;
import java.util.List;

/**
 * The net server
 * @author wenbin
 * Date：2023/3/29
 */
public interface NetServer {

    /**
     * Start the server
     */
    void start(ServiceHolder serviceHolder);

    void stop();

    /**
     * 获取所有客户端信息
     */
    List<Channel> getAllClients();

    /**
     * 获取所有客户端数量
     */
    int getClientCount();
}
