package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.IRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 角色控制器
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    /**
     * 角色服务
     */
    @Autowired
    IRoleService roleService;

    /**
     * 得到所有的角色
     *
     * @return {@link String}
     */
    @RequiresPermissions("/roleMan")
    @RequestMapping("/getAllRole")
    public String getAllRole(){
        List<Role> roles = roleService.getAllRole();
        HashMap<String, Object> result = new HashMap<String, Object>();
        if(roles.size()==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("data",roles);
        return JSON.toJSONString(result);
    }

    /**
     * 获取角色列表
     *
     * @return {@link String}
     */
    @Log("查看角色列表")
    @RequiresPermissions("/roleMan")
    @RequestMapping("/getRoleList")
    public String getRoleList(){
        List<Role> roles = roleService.getRoleList();

        for(int i=0;i<roles.size();i++){
            List<PermissionChild> permissionChildren = roleService.getPermissionList(roles.get(i).getId());

            if(permissionChildren.size()!=0){
                List<Permission> permissions = roles.get(i).getPermissions();
                for(int j=0;j<permissions.size();j++){
                    List<PermissionChild> rPermissionChildren  = permissions.get(j).getPermissionChildren();

                    for(int k=0;k<rPermissionChildren.size();k++){

                        for(int m=0;m<permissionChildren.size();m++){
                            if(permissionChildren.get(m)!=null) {
                                if (rPermissionChildren.get(k).getPermissionName().equals(permissionChildren.get(m).getPermissionName())) {
                                    rPermissionChildren.get(k).setPermissionButtons(permissionChildren.get(m).getPermissionButtons());
                                }
                            }
                        }
                    }
                }
            }
        }

        for(Role r :roles){
            r.setChildren(r.getPermissions());
            for(Permission p : r.getPermissions()){
                p.setChildren(p.getPermissionChildren());
                for(PermissionChild pc : p.getPermissionChildren()){
                    pc.setChildren(pc.getPermissionButtons());
                }
            }
        }

        HashMap<String, Object> result = new HashMap<String, Object>();
        if(roles.size()==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("data",roles);
        return JSON.toJSONString(result);
    }

    /**
     *得到所有角色列表
     *
     * @return {@link String}
     */
    @RequiresPermissions("/putPermission")
    @RequestMapping("getAllRoleList")
    public String getAllRoleList(){
        List<Permission> permissions = roleService.getAllRoleList();
        for(Permission p : permissions){
            p.setChildren(p.getPermissionChildren());
            for(PermissionChild pc : p.getPermissionChildren()){
                pc.setChildren(pc.getPermissionButtons());
            }
        }
        Map<String,Object> result = new HashMap<>();
        if(permissions.size()==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("data",permissions);
        return JSON.toJSONString(result);
    }

    /**
     * 更新许可
     *
     * @param roleId 角色id
     * @param idStr  id str
     * @return {@link String}
     */
    @Log("分配权限")
    @RequiresPermissions("/putPermission")
    @RequestMapping("/updatePermission")
    public String updatePermission(@RequestParam("roleId")String roleId, @RequestParam("idStr") String idStr){
        List<String> permissionButtonList = new ArrayList<>();
        List<String> permissionChildList = new ArrayList<>();
        String real[] = idStr.split(",");
        List<String> permissionChildName = new ArrayList<>();
        for(PermissionChild p : roleService.getAllPermissionChild()){
            permissionChildName.add(p.getPermissionName());
        }
        List<String> permissionButtonName = new ArrayList<>();
        for(PermissionButton p : roleService.getAllPermissionButton()){
            permissionButtonName.add(p.getPermissionName());
        }
        for(int i=0;i<real.length;i++){
            if(permissionChildName.contains(real[i])){
                permissionChildList.add(real[i]);
            }
            if(permissionButtonName.contains(real[i])){
                permissionButtonList.add(real[i]);
            }
        }
        roleService.deletePermissionChild(roleId);
        roleService.deletePermissionButton(roleId);
        if(permissionButtonList.size()!=0){
            roleService.updatePermissionChild(roleId,permissionChildList);
        }
        if(permissionChildList.size()!=0){
            roleService.updatePermissionButton(roleId,permissionButtonList);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 更新的作用
     *
     * @param userId 用户id
     * @param roleId 角色id
     * @return {@link String}
     */
    @Log("分配角色")
    @RequiresPermissions("/putRole")
    @RequestMapping("/updateRole")
    public String updateRole(String userId,String roleId){
        roleService.deleteUserRole(userId);
        roleService.updateUserRole(userId,roleId);
        Map<String,Object> result = new HashMap<>();
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return {@link String}
     */
    @Log("添加角色")
    @RequiresPermissions("/addRole")
    @RequestMapping("/addRole")
    public String addRole(@RequestBody Role role){
        role.setId(UUID.randomUUID().toString());
        int i = roleService.addRole(role);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 获得更新的作用
     *
     * @param id id
     * @return {@link String}
     */
    @RequiresPermissions("/updateRole")
    @RequestMapping("/getUpdateRole")
    public String getUpdateRole(String id){
        Role role = roleService.getUpdateRole(id);
        return JSON.toJSONString(role);
    }

    /**
     * 编辑的角色
     *
     * @param role 角色
     * @return {@link String}
     */
    @Log("修改角色")
    @RequiresPermissions("/updateRole")
    @RequestMapping("/editRole")
    public String editRole(@RequestBody Role role){
        int i = roleService.updateRole(role);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return {@link String}
     */
    @Log("删除角色")
    @RequiresPermissions("/deleteRole")
    @RequestMapping("/deleteRole")
    public String deleteRole(String id){
        int i = roleService.deleteRole(id);
        roleService.deletePermissionButton(id);
        roleService.deletePermissionChild(id);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }


}
