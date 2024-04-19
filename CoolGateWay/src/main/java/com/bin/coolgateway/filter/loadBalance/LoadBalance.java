package com.bin.coolgateway.filter.loadBalance;

import com.bin.coolgateway.cache.ServerCache;
import com.bin.coolgateway.model.CoolServerInstance;
/**
 * @author: bin
 * @date: 2023/12/20 10:32
 **/

public abstract class LoadBalance {

    protected ServerCache serverCache;
    public LoadBalance(ServerCache cache) {
        serverCache = cache;
    }

    abstract CoolServerInstance getServerInstance(String serviceName);
}
