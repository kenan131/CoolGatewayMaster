package com.example.aservice.service;

import com.bin.coolgatewaycore.common.CoolMethod;
import com.example.aservice.common.UserDto;

/**
 * @author: bin
 * @date: 2024/1/3 15:45
 **/

public interface BService {

    @CoolMethod(path = "/serviceB/aaa",method = "get")
    public UserDto getAAA(String name, int age);

    @CoolMethod(path = "/serviceB/bbb",method = "post")
    public UserDto postBBB(UserDto dto);

}
