package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IMenuDao;
import com.pp.auth_server.domain.Menu;
import com.pp.auth_server.service.IMenuService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {

    @Autowired
    IMenuDao menuDao;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Menu> findMenu(String id) {
        //使用redis缓存
        if(redisUtil.get(id+"menu")==null){
            redisUtil.set(id+"menu",menuDao.findMenus(id));
            redisUtil.expire(id+"menu",30*60);
            return (List<Menu>) redisUtil.get(id+"menu");
        }else{
            return (List<Menu>) redisUtil.get(id+"menu");
        }
    }

    public List<String> getButtonByUser(String id){
        //使用redis缓存
        if(redisUtil.get(id+"button")==null){
            redisUtil.set(id+"button",menuDao.getButtonByUser(id));
            redisUtil.expire(id+"button",30*60);
            return (List<String>) redisUtil.get(id+"button");
        }else{
            return (List<String>) redisUtil.get(id+"button");
        }
    }
}
