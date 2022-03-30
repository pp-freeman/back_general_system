package com.pp.code_generator.out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.code_generator.out.mybatis.entity.Book;
import com.pp.code_generator.out.mybatis.mapper.BookMapper;
import com.pp.code_generator.out.service.BookService;
import com.pp.code_generator.out.service.bo.BookCreateInBO;
import com.pp.code_generator.out.service.bo.BookQueryInBO;
import com.pp.code_generator.out.service.bo.BookUpdateInBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book loadById(String id) {
        return bookMapper.selectById(id);
    }

    @Override
    @Transactional
    public Book create(BookCreateInBO createArgs) {

        // TODO validate
        // ......

        // prepare insert DO args
        String bookId = "xxxx";

        Book book = new Book();
        BeanUtils.copyProperties(createArgs, book);

        book.setCreateTime(new Date());
        book.setCreateBy("xxxx");
        book.setModifyTime(new Date());
        book.setModifyBy("xxxx");

        // insert DB
        bookMapper.insert(book);

        return book;
    }

    @Override
    @Transactional
    public void update(BookUpdateInBO updateArgs) throws NotFoundException {


        // check exists
        if (bookMapper.selectById(updateArgs.getId()) == null) {
            throw new NotFoundException("not fount");
        }

        // TODO validate
        // ......

        // prepare update DO args
        Book book = new Book();
        BeanUtils.copyProperties(updateArgs, book);

        book.setModifyTime(new Date());
        book.setModifyBy("xxxx");

        // update DB
        bookMapper.updateById(book);
    }

    @Override
    @Transactional
    public void delete(String id) throws NotFoundException {

        // check exists
        if (bookMapper.selectById(id) == null) {
            throw new NotFoundException("not fount");
        }

        // delete DB
        bookMapper.deleteById(id);
    }

    @Override
    public IPage<Book> pageQuery(BookQueryInBO args, int page, int size) {

        // TODO prepare query args
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("account_id", "xxxx");
        if (args.getIds() != null && args.getIds().length > 0) {
            wrapper.in("id", Arrays.asList(args.getIds()));
        }
        // TODO
        wrapper.orderByDesc("create_time");

        // page query
        IPage<Book> iPage = new Page<>(page, size);
        IPage<Book> pages = bookMapper.selectPage(iPage, wrapper);

        return pages;
    }

}