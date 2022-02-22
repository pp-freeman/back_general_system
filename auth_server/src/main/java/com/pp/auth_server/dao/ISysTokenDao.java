package com.pp.auth_server.dao;

import com.pp.auth_server.domain.SysToken;
import org.springframework.stereotype.Repository;

@Repository
public interface ISysTokenDao {

    //根据用户id获得token
    public SysToken findTokenByUserId(String userId);

    //保存token
    public void save(SysToken sysToken);

    public void update(SysToken sysToken);

    public SysToken findByToken(String accessToken);
}
