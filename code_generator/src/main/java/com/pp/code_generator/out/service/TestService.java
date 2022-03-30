package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.Test;
import com.pp.code_generator.out.service.bo.TestCreateInBO;
import com.pp.code_generator.out.service.bo.TestQueryInBO;
import com.pp.code_generator.out.service.bo.TestUpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface TestService {

    Test loadById(String id);

    Test create(TestCreateInBO createArgs);

    void update(TestUpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<Test> pageQuery(TestQueryInBO args, int page, int size);
}