package com.pp.code_generator.out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.code_generator.out.mybatis.entity.Test;
import com.pp.code_generator.out.mybatis.mapper.TestMapper;
import com.pp.code_generator.out.service.TestService;
import com.pp.code_generator.out.service.bo.TestCreateInBO;
import com.pp.code_generator.out.service.bo.TestQueryInBO;
import com.pp.code_generator.out.service.bo.TestUpdateInBO;
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
public class TestServiceImpl implements TestService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired
    private TestMapper testMapper;

    @Override
    public Test loadById(String id) {
        return testMapper.selectById(id);
    }

    @Override
    @Transactional
    public Test create(TestCreateInBO createArgs) {

        // TODO validate
        // ......

        // prepare insert DO args
        String testId = "xxxx";

        Test test = new Test();
        BeanUtils.copyProperties(createArgs, test);

        test.setCreateTime(new Date());
        test.setCreateBy("xxxx");
        test.setModifyTime(new Date());
        test.setModifyBy("xxxx");

        // insert DB
        testMapper.insert(test);

        return test;
    }

    @Override
    @Transactional
    public void update(TestUpdateInBO updateArgs) throws NotFoundException {


        // check exists
        if (testMapper.selectById(updateArgs.getId()) == null) {
            throw new NotFoundException("not fount");
        }

        // TODO validate
        // ......

        // prepare update DO args
        Test test = new Test();
        BeanUtils.copyProperties(updateArgs, test);

        test.setModifyTime(new Date());
        test.setModifyBy("xxxx");

        // update DB
        testMapper.updateById(test);
    }

    @Override
    @Transactional
    public void delete(String id) throws NotFoundException {

        // check exists
        if (testMapper.selectById(id) == null) {
            throw new NotFoundException("not fount");
        }

        // delete DB
        testMapper.deleteById(id);
    }

    @Override
    public IPage<Test> pageQuery(TestQueryInBO args, int page, int size) {

        // TODO prepare query args
        QueryWrapper<Test> wrapper = new QueryWrapper<>();
        wrapper.eq("account_id", "xxxx");
        if (args.getIds() != null && args.getIds().length > 0) {
            wrapper.in("id", Arrays.asList(args.getIds()));
        }
        // TODO
        wrapper.orderByDesc("create_time");

        // page query
        IPage<Test> iPage = new Page<>(page, size);
        IPage<Test> pages = testMapper.selectPage(iPage, wrapper);

        return pages;
    }

}