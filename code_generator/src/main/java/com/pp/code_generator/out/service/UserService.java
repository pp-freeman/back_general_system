package com.pp.code_generator.out.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pp.code_generator.out.mybatis.entity.User;
import com.pp.code_generator.out.service.bo.UserCreateInBO;
import com.pp.code_generator.out.service.bo.UserQueryInBO;
import com.pp.code_generator.out.service.bo.UserUpdateInBO;
import org.apache.ibatis.javassist.NotFoundException;

public interface UserService {

    User loadById(String id);

    User create(UserCreateInBO createArgs);

    void update(UserUpdateInBO updateArgs) throws NotFoundException;

    void delete(String id) throws NotFoundException;

    IPage<User> pageQuery(UserQueryInBO args, int page, int size);
}