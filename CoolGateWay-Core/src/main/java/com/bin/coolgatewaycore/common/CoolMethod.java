package com.bin.coolgatewaycore.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: bin
 * @date: 2024/1/4 17:21
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoolMethod {
    String path() default "";

    String method() default "get";
}
