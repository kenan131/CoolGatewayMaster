package com.bin.coolgateway.common;

/**
 * @author: bin
 * @date: 2023/12/20 11:00
 **/

public class CoolGateWayException extends RuntimeException {

    private ResponseCode responseCode;

    public CoolGateWayException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
