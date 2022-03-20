package com.pp.code_generator.out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pp.code_generator.out.mybatis.entity.UserInfo;
import com.pp.code_generator.out.mybatis.mapper.UserInfoMapper;
import com.pp.code_generator.out.service.UserInfoService;
import com.pp.code_generator.out.service.bo.UserInfoCreateInBO;
import com.pp.code_generator.out.service.bo.UserInfoQueryInBO;
import com.pp.code_generator.out.service.bo.UserInfoUpdateInBO;
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
public class UserInfoServiceImpl implements UserInfoService {

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo loadById(String id) {
        return userInfoMapper.selectById(id);
    }

    @Override
    @Transactional
    public UserInfo create(UserInfoCreateInBO createArgs) {

        // TODO validate
        // ......

        // prepare insert DO args
        String userInfoId = "xxxx";

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(createArgs, userInfo);

        userInfo.setCreateTime(new Date());
        userInfo.setCreateBy("xxxx");
        userInfo.setModifyTime(new Date());
        userInfo.setModifyBy("xxxx");

        // insert DB
        userInfoMapper.insert(userInfo);

        return userInfo;
    }

    @Override
    @Transactional
    public void update(UserInfoUpdateInBO updateArgs) throws NotFoundException {


        // check exists
        if (userInfoMapper.selectById(updateArgs.getId()) == null) {
            throw new NotFoundException("not fount");
        }

        // TODO validate
        // ......

        // prepare update DO args
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(updateArgs, userInfo);

        userInfo.setModifyTime(new Date());
        userInfo.setModifyBy("xxxx");

        // update DB
        userInfoMapper.updateById(userInfo);
    }

    @Override
    @Transactional
    public void delete(String id) throws NotFoundException {

        // check exists
        if (userInfoMapper.selectById(id) == null) {
            throw new NotFoundException("not fount");
        }

        // delete DB
        userInfoMapper.deleteById(id);
    }

    @Override
    public IPage<UserInfo> pageQuery(UserInfoQueryInBO args, int page, int size) {

        // TODO prepare query args
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("account_id", "xxxx");
        if (args.getIds() != null && args.getIds().length > 0) {
            wrapper.in("id", Arrays.asList(args.getIds()));
        }
        // TODO
        wrapper.orderByDesc("create_time");

        // page query
        IPage<UserInfo> iPage = new Page<>(page, size);
        IPage<UserInfo> pages = userInfoMapper.selectPage(iPage, wrapper);

        return pages;
    }

}