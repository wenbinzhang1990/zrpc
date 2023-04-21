package com.wenbin.zrpc.core.client.proxy;


import com.wenbin.zrpc.core.client.DefaultServiceContext;
import com.wenbin.zrpc.core.client.stub.Stub;
import com.wenbin.zrpc.core.transport.protocol.RpcRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The jdk dynamic aop proxy
 * @author wenbin
 * Date：2023/4/11
 */
public class ServiceJdkProxy implements DynamicProxy, InvocationHandler {

    Class<?> clazz;

    DefaultServiceContext defaultServiceContext;

    String version;

    public ServiceJdkProxy(Class<?> clazz, String version, DefaultServiceContext defaultServiceContext) {
        super();
        this.clazz = clazz;
        this.defaultServiceContext = defaultServiceContext;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Stub stub = defaultServiceContext.getBalancedStub(clazz, version);
        if (stub == null) {
            throw new RuntimeException("no service");
        }


        return defaultServiceContext.getBalancedStub(clazz, version).call(toRpcRequest(method, args));
    }

    private RpcRequest toRpcRequest(Method method, Object[] args) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(clazz.getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setVersion(version);
        if (args != null) {
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            rpcRequest.setParameterTypes(parameterTypes);
            rpcRequest.setParameters(args);
        }

        return rpcRequest;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
