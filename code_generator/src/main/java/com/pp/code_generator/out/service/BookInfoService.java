package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.BookInfo;
import com.pp.code_generator.out.service.bo.BookInfoCreateInBO;
import com.pp.code_generator.out.service.bo.BookInfoQueryInBO;
import com.pp.code_generator.out.service.bo.BookInfoUpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface BookInfoService {

    BookInfo loadById(String id);

    BookInfo create(BookInfoCreateInBO createArgs);

    void update(BookInfoUpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<BookInfo> pageQuery(BookInfoQueryInBO args, int page, int size);
}