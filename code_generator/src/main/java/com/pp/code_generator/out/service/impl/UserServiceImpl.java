package com.pp.code_generator.out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.code_generator.out.mybatis.entity.User;
import com.pp.code_generator.out.mybatis.mapper.UserMapper;
import com.pp.code_generator.out.service.UserService;
import com.pp.code_generator.out.service.bo.UserCreateInBO;
import com.pp.code_generator.out.service.bo.UserQueryInBO;
import com.pp.code_generator.out.service.bo.UserUpdateInBO;
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
public class UserServiceImpl implements UserService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired
    private UserMapper userMapper;

    @Override
    public User loadById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional
    public User create(UserCreateInBO createArgs) {

        // TODO validate
        // ......

        // prepare insert DO args
        String userId = "xxxx";

        User user = new User();
        BeanUtils.copyProperties(createArgs, user);

        user.setCreateTime(new Date());
        user.setCreateBy("xxxx");
        user.setModifyTime(new Date());
        user.setModifyBy("xxxx");

        // insert DB
        userMapper.insert(user);

        return user;
    }

    @Override
    @Transactional
    public void update(UserUpdateInBO updateArgs) throws NotFoundException {


        // check exists
        if (userMapper.selectById(updateArgs.getId()) == null) {
            throw new NotFoundException("not fount");
        }

        // TODO validate
        // ......

        // prepare update DO args
        User user = new User();
        BeanUtils.copyProperties(updateArgs, user);

        user.setModifyTime(new Date());
        user.setModifyBy("xxxx");

        // update DB
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void delete(String id) throws NotFoundException {

        // check exists
        if (userMapper.selectById(id) == null) {
            throw new NotFoundException("not fount");
        }

        // delete DB
        userMapper.deleteById(id);
    }

    @Override
    public IPage<User> pageQuery(UserQueryInBO args, int page, int size) {

        // TODO prepare query args
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account_id", "xxxx");
        if (args.getIds() != null && args.getIds().length > 0) {
            wrapper.in("id", Arrays.asList(args.getIds()));
        }
        // TODO
        wrapper.orderByDesc("create_time");

        // page query
        IPage<User> iPage = new Page<>(page, size);
        IPage<User> pages = userMapper.selectPage(iPage, wrapper);

        return pages;
    }

}