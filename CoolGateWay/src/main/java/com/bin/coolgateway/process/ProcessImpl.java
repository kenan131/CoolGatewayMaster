package com.bin.coolgateway.process;

import com.bin.coolgateway.common.CoolGateWayException;
import com.bin.coolgateway.common.ResponseCode;
import com.bin.coolgateway.filter.GatewayFilterChain;
import com.bin.coolgateway.model.Context;
import com.bin.coolgateway.model.net.GatewayResponseRes;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: bin
 * @date: 2023/12/19 17:40
 **/
@Slf4j
@Component
public class ProcessImpl extends AbstractProcess{

    @Autowired
    protected GatewayFilterChain gatewayFilterChain;

    @Override
    public void process(FullHttpRequest request, ChannelHandlerContext ctx) {
        //创建网关上下文
        Context context = new Context(request,ctx);
        try {
            gatewayFilterChain.doFilters(context);
        }catch (CoolGateWayException e){
            log.error("过滤链内部发生错误 {} {}", e.getResponseCode().getCode(), e.getMessage());
            FullHttpResponse fullHttpResponse = GatewayResponseRes.builderGatewayRes(null, e.getResponseCode());
            ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            log.error("发生未知错误 {}", e);
            FullHttpResponse fullHttpResponse = GatewayResponseRes.builderGatewayRes(null, ResponseCode.GATEWAY_ERROR);
            ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
