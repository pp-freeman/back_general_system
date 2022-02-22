package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IPermissionDao;
import com.pp.auth_server.domain.Permission;
import com.pp.auth_server.service.IPermissionService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    IPermissionDao permissionDao;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Permission> getAllPermission() {
        return permissionDao.getAllPermission();
    }

    @Override
    public List<Permission> getAllPermissionChild() {
        return permissionDao.getAllPermissionChild();
    }

    @Override
    public List<Permission> getAllPermissionButton() {
        return permissionDao.getAllPermissionButton();
    }

    @Override
    public int addPermission(String id, String permissionname, String url) {
        return permissionDao.addPermission(id,permissionname,url);
    }

    @Override
    public int addPermissionChild(String id, String parent, String permissionname, String url) {
        return permissionDao.addPermissionChild(id,parent,permissionname,url);
    }

    @Override
    public int addPermissionButton(String id, String parent, String permissionname, String url) {
        return permissionDao.addPermissionButton(id,parent,permissionname,url);
    }

    public Permission getUpdatePermission(String id){
        return permissionDao.getUpdatePermission(id);
    }

    public Permission getUpdatePermissionChild(String id){
        return permissionDao.getUpdatePermissionChild(id);
    }
    public Permission getUpdatePermissionButton(String id){
        return permissionDao.getUpdatePermissionButton(id);
    }

    public int editPermission(String permissionname,String url,String id){
        redisUtil.flushDb();
        return permissionDao.editPermission(permissionname,url,id);
    }
    public int editPermissionChild(String permissionname,String url,String id){
        redisUtil.flushDb();
        return permissionDao.editPermissionChild(permissionname,url,id);
    }
    public int editPermissionButton(String permissionname,String url,String id){
        redisUtil.flushDb();
        return permissionDao.editPermissionButton(permissionname,url,id);
    }

    public int deletePermission(String id){
        redisUtil.flushDb();
        return permissionDao.deletePermission(id);
    }
    public int deletePermissionChild(String id){
        redisUtil.flushDb();
        return permissionDao.deletePermissionChild(id);
    }
    public int deletePermissionButton(String id){
        redisUtil.flushDb();
        return permissionDao.deletePermissionButton(id);
    }
}
