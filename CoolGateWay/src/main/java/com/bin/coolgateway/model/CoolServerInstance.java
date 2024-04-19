package com.bin.coolgateway.model;

import lombok.Data;
import java.util.Objects;

/**
 * @author: bin
 * @date: 2023/12/15 11:38
 **/
@Data
public class CoolServerInstance {

    /*
        服务名
     */
    protected String serviceName;

    protected String instanceId;

    protected String ip;

    protected int port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoolServerInstance that = (CoolServerInstance) o;
        return Objects.equals(instanceId, that.instanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId);
    }
}
