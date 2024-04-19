package com.bin.coolgateway;

import com.bin.coolgateway.model.CoolGatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CoolGatewayProperties.class)
public class CoolGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoolGateWayApplication.class, args);
    }

}
