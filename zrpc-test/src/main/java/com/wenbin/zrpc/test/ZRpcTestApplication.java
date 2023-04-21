package com.wenbin.zrpc.test;


import com.wenbin.zrpc.core.server.EnableZRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The zrpc service application
 * @author wenbin
 * Date：2023/4/17
 */
@SpringBootApplication
@EnableZRpcService
public class ZRpcTestApplication {


    public static void main(String[] args) {
        SpringApplication.run(ZRpcTestApplication.class, args);
        System.out.println("========Spring start successful!!!=========");
    }


}
