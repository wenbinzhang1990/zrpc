package com.wenbin.zrpc.test;

import com.wenbin.zrpc.client.Complex;
import com.wenbin.zrpc.client.ComplexService;
import com.wenbin.zrpc.core.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * description
 * @author wenbin
 * Date：2023/4/21
 */
@Component
public class ComplexServiceInvoker {

    @Reference
    private ComplexService complexService;

    public Complex getComplex() {
        return complexService.getComplex();
    }
}
