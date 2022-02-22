package com.pp.auth_server.service;

import com.pp.auth_server.domain.SysToken;
import com.pp.auth_server.domain.User;

import java.util.Map;

public interface IShiroService {

    public User getUserByName(String username);

    public Map<String, Object> createToken(String userId);

    public void logout(String token);

    public SysToken findByToken(String accessToken);

    public User findByUserId(String userId);
}
