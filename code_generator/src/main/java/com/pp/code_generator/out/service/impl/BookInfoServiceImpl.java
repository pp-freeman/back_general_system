package com.pp.code_generator.out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.code_generator.out.mybatis.entity.BookInfo;
import com.pp.code_generator.out.mybatis.mapper.BookInfoMapper;
import com.pp.code_generator.out.service.BookInfoService;
import com.pp.code_generator.out.service.bo.BookInfoCreateInBO;
import com.pp.code_generator.out.service.bo.BookInfoQueryInBO;
import com.pp.code_generator.out.service.bo.BookInfoUpdateInBO;
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
public class BookInfoServiceImpl implements BookInfoService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    public BookInfo loadById(String id) {
        return bookInfoMapper.selectById(id);
    }

    @Override
    @Transactional
    public BookInfo create(BookInfoCreateInBO createArgs) {

        // TODO validate
        // ......

        // prepare insert DO args
        String bookInfoId = "xxxx";

        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(createArgs, bookInfo);

        bookInfo.setCreateTime(new Date());
        bookInfo.setCreateBy("xxxx");
        bookInfo.setModifyTime(new Date());
        bookInfo.setModifyBy("xxxx");

        // insert DB
        bookInfoMapper.insert(bookInfo);

        return bookInfo;
    }

    @Override
    @Transactional
    public void update(BookInfoUpdateInBO updateArgs) throws NotFoundException {


        // check exists
        if (bookInfoMapper.selectById(updateArgs.getId()) == null) {
            throw new NotFoundException("not fount");
        }

        // TODO validate
        // ......

        // prepare update DO args
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(updateArgs, bookInfo);

        bookInfo.setModifyTime(new Date());
        bookInfo.setModifyBy("xxxx");

        // update DB
        bookInfoMapper.updateById(bookInfo);
    }

    @Override
    @Transactional
    public void delete(String id) throws NotFoundException {

        // check exists
        if (bookInfoMapper.selectById(id) == null) {
            throw new NotFoundException("not fount");
        }

        // delete DB
        bookInfoMapper.deleteById(id);
    }

    @Override
    public IPage<BookInfo> pageQuery(BookInfoQueryInBO args, int page, int size) {

        // TODO prepare query args
        QueryWrapper<BookInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("account_id", "xxxx");
        if (args.getIds() != null && args.getIds().length > 0) {
            wrapper.in("id", Arrays.asList(args.getIds()));
        }
        // TODO
        wrapper.orderByDesc("create_time");

        // page query
        IPage<BookInfo> iPage = new Page<>(page, size);
        IPage<BookInfo> pages = bookInfoMapper.selectPage(iPage, wrapper);

        return pages;
    }

}