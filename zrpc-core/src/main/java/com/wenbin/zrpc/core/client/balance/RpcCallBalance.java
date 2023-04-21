package com.wenbin.zrpc.core.client.balance;

import com.wenbin.zrpc.core.client.stub.Stub;
import java.util.Collection;

/**
 * The rpc call balance
 * @author wenbin
 * Date：2023/4/12
 */
public interface RpcCallBalance {

    Stub balance(Collection<Stub> stubs);
}
