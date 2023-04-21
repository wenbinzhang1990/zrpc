package com.wenbin.zrpc.core.client.balance;

import com.wenbin.zrpc.core.client.stub.Stub;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The round-robin balance
 * @author wenbin
 * Date：2023/4/18
 */
public class RoundRobinBalance implements RpcCallBalance {

    private final static AtomicInteger NEXT_REQUEST_ID = new AtomicInteger(1);

    @Override
    public Stub balance(Collection<Stub> stubs) {
        return (Stub) stubs.toArray()[NEXT_REQUEST_ID.getAndIncrement() % stubs.size()];
    }
}
