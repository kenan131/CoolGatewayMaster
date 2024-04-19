package com.bin.coolgatewaycore.common;

import java.lang.annotation.*;

/**
 * @author: bin
 * @date: 2024/1/3 15:38
 **/

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CoolReference {
}
