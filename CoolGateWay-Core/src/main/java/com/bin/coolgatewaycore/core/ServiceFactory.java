package com.bin.coolgatewaycore.core;

import com.alibaba.fastjson.JSON;
import com.bin.coolgatewaycore.common.CoolMethod;
import com.bin.coolgatewaycore.common.GatewayResponseRes;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @author: bin
 * @date: 2024/1/4 11:51
 **/
@Slf4j
public class ServiceFactory {


    static private RequestClient requestClient = new RequestClient();
    
    private String domain;

    public ServiceFactory(String domain) {
        this.domain = domain;
    }

    public Object getProxy(Class iface){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class<?> returnType = method.getReturnType();
                String className = method.getDeclaringClass().getName();
                // 过滤 object 默认的方法。hashCode，toString，equals
                if (className.equals(Object.class.getName())) {
                    throw new RuntimeException("暂不支持Object 原生方法。");
                }
                GatewayResponseRes responseRes ;
                CoolMethod annotation = method.getAnnotation(CoolMethod.class);
                String path = annotation.path();
                String url = domain + path;
                Parameter[] parameters = method.getParameters();
                ArrayList<String> parameterName = new ArrayList<>();
                for(Parameter parameter : parameters){
                    parameterName.add(parameter.getName());
                }
                if(annotation.method().equals("get")){
                    responseRes = requestClient.sendGetRequest(url, parameterName, args);
                }
                else{
                    responseRes = requestClient.sendPostRequest(url, parameterName, args);
                }
                Object res;
                if(responseRes.getGatewayCode() == 10001){
                    res = JSON.parseObject(responseRes.getData().toString(), returnType);
                    if(res == null){
                        log.error("json 转换 对象失败。");
                        throw new RuntimeException("json 转换 对象失败。");
                    }
                }
                else{
                    throw new RuntimeException("下游服务，请求失败" + responseRes.getMessage());
                }
                return res;
            }
        });
    }

}
