package com.pp.auth_server.controller;//package com.pp.auth_server.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.pp.auth_server.domain.User;
//import com.pp.auth_server.service.IUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//
//@RestController
//@RequestMapping("/user")
//public class LoginController {
//
//    @Autowired
//    IUserService userService;
//
//    @RequestMapping("/login")
//    public String login(@RequestBody User user){
//        String flag = "error";
//        User us = userService.login(user.getUsername(),user.getPassword());
//        if(us!=null){
//            flag = "ok";
//        }
//        HashMap<String,Object> res = new HashMap<>();
//        res.put("flag",flag);
//        res.put("user",us);
//        String res_json = JSON.toJSONString(res);
//        return res_json;
//    }
//
//}
