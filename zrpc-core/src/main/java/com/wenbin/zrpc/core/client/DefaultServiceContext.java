package com.wenbin.zrpc.core.client;

import com.wenbin.zrpc.core.client.balance.RpcCallBalance;
import com.wenbin.zrpc.core.client.proxy.ServiceJdkProxy;
import com.wenbin.zrpc.core.client.stub.Stub;
import com.wenbin.zrpc.core.client.stub.StubFactory;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;
import com.wenbin.zrpc.core.zookeeper.ZookeeperPathSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service manager
 * @author wenbin
 * Date：2023/4/11
 */
public class DefaultServiceContext implements ServiceManager, ServiceSubscriber {

    private final ConcurrentHashMap<Long, CompletableFuture<Object>> pendingRpcFuture = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceContext.class);

    // serviceKey -> serverAddress
    private final Map<String, List<String>> serviceAndServerMap = new HashMap<>();

    // serviceKey -> <serverAddress -> stub>
    private final Map<String, Map<String, Stub>> serviceAndServerStubMap = new HashMap<>();

    // serverAddress -> stub
    private final Map<String, Stub> serverAndStubMap = new HashMap<>();

    private final RpcCallBalance rpcCallBalance;


    private final Object lock = new Object();

    private final SerializationTypeEnum serializationTypeEnum;

    StubFactory stubFactory;



    public DefaultServiceContext(RpcCallBalance rpcCallBalance, SerializationTypeEnum serializationTypeEnum) {
        this.rpcCallBalance = rpcCallBalance;
        this.serializationTypeEnum = serializationTypeEnum;
        stubFactory = new StubFactory(serializationTypeEnum);
    }

    public Stub getBalancedStub(Class<?> clazz, String version) {
        Map<String, Stub> stubMap = serviceAndServerStubMap.get(ZookeeperPathSupport.getServiceKey(clazz.getName(), version));
        if (stubMap == null || stubMap.size() == 0) {
            return null;
        }

        return this.rpcCallBalance.balance(
                serviceAndServerStubMap.get(ZookeeperPathSupport.getServiceKey(clazz.getName(), version)).values());
    }

    @Override
    public Object subscribe(Class<?> clazz, String version) {
        String serviceKey = ZookeeperPathSupport.getServiceKey(clazz.getName(), version);
        synchronized (lock) {
            if (!serviceAndServerStubMap.containsKey(serviceKey) && serviceAndServerMap.containsKey(serviceKey)) {
                serviceAndServerStubMap.put(serviceKey, new HashMap<>());
                for (String server : serviceAndServerMap.get(serviceKey)) {
                    try {
                        serviceAndServerStubMap.get(serviceKey).put(server, createOrGetStub(server));
                    } catch (Exception e) {
                        logger.error("crete stub error,{}", server, e);
                    }

                }
            }
        }

        return new ServiceJdkProxy(clazz, version, this).getProxy();
    }

    @Override
    public void addService(String serviceAddress, List<String> serviceKeys) {
        synchronized (lock) {
            if (!serviceAndServerMap.containsKey(serviceAddress)) {
                serviceAndServerMap.put(serviceAddress, new ArrayList<>(serviceKeys));
            }

            serviceAndServerMap.get(serviceAddress).addAll(serviceKeys);
        }
    }

    @Override
    public void removeService(String serviceKey, String serviceAddress) {
        synchronized (lock) {
            serviceAndServerMap.get(serviceKey).remove(serviceAddress);
        }
    }

    @Override
    public void reloadService(Map<String, List<String>> serverAndServiceConfig) {
        synchronized (lock) {
            serviceAndServerMap.clear();
            completeServiceAndServer(serverAndServiceConfig);
            // 未订阅服务时，不会产生调用，无需重新生成stub
            if (serviceAndServerStubMap.size() == 0) {
                return;
            }

            // 更新可用服务列表
            updateServiceAndServerStub();
            // 删除
            notifyToDeleteIfNecessary();
        }
    }

    public SerializationTypeEnum getSerializationTypeEnum() {
        return serializationTypeEnum;
    }

    /**
     * 当发现stub没有引用时，真正释放stub
     */
    private void notifyToDeleteIfNecessary() {
        for (Map.Entry<String, Stub> entry : serverAndStubMap.entrySet()) {
            if (entry.getValue().getRefCount() == 0) {
                entry.getValue().release();
                serverAndStubMap.remove(entry.getKey());
            }
        }
    }

    /**
     * server发生了变化，简单处理可以重新全部生成新的stub，
     * 但是为了性能考虑，剔除无用的stub，给新增的server生成stub，加入到stub列表里面，原先继续生效的stub维持不变
     */
    private void updateServiceAndServerStub() {
        for (Map.Entry<String, Map<String, Stub>> entry : serviceAndServerStubMap.entrySet()) {
            String usedServiceKey = entry.getKey();
            // 无可用服务，剔除服务对应所有的stub
            if (!serviceAndServerMap.containsKey(usedServiceKey)) {
                Map<String, Stub> usedServerStub = entry.getValue();
                usedServerStub.forEach((server, stub) -> stub.releaseRef());
                serviceAndServerStubMap.get(usedServiceKey).clear();
            }

            Map<String, Stub> usedServerStub = entry.getValue();
            List<String> serverListProvided = serviceAndServerMap.get(usedServiceKey);
            // 剔除无用的stub
            for (String server : usedServerStub.keySet()) {
                if (!serverListProvided.contains(server)) {
                    usedServerStub.get(server).releaseRef();
                    usedServerStub.remove(server);
                }
            }

            // 新增的server，生成stub
            for (String server : serverListProvided) {
                if (!usedServerStub.containsKey(server)) {
                    try {
                        // 新增的server，生成stub
                        Stub stub = createOrGetStub(server);
                        stub.ref();
                        usedServerStub.put(server, stub);
                    } catch (Exception e) {
                        logger.error("crete stub error,{}", server, e);
                    }
                }
            }
        }
    }

    private Stub createOrGetStub(String server) throws InterruptedException {
        Stub result = serverAndStubMap.get(server);
        if (result != null) {
            result.ref();
            return result;
        }

        result = createStub(server);
        result.ref();
        serverAndStubMap.put(server, result);
        return result;
    }

    private Stub createStub(String server) throws InterruptedException {
        return stubFactory.createStub(server, this);
    }



    private void completeServiceAndServer(Map<String, List<String>> serverAndServiceConfig) {
        for (Map.Entry<String, List<String>> entry : serverAndServiceConfig.entrySet()) {
            for (String serviceKey : entry.getValue()) {
                if (!serviceAndServerMap.containsKey(serviceKey)) {
                    serviceAndServerMap.put(serviceKey, new ArrayList<>());
                }

                serviceAndServerMap.get(serviceKey).add(entry.getKey());
            }
        }
    }

    public void pending(long requestId, CompletableFuture<Object> future) {
        this.pendingRpcFuture.put(requestId, future);
    }

    public void pendingDone(long requestId) {
        this.pendingRpcFuture.remove(requestId);
    }

    public CompletableFuture<Object> getPending(long requestId) {
        return this.pendingRpcFuture.get(requestId);
    }
}
