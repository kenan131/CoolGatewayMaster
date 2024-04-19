package com.example.aservice.controller;

import com.bin.coolgatewaycore.common.CoolReference;
import com.example.aservice.common.UserDto;
import com.example.aservice.service.BService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: bin
 * @date: 2024/1/9 15:54
 **/
@RequestMapping("serviceA")
@RestController
public class ServiceAController {

    //通过注解实现 自动注入代理类。
    @CoolReference
    private BService bService;

    // 测试token   网关服务中AuthFilter类 处理鉴权功能，鉴权成功后，将userId塞入请求头中。下游必须用userId去请求头中获取。不然得改AuthFilter类中的存入name
    @GetMapping("getUserId")
    @ResponseBody
    public String getUser(HttpServletRequest request) {
        String userId = request.getHeader("userId");//获取userId的操作可以在每个服务中通过拦截器去实现。
        System.out.println("从请求头中获取的userId "+ userId);
        return "测试token，请求成功！";
    }

    //入口方法 controller
    @GetMapping("testGet")
    public UserDto getTest(){
        UserDto dto = bService.getAAA("入参A", 88);
        return dto;
    }

    @PostMapping("testPost")
    public UserDto postTest(){
        UserDto dto = bService.postBBB(new UserDto("入参B",99));
        return dto;
    }

}
