package com.bin.coolgatewaycore.core;

import com.alibaba.fastjson.JSON;
import com.bin.coolgatewaycore.common.CoolMethod;
import com.bin.coolgatewaycore.common.CoolParam;
import com.bin.coolgatewaycore.common.GatewayResponseRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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

    public Object getProxy(Class iface) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{iface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class<?> returnType = method.getReturnType();
                String className = method.getDeclaringClass().getName();
                // 过滤 object 默认的方法。hashCode，toString，equals
                if (className.equals(Object.class.getName())) {
                    throw new RuntimeException("暂不支持Object 原生方法。");
                }
                GatewayResponseRes responseRes;
                CoolMethod annotation = method.getAnnotation(CoolMethod.class);
                String path = annotation.path();
                String url = domain + path;
                List<String> parameterName = getParameters(method);
                if (annotation.method().equals("get")) {
                    responseRes = requestClient.sendGetRequest(url, parameterName, args);
                } else {
                    responseRes = requestClient.sendPostRequest(url, parameterName, args);
                }
                Object res ;
                if (responseRes.getGatewayCode() == 10001 && responseRes.getData() != null) {
                    if (!isPrimitiveType(returnType)) {
                        res = JSON.parseObject(responseRes.getData().toString(), returnType);
                    } else {
                        res = responseRes.getData();
                    }
                    if (res == null) {
                        log.error("json 转换 对象失败。");
                        throw new RuntimeException("json 转换 对象失败。");
                    }
                } else {
                    throw new RuntimeException("下游服务，请求失败" + responseRes.getMessage());
                }
                return res;
            }
        });
    }
    // 判断是否基本类型。
    public boolean isPrimitiveType(Class<?> clazz) {
        String typeName = clazz.getName();
        boolean isPrimitive = typeName.equals("int") ||
                        typeName.equals("long") ||
                        typeName.equals("short") ||
                        typeName.equals("byte") ||
                        typeName.equals("double") ||
                        typeName.equals("float") ||
                        typeName.equals("char") ||
                        typeName.equals("boolean") ||
                        typeName.equals("java.lang.String");
        return isPrimitive;
    }

    public List<String> getParameters(Method method){
        ArrayList<String> parameters = new ArrayList<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        for(int i=0;i<annotations.length;i++){
            Annotation[] parameterAnnotations = annotations[i];
            for (Annotation annotation : parameterAnnotations) {
                if(annotation instanceof CoolParam){
                    CoolParam coolParam = (CoolParam) annotation;
                    String paramValue = coolParam.value();
                    parameters.add(paramValue);
                    break;
                }
            }
        }
        return parameters;
    }
}
