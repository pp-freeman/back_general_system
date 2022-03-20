package com.pp.code_generator.out.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.generator.dependency.JsonUtil;
import com.pp.code_generator.out.controller.dto.UserInfoCreateInDTO;
import com.pp.code_generator.out.controller.dto.UserInfoQueryInDTO;
import com.pp.code_generator.out.controller.dto.UserInfoUpdateInDTO;
import com.pp.code_generator.out.mybatis.entity.UserInfo;
import com.pp.code_generator.out.service.UserInfoService;
import com.pp.code_generator.out.service.bo.UserInfoCreateInBO;
import com.pp.code_generator.out.service.bo.UserInfoQueryInBO;
import com.pp.code_generator.out.service.bo.UserInfoUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/userinfo")
@Slf4j
public class UserInfoController {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping (value = "/getUserInfoById")
    public Object getUserInfoById(@RequestParam String id) {

        UserInfo userInfo = userInfoService.loadById(id);

        return userInfo;
    }

    @PostMapping (value = "/createUserInfo")
    public Object createUserInfo(@Valid @RequestBody UserInfoCreateInDTO createArgs) {

        UserInfoCreateInBO createInBo = JsonUtil.objectToObject(createArgs, UserInfoCreateInBO.class);
        UserInfo userInfo = userInfoService.create(createInBo);

        return userInfo;
    }

    @PutMapping (value = "/updateUserInfo")
    public Object updateUserInfo(@Valid @RequestBody UserInfoUpdateInDTO updateArgs) throws NotFoundException {

        UserInfoUpdateInBO updateInBo = JsonUtil.objectToObject(updateArgs, UserInfoUpdateInBO.class);
        userInfoService.update(updateInBo);

        return "success";
    }

    @DeleteMapping (value = "/deleteById")
    public Object deleteById(@RequestParam String id) throws NotFoundException {
        userInfoService.delete(id);

        return "success";
    }

    @GetMapping("/query")
    public Object pageQuery(@Valid UserInfoQueryInDTO queryArgs,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size) {

        UserInfoQueryInBO queryInBo = JsonUtil.objectToObject(queryArgs, UserInfoQueryInBO.class);
        IPage<UserInfo> iPage = userInfoService.pageQuery(queryInBo, page, size);

        return iPage;
    }

}




































