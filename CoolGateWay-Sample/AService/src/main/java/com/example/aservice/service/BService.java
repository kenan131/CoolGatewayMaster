package com.example.aservice.service;

import com.bin.coolgatewaycore.common.CoolMethod;
import com.bin.coolgatewaycore.common.CoolParam;
import com.example.aservice.common.UserDto;

/**
 * @author: bin
 * @date: 2024/1/3 15:45
 **/

public interface BService {

    @CoolMethod(path = "/serviceB/aaa",method = "get")
    public UserDto getAAA(@CoolParam(value = "name") String name,@CoolParam(value = "age") int age);

    @CoolMethod(path = "/serviceB/bbb",method = "post")
    public UserDto postBBB(@CoolParam(value = "dto")UserDto dto);

}
