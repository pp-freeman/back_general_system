package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.ISysTokenDao;
import com.pp.auth_server.dao.IUserDao;
import com.pp.auth_server.domain.SysToken;
import com.pp.auth_server.domain.User;
import com.pp.auth_server.service.IShiroService;
import com.pp.auth_server.utils.RedisUtil;
import com.pp.auth_server.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShiroService implements IShiroService {


    @Autowired
    private IUserDao userDao;
    @Autowired
    private ISysTokenDao sysTokenDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 根据username查找用户
     *
     * @param username
     * @return User
     */
    @Override
    public User getUserByName(String username) {
        User user = userDao.getUserByName(username);
        return user;
    }

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Override
    /**
     * 生成一个token
     *@param  [userId]
     *@return Result
     */
    public Map<String, Object> createToken(String userId) {
        Map<String, Object> result = new HashMap<>();
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SysToken tokenEntity = sysTokenDao.findTokenByUserId(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysToken();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(new String(Base64.getEncoder().encode(token.getBytes())));
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            sysTokenDao.save(tokenEntity);
        } else {
            tokenEntity.setToken(new String(Base64.getEncoder().encode(token.getBytes())));
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            sysTokenDao.update(tokenEntity);
        }
        result.put("token", token);
        result.put("expire", EXPIRE);
        return result;
    }

    @Override
    public void logout(String token) {
        SysToken byToken = findByToken(token);
        //生成一个token
        token = TokenGenerator.generateValue();
        //修改token
        SysToken tokenEntity = new SysToken();
        tokenEntity.setUserId(byToken.getUserId());
        tokenEntity.setToken(token);
        sysTokenDao.update(tokenEntity);
    }

    @Override
    public SysToken findByToken(String accessToken) {
        if(redisUtil.get(new String(Base64.getEncoder().encode(accessToken.getBytes())))==null){
            redisUtil.set(new String(Base64.getEncoder().encode(accessToken.getBytes())),sysTokenDao.findByToken(new String(Base64.getEncoder().encode(accessToken.getBytes()))));
            redisUtil.expire(new String(Base64.getEncoder().encode(accessToken.getBytes())),30*60);
            return (SysToken) redisUtil.get(new String(Base64.getEncoder().encode(accessToken.getBytes())));
        }else{
            return (SysToken) redisUtil.get(new String(Base64.getEncoder().encode(accessToken.getBytes())));
        }
    }

    @Override
    public User findByUserId(String userId) {
        if(redisUtil.get(userId)==null){
            redisUtil.set(userId,userDao.findUserById(userId));
            redisUtil.expire(userId,30*60);
            return (User) redisUtil.get(userId);
        }else {
            return (User) redisUtil.get(userId);
        }
    }
}