package com.wenbin.zrpc.core.transport.protocol;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The rpc response
 * @author wenbin
 * Date：2023/4/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {

    private Object result;
}
