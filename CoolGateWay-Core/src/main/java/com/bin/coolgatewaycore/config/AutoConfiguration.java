package com.bin.coolgatewaycore.config;

import com.bin.coolgatewaycore.core.BeanScannerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author: bin
 * @date: 2024/1/12 17:34
 **/
@Configuration
public class AutoConfiguration {

    @Value("${cool.gateway.domain}")
    public String gatewayDomain;

    @Bean
    @ConditionalOnClass(BeanScannerFactory.class)
    public BeanScannerFactory jwtSignerHolder(){
        return new BeanScannerFactory(gatewayDomain);
    }
}
