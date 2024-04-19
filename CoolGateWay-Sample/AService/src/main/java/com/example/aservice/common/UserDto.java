package com.example.aservice.common;

/**
 * @author: bin
 * @date: 2024/1/4 17:46
 **/

public class UserDto {
    public UserDto() {
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int age;
    String name;

    public UserDto(String name, int age) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
