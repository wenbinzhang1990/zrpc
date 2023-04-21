package com.wenbin.zrpc.core.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark the service referencing the current annotation as rpc service
 * @Author wenbin
 * @Date 2023/3/15
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {

    String version() default "";
}
