package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.Book;
import com.pp.code_generator.out.service.bo.BookCreateInBO;
import com.pp.code_generator.out.service.bo.BookQueryInBO;
import com.pp.code_generator.out.service.bo.BookUpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface BookService {

    Book loadById(String id);

    Book create(BookCreateInBO createArgs);

    void update(BookUpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<Book> pageQuery(BookQueryInBO args, int page, int size);
}