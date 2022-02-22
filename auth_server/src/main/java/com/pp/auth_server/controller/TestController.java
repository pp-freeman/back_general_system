package com.pp.auth_server.controller;

import com.pp.auth_server.service.IMenuService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IMenuService menuService;
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/test")
    public String test(){
        System.out.println(menuService.getButtonByUser("001"));
        return "ok";
    }
}
