package com.bin.coolgatewaycore.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: bin
 * @date: 2023/12/22 15:48
 **/
@Data
public class GatewayResponseRes implements Serializable {

    private static final long serialVersionUID = -8263363465897336689L;

    private int gatewayCode;
    private String message;
    private Object data;
}
