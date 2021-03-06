package com.pp.auth_server.dao;

import com.pp.auth_server.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao {
    public User getUserByMessage(String username, String password);

    //根据用户名查找信息
    public User getUserByName(String username);

    public User findUserById(String userId);

    public List<User> getAllUser(String username, String roleName, int pageStart, int pageSize);

    public int getAllUserCount(String username, String roleName);

    public int updateUserState(String id, int state);

    public int addUser(String id, String username, String password);
    public int deleteUser(String id);
    public User getUpdateUser(String id);
    public int editUser(User user);

    public List<User> getUserList();

}
