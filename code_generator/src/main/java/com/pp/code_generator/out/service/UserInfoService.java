package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.UserInfo;
import com.pp.code_generator.out.service.bo.UserInfoCreateInBO;
import com.pp.code_generator.out.service.bo.UserInfoQueryInBO;
import com.pp.code_generator.out.service.bo.UserInfoUpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface UserInfoService {

    UserInfo loadById(String id);

    UserInfo create(UserInfoCreateInBO createArgs);

    void update(UserInfoUpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<UserInfo> pageQuery(UserInfoQueryInBO args, int page, int size);
}