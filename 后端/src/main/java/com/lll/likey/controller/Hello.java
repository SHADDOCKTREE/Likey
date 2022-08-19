package com.lll.likey.controller;

import com.lll.likey.mapper.UsersMapper;
import com.lll.likey.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @Autowired
    UsersService usersService;
    @Autowired
    UsersMapper usersMapper;

    @GetMapping("/hello")
    public String hello(){
        return "hello ";
    }
}

