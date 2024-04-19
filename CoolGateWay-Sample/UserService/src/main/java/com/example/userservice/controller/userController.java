package com.example.userservice.controller;

import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: bin
 * @date: 2024/1/2 16:50
 **/
@RestController
@RequestMapping("user")
public class userController {

    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String login(HttpServletResponse response){
        String token = userService.getToken(10086L);
        Cookie cookie = new Cookie("coolToken", token);
        cookie.setMaxAge(1 * 24 * 3600);//一天
        response.addCookie(cookie);
        return "成功!";
    }

}
