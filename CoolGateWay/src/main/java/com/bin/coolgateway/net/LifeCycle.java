package com.bin.coolgateway.net;

import com.bin.coolgateway.common.CallBack;
import com.bin.coolgateway.process.AbstractProcess;

/**
 * @author: bin
 * @date: 2023/12/18 15:13
 **/

public abstract class LifeCycle {
    private CallBack statedCallBack;
    private CallBack stopCallBack;

    public abstract void init();

    abstract public void start();

    abstract public void stop();

    public void onStart(){
        if(statedCallBack != null){
            statedCallBack.call();
        }
    }
    public void onStop(){
        if(stopCallBack != null){
            stopCallBack.call();
        }
    }

    public void setStatedCallBack(CallBack statedCallBack) {
        this.statedCallBack = statedCallBack;
    }

    public void setStopCallBack(CallBack stopCallBack) {
        this.stopCallBack = stopCallBack;
    }
}
