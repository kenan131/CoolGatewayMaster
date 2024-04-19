package com.bin.coolgatewaycore.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bin.coolgatewaycore.common.GatewayResponseRes;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: bin
 * @date: 2024/1/4 14:24
 **/
@Slf4j
public class RequestClient {
    static private AsyncHttpClient asyncHttpClient;

    static {
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(new NioEventLoopGroup())
                .setAllocator(PooledByteBufAllocator.DEFAULT) // 使用池化的ByteBuf分配器以提升性能
                .setCompressionEnforced(true);// 强制压缩
        asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
    }


    public GatewayResponseRes sendGetRequest(String url, List parameterName, Object[] parameterArgs){
        String pathParameter = getParameterPath(parameterName,parameterArgs);
        String finUrl = url +  pathParameter;
        try{
            Response response = asyncHttpClient.prepareGet(finUrl)
                    .setReadTimeout(3000)
                    .execute().get();
            String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
            GatewayResponseRes responseRes = JSON.parseObject(responseBody, GatewayResponseRes.class);
            return responseRes;
        }catch (Exception e){
            log.error("get 请求发送失败。请求路径："+finUrl);
            throw new RuntimeException(e.getMessage());
        }
    }

    //构造get请求，路径中的参数。
    private String getParameterPath(List parameterName, Object[] parameterArgs) {
        //参数为空，参数和名称不相等都不设置参数。
        if(parameterName.size() == 0 || parameterArgs == null || parameterName.size() != parameterArgs.length){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("?");
        for(int i=0;i<parameterName.size();i++){
            if(i!=0){
                stringBuilder.append("&");
            }
            stringBuilder.append(parameterName.get(i)+"="+parameterArgs[i].toString());
        }
        return stringBuilder.toString();
    }

    public GatewayResponseRes sendPostRequest(String finUrl, List<String> parameterNames, Object[] args){
        BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(finUrl)
                .setRequestTimeout(3000);//默认超时3s
        try{
            if(parameterNames.size() > 1){
                //如果参数大于1 则通过form表单传输
                for(int i =0;i<parameterNames.size();i++){
                    requestBuilder.addFormParam(parameterNames.get(i),args[i].toString());
                }
            }else{
                //否则，通过json串进行传输。默认取第一个参数
                if(args != null && args[0] != null){
                    Object arg = args[0];
                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(arg));
                    requestBuilder.setBody(jsonObject.toJSONString());
                    requestBuilder.setHeader("Content-Type","application/json");
                }
            }
            Response response = requestBuilder.execute().get();
            String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
            GatewayResponseRes responseRes = JSON.parseObject(responseBody, GatewayResponseRes.class);
            return responseRes;
        }catch (Exception e) {
            log.error("post请求异常 请求路径：" + finUrl);
            throw new RuntimeException(e.getMessage());
        }
    }

}
