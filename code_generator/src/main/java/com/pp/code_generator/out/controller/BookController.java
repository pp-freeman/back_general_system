package com.pp.code_generator.out.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.generator.dependency.JsonUtil;
import com.pp.code_generator.out.controller.dto.BookCreateInDTO;
import com.pp.code_generator.out.controller.dto.BookQueryInDTO;
import com.pp.code_generator.out.controller.dto.BookUpdateInDTO;
import com.pp.code_generator.out.mybatis.entity.Book;
import com.pp.code_generator.out.service.BookService;
import com.pp.code_generator.out.service.bo.BookCreateInBO;
import com.pp.code_generator.out.service.bo.BookQueryInBO;
import com.pp.code_generator.out.service.bo.BookUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/book")
@Slf4j
public class BookController {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private BookService bookService;

    @GetMapping (value = "/getBookById")
    public Object getBookById(@RequestParam String id) {

        Book book = bookService.loadById(id);

        return book;
    }

    @PostMapping (value = "/createBook")
    public Object createBook(@Valid @RequestBody BookCreateInDTO createArgs) {

        BookCreateInBO createInBo = JsonUtil.objectToObject(createArgs, BookCreateInBO.class);
        Book book = bookService.create(createInBo);

        return book;
    }

    @PutMapping (value = "/updateBook")
    public Object updateBook(@Valid @RequestBody BookUpdateInDTO updateArgs) throws NotFoundException {

        BookUpdateInBO updateInBo = JsonUtil.objectToObject(updateArgs, BookUpdateInBO.class);
        bookService.update(updateInBo);

        return "success";
    }

    @DeleteMapping (value = "/deleteById")
    public Object deleteById(@RequestParam String id) throws NotFoundException {
        bookService.delete(id);

        return "success";
    }

    @GetMapping("/query")
    public Object pageQuery(@Valid BookQueryInDTO queryArgs,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size) {

        BookQueryInBO queryInBo = JsonUtil.objectToObject(queryArgs, BookQueryInBO.class);
        IPage<Book> iPage = bookService.pageQuery(queryInBo, page, size);

        return iPage;
    }

}




































