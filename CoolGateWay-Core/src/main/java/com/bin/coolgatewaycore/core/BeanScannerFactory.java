package com.bin.coolgatewaycore.core;

import com.bin.coolgatewaycore.common.CoolReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author: bin
 * @date: 2024/1/4 9:52
 **/
@Slf4j
public class BeanScannerFactory implements InstantiationAwareBeanPostProcessor {

    private String domain;

    private ServiceFactory serviceFactory;

    public BeanScannerFactory(String domain) {
        this.domain = domain;
        serviceFactory = new ServiceFactory(domain);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if(field.isAnnotationPresent(CoolReference.class)){
                    Class iface = field.getType();
                    if(!iface.isInterface()) {
                        log.error("不允许将CoolReference注解加在非接口上");
                        throw new RuntimeException("不允许将CoolReference注解加在非接口上");
                    }
                    Object proxy = serviceFactory.getProxy(iface);
                    field.setAccessible(true);
                    field.set(bean,proxy);
                }
            }
        });
        return true;
    }
}
