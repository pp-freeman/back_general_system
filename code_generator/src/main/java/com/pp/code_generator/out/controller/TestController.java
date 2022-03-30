package com.pp.code_generator.out.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.generator.dependency.JsonUtil;
import com.pp.code_generator.out.controller.dto.TestCreateInDTO;
import com.pp.code_generator.out.controller.dto.TestQueryInDTO;
import com.pp.code_generator.out.controller.dto.TestUpdateInDTO;
import com.pp.code_generator.out.mybatis.entity.Test;
import com.pp.code_generator.out.service.TestService;
import com.pp.code_generator.out.service.bo.TestCreateInBO;
import com.pp.code_generator.out.service.bo.TestQueryInBO;
import com.pp.code_generator.out.service.bo.TestUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/test")
@Slf4j
public class TestController {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private TestService testService;

    @GetMapping (value = "/getTestById")
    public Object getTestById(@RequestParam String id) {

        Test test = testService.loadById(id);

        return test;
    }

    @PostMapping (value = "/createTest")
    public Object createTest(@Valid @RequestBody TestCreateInDTO createArgs) {

        TestCreateInBO createInBo = JsonUtil.objectToObject(createArgs, TestCreateInBO.class);
        Test test = testService.create(createInBo);

        return test;
    }

    @PutMapping (value = "/updateTest")
    public Object updateTest(@Valid @RequestBody TestUpdateInDTO updateArgs) throws NotFoundException {

        TestUpdateInBO updateInBo = JsonUtil.objectToObject(updateArgs, TestUpdateInBO.class);
        testService.update(updateInBo);

        return "success";
    }

    @DeleteMapping (value = "/deleteById")
    public Object deleteById(@RequestParam String id) throws NotFoundException {
        testService.delete(id);

        return "success";
    }

    @GetMapping("/query")
    public Object pageQuery(@Valid TestQueryInDTO queryArgs,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size) {

        TestQueryInBO queryInBo = JsonUtil.objectToObject(queryArgs, TestQueryInBO.class);
        IPage<Test> iPage = testService.pageQuery(queryInBo, page, size);

        return iPage;
    }

}




































