package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.common.CommonResult;
import com.pp.auth_server.domain.EntityField;
import com.pp.auth_server.domain.FormEntity;
import com.pp.auth_server.domain.Semester;
import com.pp.auth_server.service.impl.CourseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/code")
public class CodeGeneratorController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CourseService courseService;

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


    @Log("查看字典")
    @GetMapping("/getDicList")
    public String getDicList(){
        String dictionType = "codeGener:codeGenerNotVal";
        String dic1 = dictionType.split(":")[0];
        String dic2 = dictionType.split(":")[1];
        List<Semester> semesters1 = courseService.getAllDiction(dic1);
        List<Semester> semesters2 = courseService.getAllDiction(dic2);
        semesters1.addAll(semesters2);
        HashMap<String,Object> result = new HashMap<>();
        for (Semester semester : semesters1) {
            if(semester.getState().equals("codeGenerNotVal")) {
                semester.setSemesterno(semester.getSemesterno() + ":0");
            } else if (semester.getState().equals("codeGener")){
                semester.setSemesterno(semester.getSemesterno() + ":1");
            }
        }
        if(semesters1==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("diction",semesters1);
        return JSON.toJSONString(result);
    }
}
