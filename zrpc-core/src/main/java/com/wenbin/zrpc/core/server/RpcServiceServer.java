package com.wenbin.zrpc.core.server;

import com.wenbin.zrpc.core.config.annotation.Service;
import com.wenbin.zrpc.core.server.registry.DefaultServiceRegistry;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The server for rpc
 * Start the netty service , scan and load all rpc service to registry
 * @author wenbin
 * Date 2023/3/15
 */

public class RpcServiceServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcServiceServer.class);

    NetServer netServer;

    DefaultServiceRegistry defaultServiceRegistry;


    public RpcServiceServer(@Value("${zrpc.server.port}") int port, @Value("${zrpc.zookeeper.address}") String zookeeperAddress) {
        this.netServer = new NettyServer(findHostIP(), port);
        this.defaultServiceRegistry = new DefaultServiceRegistry(zookeeperAddress, findHostIP() + ":" + port);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> services = applicationContext.getBeansWithAnnotation(Service.class);
        for (Object serviceBean : services.values()) {
            Service service = serviceBean.getClass().getAnnotation(Service.class);
            this.defaultServiceRegistry.addService(service.value(), serviceBean, service.version());
        }
    }

    public void addService(Class<?> clazz, Object serviceBean, String version) {
        this.defaultServiceRegistry.addService(clazz, serviceBean, version);
    }

    @Override
    public void destroy() throws Exception {
        // 先注销再关闭，避免服务不可用
        this.defaultServiceRegistry.unregisterService();
        netServer.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 先启动在注册，避免服务不可用
        netServer.start(this.defaultServiceRegistry);
        this.defaultServiceRegistry.registerService();
    }

    private String findHostIP() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> addresses;
            while (networks.hasMoreElements()) {
                addresses = networks.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (inetAddress instanceof Inet4Address && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取本机IP地址失败", e);
        }
        // 兜底
        String tempIP = "127.0.0.1";
        try {
            tempIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取本机IP地址失败", e);
        }
        return tempIP;
    }
}
