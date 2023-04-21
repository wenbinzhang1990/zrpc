package com.wenbin.zrpc.core.server;

import com.wenbin.zrpc.core.client.RpcClient;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * description
 * @author wenbin
 * Date：2023/4/18
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcServiceServer.class, RpcClient.class})
public @interface EnableZRpcService {

}
