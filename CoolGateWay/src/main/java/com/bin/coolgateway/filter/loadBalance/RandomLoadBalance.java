package com.bin.coolgateway.filter.loadBalance;

import com.bin.coolgateway.cache.ServerCache;
import com.bin.coolgateway.common.CoolGateWayException;
import com.bin.coolgateway.common.ResponseCode;
import com.bin.coolgateway.model.CoolServerInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: bin
 * @date: 2023/12/20 10:32
 **/

@Slf4j
public class RandomLoadBalance extends LoadBalance{


    public RandomLoadBalance(ServerCache cache) {
        super(cache);
    }

    @Override
    public CoolServerInstance getServerInstance(String serviceName) {
        List<CoolServerInstance> coolServerInstances = serverCache.getInstanceListByServiceName(serviceName);
        if(coolServerInstances == null){
            log.error("according to {} not found serviceInstance",serviceName);
            throw new CoolGateWayException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }
        Set<CoolServerInstance> serverInstances = serverCache.getInstanceSetByServiceName(serviceName);
        if(serverInstances.isEmpty()){
            throw new CoolGateWayException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(serverInstances.size());
        return coolServerInstances.get(randomIndex);
    }
}
