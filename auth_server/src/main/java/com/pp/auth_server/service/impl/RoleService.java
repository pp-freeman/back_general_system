package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IRoleDao;
import com.pp.auth_server.dao.IUserDao;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.IRoleService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RoleService implements IRoleService {

    @Autowired
    IRoleDao roleDao;

    @Autowired
    IUserDao userDao;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public List<Role> getAllRole() {
        return roleDao.getAllRole();
    }

    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }

    @Override
    public List<PermissionChild> getPermissionList(String id) {
        return roleDao.getPermissionList(id);
    }

    public List<Permission> getAllRoleList(){
        return roleDao.getAllRoleList();
    }
    //得到所有一级菜单
    public List<Permission> getAllPermission(){
        return roleDao.getAllPermission();
    }
    //得到所有二级菜单
    public List<PermissionChild> getAllPermissionChild(){
        return roleDao.getAllPermissionChild();
    }
    //得到所有三级菜单
    public List<PermissionButton> getAllPermissionButton(){
        return roleDao.getAllPermissionButton();
    }
    //删除已有菜单权限
    public int deletePermissionChild(String roleId){
        redisUtil.flushDb();
        return roleDao.deletePermissionChild(roleId);
    }
    //删除已有按钮权限
    public int deletePermissionButton(String roleId){
        redisUtil.flushDb();
        return roleDao.deletePermissionButton(roleId);
    }
    //修改菜单权限
    public int updatePermissionChild(String roleId,List<String>idList){
        redisUtil.flushDb();
        return roleDao.updatePermissionChild(roleId,idList);
    }
    //修改按钮权限
    public int updatePermissionButton(String roleId,List<String>idList){
        redisUtil.flushDb();
        return roleDao.updatePermissionButton(roleId,idList);
    }

    @Override
    public void deleteUserRole(String userId) {
        redisUtil.flushDb();
        roleDao.deleteUserRole(userId);
    }

    @Override
    public void updateUserRole(String userId, String roleId) {
        redisUtil.flushDb();
        roleDao.updateUserRole(userId,roleId);
    }

    public int addRole(Role role){
        return roleDao.addRole(role);
    }

    public Role getUpdateRole(String id){
        return roleDao.getUpdateRole(id);
    }

    //修改角色
    public int updateRole(Role role){
        redisUtil.flushDb();
        return roleDao.updateRole(role);
    }
    //删除角色
    public int deleteRole(String id) {
        redisUtil.flushDb();
        return roleDao.deleteRole(id);
    }
    //通过id获得角色
    public String getRoleByUserId(String id){
        redisUtil.flushDb();
        return roleDao.getRoleByUserId(id);
    }
    //通过用户的所有permissionchild
    public List<PermissionChild> getPermissionChildByUserId(String id){
        return roleDao.getPermissionChildByUserId(id);
    }

    //所有角色下对应的用户
    public Map<String, Object> getAllRoleAndUser(){
        List<RoleUser> list = roleDao.getAllRoleAndUser();
        List<User> userList = userDao.getUserList();
        Map<String,Object> map = new HashMap<>();
        for (RoleUser roleUser : list) {
            if (map.containsKey(roleUser.getRoleName())) {
                String users = (String) map.get(roleUser.getRoleName());
                users = users + "," + roleUser.getUserId();
                map.replace(roleUser.getRoleName(), users);
            } else {
                map.put(roleUser.getRoleName(), roleUser.getUserId());
            }
        }
        String users = "";
        for (User user : userList) {
            if ("".equals(users)) {
                users = user.getId();
            } else {
                users = users + "," + user.getId();
            }
        }
        map.put("users", users);
        return map;
    }
}
