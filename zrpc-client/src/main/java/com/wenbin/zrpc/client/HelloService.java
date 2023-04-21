package com.wenbin.zrpc.client;


/**
 * The hello service
 * @author wenbin
 * Date：2023/4/18
 */
public interface HelloService {

    String hello();

    String hello(String name);

    String hello(Person person);

}
