package com.bin.coolgateway.process;

import com.bin.coolgateway.filter.GatewayFilterChain;
import com.bin.coolgateway.model.Context;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: bin
 * @date: 2023/12/19 17:37
 **/

public abstract class AbstractProcess {
    public abstract void process(FullHttpRequest request, ChannelHandlerContext ctx);
}
