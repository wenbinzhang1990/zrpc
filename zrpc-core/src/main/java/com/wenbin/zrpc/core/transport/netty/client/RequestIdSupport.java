package com.wenbin.zrpc.core.transport.netty.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The request id support
 * @author wenbin
 * Date：2023/4/17
 */
public class RequestIdSupport {

    private final static AtomicInteger NEXT_REQUEST_ID = new AtomicInteger(1);

    public static int next() {
        return NEXT_REQUEST_ID.getAndIncrement();
    }
}
