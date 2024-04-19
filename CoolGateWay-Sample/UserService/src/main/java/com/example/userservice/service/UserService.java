package com.example.userservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: bin
 * @date: 2024/1/2 16:52
 **/
@Service
public class UserService {

    public String getToken(Long userId){
        String accessKey = "coolGateway";
        String token = Jwts.builder()
                .setSubject(String.valueOf(userId)) // 设置JWT的主题，通常为用户的唯一标识
                .setIssuedAt(new Date()) // 设置JWT的签发时间，通常为当前时间
                .signWith(SignatureAlgorithm.HS256, accessKey) // 使用HS256算法签名JWT，使用密钥"security"
                .compact(); // 构建JWT并返回字符串表示
        return token;
    }
}
