package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.config.VerificationCode;
import com.pp.auth_server.domain.User;
import com.pp.auth_server.service.IRoleService;
import com.pp.auth_server.service.impl.ShiroService;
import com.pp.auth_server.utils.MD5Utils;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class ShiroController {

    public static User user;

    private final ShiroService shiroService;

    @Autowired
    IRoleService roleService;

    @Autowired
    RedisUtil redisUtil;


    public ShiroController(ShiroService shiroService) {
        this.shiroService = shiroService;
    }


    /**
     * 登录
     */
    @Log("登录")
    @RequestMapping("/login")
    public String login(@RequestBody User us, HttpServletRequest request, HttpServletResponse resp) {
        Map<String, Object> result = new HashMap<>();
        if(request.getSession(true).getAttribute("verify_code")==null||!us.getVerifyCode().toUpperCase().equals(request.getSession(true).getAttribute("verify_code").toString().toUpperCase())){
            result.put("status", 300);
            result.put("msg", "验证码错误");
            return JSON.toJSONString(result);
        }
        String username = us.getUsername();
        String password = us.getPassword();
        //用户信息
        User user = shiroService.getUserByName(username);

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(MD5Utils.generatePassword(password))) {
            result.put("status", 400);
            result.put("msg", "账号或密码有误");
        }else if(user.getState()==0){
            result.put("status", 404);
            result.put("msg", "账号未激活，请联系管理员");
        }else {
            if(request.getSession(true).getAttribute(username)!=null){
                result.put("status", 302);
                result.put("msg", "此账号此时在线！！！");
                return JSON.toJSONString(result);
            }
            this.user = user;
            //生成token，并保存到数据库
            result = shiroService.createToken(user.getId());
            result.put("status", 200);
            result.put("msg", "登陆成功");
            result.put("id",user.getId());
            result.put("username",user.getUsername());
            result.put("rolename",roleService.getRoleByUserId(user.getId()));
        }
        String res = JSON.toJSONString(result);
        return res;
    }

    /**
     * 得到验证码图片
     * @param request
     * @param resp
     * @throws IOException
     */
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        VerificationCode.output(image,resp.getOutputStream());
    }

    /**
     * 退出
     */
    @Log("退出登录")
    @RequestMapping("/logout")
    public String logout(String token, String username, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        shiroService.logout(token);
        result.put("status", 200);
        result.put("msg", "您已安全退出系统");
        return JSON.toJSONString(result);
    }

}
