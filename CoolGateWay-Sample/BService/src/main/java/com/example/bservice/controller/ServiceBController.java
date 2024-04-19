package com.example.bservice.controller;

import com.example.bservice.common.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author: bin
 * @date: 2023/12/14 14:41
 **/
@RestController
@RequestMapping("serviceB")
public class ServiceBController {

    @GetMapping("aaa")
    public UserDto getAAA(@PathParam("arg0") String arg0, @PathParam("arg1")String arg1) throws InterruptedException {
//        System.out.println("aaa get");
//        System.out.println("请求参数===> name="+arg0 + " age="+arg1);
        UserDto data = new UserDto( "aaa",18);
        return data;
    }

    @PostMapping("bbb")
    public UserDto postBBB(@RequestBody UserDto dto){
        System.out.println("bbb post");
        System.out.println("请求参数===> name="+dto.getName() + " age="+dto.getAge());
        UserDto data = new UserDto( "bbb",19);
        return data;
    }

}
