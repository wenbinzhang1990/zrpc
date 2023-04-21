package com.wenbin.zrpc.core.client;

import com.wenbin.zrpc.core.client.balance.RoundRobinBalance;
import com.wenbin.zrpc.core.config.annotation.Reference;
import com.wenbin.zrpc.core.transport.serialize.SerializationTypeEnum;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The rpc client
 * @author wenbin
 * Date：2023/4/11
 */
public class RpcClient implements ApplicationContextAware, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    DefaultServiceContext defaultServiceContext;

    ServerAndServiceDiscovery serverAndServiceDiscovery;

    public RpcClient(@Value("${zrpc.zookeeper.address}") String zookeeperAddress) {
        defaultServiceContext = new DefaultServiceContext(new RoundRobinBalance(), SerializationTypeEnum.PROTOSTUFF);
        serverAndServiceDiscovery = new DefaultDiscovery(zookeeperAddress, defaultServiceContext);
        serverAndServiceDiscovery.start();
    }

    @Override
    public void destroy() throws Exception {
        serverAndServiceDiscovery.stop();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.equalsIgnoreCase(this.getClass().getName())) {
                continue;
            }

            Object bean = applicationContext.getBean(beanName);
            Field[] fields = bean.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    Reference reference = field.getAnnotation(Reference.class);
                    if (reference != null) {
                        field.setAccessible(true);
                        field.set(bean, defaultServiceContext.subscribe(field.getType(), reference.version()));
                    }
                }
            } catch (Exception e) {
                logger.error("autowire the reference error", e);
            }
        }
    }
}
