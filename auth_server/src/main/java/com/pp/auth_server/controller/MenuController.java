package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.domain.Menu;
import com.pp.auth_server.domain.PermissionChild;
import com.pp.auth_server.service.IMenuService;
import com.pp.auth_server.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
public class MenuController {

    @Autowired
    IMenuService menuService;

    @Autowired
    IRoleService roleService;

    /**
     *得到所有菜单
     * @param id id
     * @return {@link String}
     */
    @RequestMapping("/menus")
    public String getAllMenus(String id){
        HashMap<String,Object> data = new HashMap<>();
        int status = 404; //错误为404，成功为200
        List<Menu> list = menuService.findMenu(id);
        if (list.size()!=0){
            data.put("menus",list);
            data.put("flag",200);
        }else {
            data.put("flag",404);
        }
        List<PermissionChild> permissionChildren = roleService.getPermissionChildByUserId(id);
        for(PermissionChild p : permissionChildren){
            p.setValue(p.getPermissionName());
        }
        data.put("permissionChild",permissionChildren);
        List<String> list1 = menuService.getButtonByUser(id);
        data.put("button",list1);
        String s = JSON.toJSONString(data);
        return s;
    }

    /**
     * 得到用户的按钮级权限
     * @param id
     * @return
     */
    @RequestMapping("/getAllButtons")
    public String getAllbuttons(String id){
        HashMap<String,Object> data = new HashMap<>();
        List<String> list = menuService.getButtonByUser(id);
        data.put("button",list);
        return JSON.toJSONString(data);
    }

}
