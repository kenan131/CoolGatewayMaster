package com.bin.coolgateway;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.common.executor.NameThreadFactory;
import com.bin.coolgateway.model.CoolGatewayProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.asynchttpclient.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class CoolGateWayApplicationTests {



    @Test
    void contextLoads() throws NacosException, InterruptedException {
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setAllocator(PooledByteBufAllocator.DEFAULT) // 使用池化的ByteBuf分配器以提升性能
                .setCompressionEnforced(true);// 强制压缩
        try (AsyncHttpClient asyncHttpClient =  new DefaultAsyncHttpClient(builder.build())) {
            // 创建一个GET请求
            Request request = new RequestBuilder()
                    .setUrl("https://localhost:8080/users")
                    .setMethod("GET")
                    .build();
            // 发送请求
            ListenableFuture<Response> responseListenableFuture = asyncHttpClient.executeRequest(request);
            CompletableFuture<Response> future = responseListenableFuture.toCompletableFuture();
            future.whenCompleteAsync((response, throwable) -> {
                System.out.println(response);
                System.out.println(throwable);
                throw  new RuntimeException();
            });
        } catch (Exception e) {
            if(e instanceof RuntimeException){
                System.out.println("1");
            }
            e.printStackTrace();
        }
        Thread.sleep(1000);
        System.out.println(2);
    }
    static List<String> whites;
    static {
//        whites = CoolGatewayProperties.getWhites();
    }

    @Autowired
    CoolGatewayProperties coolGatewayProperties;


    @Test
    public void test(){
//        System.out.println(whites);
        System.out.println(coolGatewayProperties.getWhites());
    }

    public static void main(String[] args)  {
    }

}
