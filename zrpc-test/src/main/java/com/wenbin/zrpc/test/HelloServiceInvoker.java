package com.wenbin.zrpc.test;

import com.wenbin.zrpc.client.HelloService;
import com.wenbin.zrpc.client.Person;
import com.wenbin.zrpc.core.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * The hello service invoker
 * @author wenbin
 * Date：2023/4/19
 */
@Component
public class HelloServiceInvoker {

    @Reference
    private HelloService helloService;

    public String sayHello() {
        return helloService.hello();
    }

    public String sayHello(String name) {
        return helloService.hello(name);
    }

    public String sayHello(Person person) {
        return helloService.hello(person);
    }
}
