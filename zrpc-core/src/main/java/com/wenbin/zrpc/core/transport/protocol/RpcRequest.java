package com.wenbin.zrpc.core.transport.protocol;

import java.io.Serializable;
import lombok.Data;

/**
 * The rpc request
 * @author wenbin
 * Date：2023/4/3
 */
@Data
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = 8215493329459772526L;

    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    private String version = "";
}
