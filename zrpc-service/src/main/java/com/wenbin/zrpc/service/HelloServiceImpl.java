package com.wenbin.zrpc.service;

import com.wenbin.zrpc.client.HelloService;
import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.core.config.annotation.Service;


/**
 * The service implementation
 * @author wenbin
 * Date：2023/4/18
 */
@Service(value = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public String hello(String name) {
        return "hello:" + name;
    }

    @Override
    public String hello(Person person) {
        return "hello:" + person.toString();
    }
}
