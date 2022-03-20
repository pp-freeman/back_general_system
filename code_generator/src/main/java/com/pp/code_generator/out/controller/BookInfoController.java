package com.pp.code_generator.out.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.generator.dependency.JsonUtil;
import com.pp.code_generator.out.controller.dto.BookInfoCreateInDTO;
import com.pp.code_generator.out.controller.dto.BookInfoQueryInDTO;
import com.pp.code_generator.out.controller.dto.BookInfoUpdateInDTO;
import com.pp.code_generator.out.mybatis.entity.BookInfo;
import com.pp.code_generator.out.service.BookInfoService;
import com.pp.code_generator.out.service.bo.BookInfoCreateInBO;
import com.pp.code_generator.out.service.bo.BookInfoQueryInBO;
import com.pp.code_generator.out.service.bo.BookInfoUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/bookinfo")
@Slf4j
public class BookInfoController {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private BookInfoService bookInfoService;

    @GetMapping (value = "/getBookInfoById")
    public Object getBookInfoById(@RequestParam String id) {

        BookInfo bookInfo = bookInfoService.loadById(id);

        return bookInfo;
    }

    @PostMapping (value = "/createBookInfo")
    public Object createBookInfo(@Valid @RequestBody BookInfoCreateInDTO createArgs) {

        BookInfoCreateInBO createInBo = JsonUtil.objectToObject(createArgs, BookInfoCreateInBO.class);
        BookInfo bookInfo = bookInfoService.create(createInBo);

        return bookInfo;
    }

    @PutMapping (value = "/updateBookInfo")
    public Object updateBookInfo(@Valid @RequestBody BookInfoUpdateInDTO updateArgs) throws NotFoundException {

        BookInfoUpdateInBO updateInBo = JsonUtil.objectToObject(updateArgs, BookInfoUpdateInBO.class);
        bookInfoService.update(updateInBo);

        return "success";
    }

    @DeleteMapping (value = "/deleteById")
    public Object deleteById(@RequestParam String id) throws NotFoundException {
        bookInfoService.delete(id);

        return "success";
    }

    @GetMapping("/query")
    public Object pageQuery(@Valid BookInfoQueryInDTO queryArgs,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size) {

        BookInfoQueryInBO queryInBo = JsonUtil.objectToObject(queryArgs, BookInfoQueryInBO.class);
        IPage<BookInfo> iPage = bookInfoService.pageQuery(queryInBo, page, size);

        return iPage;
    }

}




































