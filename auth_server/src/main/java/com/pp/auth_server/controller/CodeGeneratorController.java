package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.common.CommonResult;
import com.pp.auth_server.domain.EntityField;
import com.pp.auth_server.domain.FormEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/code")
public class CodeGeneratorController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service-url.code_generator}")
    private String userServiceUrl;

    @PostMapping("/addFiled")
    public CommonResult getUser(@RequestBody EntityField entityField) {
        return restTemplate.getForObject(userServiceUrl + "/code/addField?json={1}", CommonResult.class, JSON.toJSONString(entityField));
    }

    @GetMapping("/getFieldList")
    public CommonResult getFieldList() {
        return restTemplate.getForObject(userServiceUrl + "/code/getFieldList", CommonResult.class);
    }

    @GetMapping("/delFieldList")
    public CommonResult delFieldList() {
        return restTemplate.getForObject(userServiceUrl + "/code/delFieldList", CommonResult.class);
    }

    @PostMapping("/generate-code")
    public CommonResult generator(@RequestBody FormEntity formEntity) {
        return restTemplate.postForObject(userServiceUrl + "/code/generate-code", formEntity, CommonResult.class);
    }
}
