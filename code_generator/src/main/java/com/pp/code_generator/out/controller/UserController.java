package com.pp.code_generator.out.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.generator.dependency.JsonUtil;
import com.pp.code_generator.out.controller.dto.UserCreateInDTO;
import com.pp.code_generator.out.controller.dto.UserQueryInDTO;
import com.pp.code_generator.out.controller.dto.UserUpdateInDTO;
import com.pp.code_generator.out.mybatis.entity.User;
import com.pp.code_generator.out.service.UserService;
import com.pp.code_generator.out.service.bo.UserCreateInBO;
import com.pp.code_generator.out.service.bo.UserQueryInBO;
import com.pp.code_generator.out.service.bo.UserUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class UserController {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserService userService;

    @GetMapping (value = "/getUserById")
    public Object getUserById(@RequestParam String id) {

        User user = userService.loadById(id);

        return user;
    }

    @PostMapping (value = "/createUser")
    public Object createUser(@Valid @RequestBody UserCreateInDTO createArgs) {

        UserCreateInBO createInBo = JsonUtil.objectToObject(createArgs, UserCreateInBO.class);
        User user = userService.create(createInBo);

        return user;
    }

    @PutMapping (value = "/updateUser")
    public Object updateUser(@Valid @RequestBody UserUpdateInDTO updateArgs) throws NotFoundException {

        UserUpdateInBO updateInBo = JsonUtil.objectToObject(updateArgs, UserUpdateInBO.class);
        userService.update(updateInBo);

        return "success";
    }

    @DeleteMapping (value = "/deleteById")
    public Object deleteById(@RequestParam String id) throws NotFoundException {
        userService.delete(id);

        return "success";
    }

    @GetMapping("/query")
    public Object pageQuery(@Valid UserQueryInDTO queryArgs,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size) {

        UserQueryInBO queryInBo = JsonUtil.objectToObject(queryArgs, UserQueryInBO.class);
        IPage<User> iPage = userService.pageQuery(queryInBo, page, size);

        return iPage;
    }

}




































