package com.bin.coolgateway.filter.limit;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: bin
 * @date: 2023/12/27 13:43
 **/
@Slf4j
public class CountLimit extends AbstractLimit{

    private Map<String,Integer> map = new HashMap<>();

    private Long timePoint = 0l;

    public CountLimit() {
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new MyTask(),timeWindow,timeWindow);
    }

    @Override
    public boolean doLimit(String ip) {
        synchronized (map){
            if(System.currentTimeMillis() > timePoint){
                map.clear();
                timePoint = System.currentTimeMillis() + timeWindow;
            }
            Integer cnt = map.get(ip);
            if(cnt == null){
                map.put(ip,1);
                return true;
            }
            if(cnt+1>qps) {
                return false;
            }
            map.put(ip,cnt+1);
            return true;
        }
    }

//    class MyTask extends TimerTask {
//
//        @Override
//        public void run() {
//            synchronized (map){
//                map.clear();
//            }
//        }
//    }
}
