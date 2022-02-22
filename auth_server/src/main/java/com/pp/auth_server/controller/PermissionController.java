package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.Permission;
import com.pp.auth_server.service.IPermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 权限控制器
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/permission")

public class PermissionController {
    /**
     * 许可服务
     */
    @Autowired
    IPermissionService permissionService;

    /**
     * 获得所有许可
     *
     * @param level 水平
     * @return {@link String}
     */
    @Log("查看权限资源")
    @RequiresPermissions("/permissionMan")
    @RequestMapping("/getAllPermission")
    public String getAllPermission(String level){
        List<Permission> list = new ArrayList<>();
        if (level == null || level.equals("")) {
            list.addAll(permissionService.getAllPermission());
            list.addAll(permissionService.getAllPermissionChild());
            list.addAll(permissionService.getAllPermissionButton());
        }else{
            if(level.equals("1")){
                list.addAll(permissionService.getAllPermission());
            }
            if(level.equals("2")){
                list.addAll(permissionService.getAllPermissionChild());
            }
            if(level.equals("3")){
                list.addAll(permissionService.getAllPermissionButton());
            }
        }
        HashMap<String,Object> result = new HashMap<>();
        if(list.size()==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("data",list);
        return JSON.toJSONString(result);
    }

    /**
     * 获取表单数据
     *
     * @return {@link String}
     */

    @RequiresPermissions("/permissionMan")
    @RequestMapping("/getFormData")
    public String getFormData(){
        List<Permission> permissions = permissionService.getAllPermission();
        List<Permission> permissionChildren = permissionService.getAllPermissionChild();
        HashMap<String,Object> result = new HashMap<>();
        result.put("permissions",permissions);
        result.put("permissionChildren",permissionChildren);
        return JSON.toJSONString(result);
    }

    /**
     * 添加权限资源
     *
     * @param level          水平
     * @param permissionname permissionname
     * @param parent         父
     * @param url            url
     * @return {@link String}
     */
    @Log("添加权限资源")
    @RequiresPermissions("/addPermission")
    @RequestMapping("/addPermission")
    public String addPermission(String level,String permissionname,String parent,String url){
        int i=0;
        if(level.equals("1")){
            i=permissionService.addPermission(UUID.randomUUID().toString(),permissionname,url);
        }
        if(level.equals("2")){
            i=permissionService.addPermissionChild(UUID.randomUUID().toString(),parent,permissionname,url);
        }
        if(level.equals("3")){
            i=permissionService.addPermissionButton(UUID.randomUUID().toString(),parent,permissionname,url);
        }
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state","404");
        }else{
            result.put("state","200");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 修改资源权限
     *
     * @param id    id
     * @param level 水平
     * @return {@link String}
     */
    @RequiresPermissions("/editPermission")
    @RequestMapping("/getUpdatePermission")
    public String getUpdatePermission(String id,String level){

        Permission permission = null;
        if(level.equals("1")){
           permission=permissionService.getUpdatePermission(id);
        }
        if(level.equals("2")){
            permission=permissionService.getUpdatePermissionChild(id);
        }
        if(level.equals("3")){
            permission=permissionService.getUpdatePermissionButton(id);
        }
        HashMap<String,Object> result = new HashMap<>();
        if(permission==null){
            result.put("state","404");
        }else{
            result.put("state","200");
        }

        result.put("permission",permission);
        return JSON.toJSONString(result);
    }

    /**
     * 编辑权限
     *
     * @param level          水平
     * @param permissionname permissionname
     * @param url            url
     * @param id             id
     * @return {@link String}
     */
    @Log("修改权限资源")
    @RequiresPermissions("/editPermission")
    @RequestMapping("editPermission")
    public String editPermission(String level,String permissionname,String url,String id){
        int i=0;
        if(level.equals("1")){
            i=permissionService.editPermission(permissionname,url,id);
        }
        if(level.equals("2")){
            i=permissionService.editPermissionChild(permissionname,url,id);
        }
        if(level.equals("3")){
            i=permissionService.editPermissionButton(permissionname,url,id);
        }
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state","404");
        }else{
            result.put("state","200");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除权限
     *
     * @param id    id
     * @param level 水平
     * @return {@link String}
     */
    @Log("删除权限资源")
    @RequiresPermissions("/deletePermission")
    @RequestMapping("/deletePermission")
    public String deletePermission(String id,String level){
        int i=0;
        if(level.equals("1")){
            i=permissionService.deletePermission(id);
        }
        if(level.equals("2")){
            i=permissionService.deletePermissionChild(id);
        }
        if(level.equals("3")){
            i=permissionService.deletePermissionButton(id);
        }
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state","404");
        }else{
            result.put("state","200");
        }
        return JSON.toJSONString(result);
    }

}
