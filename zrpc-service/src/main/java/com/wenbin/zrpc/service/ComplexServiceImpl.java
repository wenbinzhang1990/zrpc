package com.wenbin.zrpc.service;

import com.wenbin.zrpc.client.Complex;
import com.wenbin.zrpc.client.ComplexService;
import com.wenbin.zrpc.core.config.annotation.Service;

/**
 * description
 * @author wenbin
 * Date：2023/4/21
 */

@Service(value = ComplexService.class)
public class ComplexServiceImpl implements ComplexService {

    @Override
    public Complex getComplex() {
        return Complex.createComplex();
    }
}
