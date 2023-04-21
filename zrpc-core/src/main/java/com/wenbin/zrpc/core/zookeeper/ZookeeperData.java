package com.wenbin.zrpc.core.zookeeper;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * The zookeeper data
 * @author wenbin
 * Date：2023/4/12
 */
@AllArgsConstructor
@Getter
@Setter
public class ZookeeperData {

    private List<String> serviceKeys;

    private String serviceAddress;
}
