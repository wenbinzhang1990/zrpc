package com.wenbin.zrpc.core.client.balance;

import com.wenbin.zrpc.core.client.stub.Stub;
import java.util.Collection;
import java.util.Random;

/**
 * The random balance
 * @author wenbin
 * Date：2023/4/18
 */
public class RandomBalance implements RpcCallBalance {

    Random random = new Random();

    @Override
    public Stub balance(Collection<Stub> stubs) {
        return (Stub) stubs.toArray()[random.nextInt(100) % stubs.size()];
    }
}
