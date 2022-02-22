package com.pp.auth_server.service;

import com.pp.auth_server.domain.Permission;

import java.util.List;

public interface IPermissionService {
    public List<Permission> getAllPermission();
    public List<Permission> getAllPermissionChild();
    public List<Permission> getAllPermissionButton();

    public int addPermission(String id, String permissionname, String url);
    public int addPermissionChild(String id, String parent, String permissionname, String url);
    public int addPermissionButton(String id, String parent, String permissionname, String url);

    public Permission getUpdatePermission(String id);
    public Permission getUpdatePermissionChild(String id);
    public Permission getUpdatePermissionButton(String id);

    public int editPermission(String permissionname, String url, String id);
    public int editPermissionChild(String permissionname, String url, String id);
    public int editPermissionButton(String permissionname, String url, String id);

    public int deletePermission(String id);
    public int deletePermissionChild(String id);
    public int deletePermissionButton(String id);
}
