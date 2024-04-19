package com.bin.coolgateway.filter.loadBalance;

import com.bin.coolgateway.cache.ServerCache;
import com.bin.coolgateway.common.CoolGateWayException;
import com.bin.coolgateway.common.GatewayConst;
import com.bin.coolgateway.common.ResponseCode;
import com.bin.coolgateway.filter.Filter;
import com.bin.coolgateway.model.Context;
import com.bin.coolgateway.model.CoolServerInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: bin
 * @date: 2023/12/20 10:37
 **/
@Slf4j
@Component
public class LoadBalanceFilter implements Filter {

    private  Map<String,LoadBalance> loadBalances = new HashMap<>();

    @Autowired
    private ServerCache serverCache;

    @PostConstruct
    void init(){
        loadBalances.put(GatewayConst.RANDOM,new RandomLoadBalance(serverCache));
        loadBalances.put(GatewayConst.ROUND,new RoundLoadBalance(serverCache));
    }

    public void addLoadBalance(String loadType,LoadBalance loadBalance){
        loadBalances.put(loadType,loadBalance);
    }

    @Override
    public void doFiler(Context context) {
        LoadBalance loadBalance = loadBalances.get(context.getGatewayRule().getLoadBalanceType());
        if(loadBalance == null){
            log.error("loadBalance trigger error:loadType {} not found relation instance",context.getGatewayRule().getLoadBalanceType());
            throw new CoolGateWayException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }
        CoolServerInstance serverInstance = loadBalance.getServerInstance(context.getServiceName());
        String host = serverInstance.getIp() + ":" + serverInstance.getPort();
        //设置新域名
        context.getGatewayRequest().setModifyHost(host);
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
