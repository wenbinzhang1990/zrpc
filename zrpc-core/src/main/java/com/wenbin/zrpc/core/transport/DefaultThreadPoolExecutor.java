package com.wenbin.zrpc.core.transport;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wenbin
 * Date：2023/3/31
 * @Description
 */
public class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

    private DefaultThreadPoolExecutor() {
        super(Runtime.getRuntime().availableProcessors() * 2, Runtime.getRuntime().availableProcessors() * 4, 30L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), r -> new Thread(r, "zrpc-" + r.hashCode()), new AbortPolicy());
    }

    public static DefaultThreadPoolExecutor getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        /**
         * Singleton
         */
        INSTANCE;

        Singleton() {
            executor = new DefaultThreadPoolExecutor();
        }

        private final DefaultThreadPoolExecutor executor;

        public DefaultThreadPoolExecutor getInstance() {
            return this.executor;
        }
    }

}
