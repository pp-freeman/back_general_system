package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.IRoleService;
import com.pp.auth_server.service.IShiroService;
import com.pp.auth_server.service.IUserInfoService;
import com.pp.auth_server.service.IUserService;
import com.pp.auth_server.utils.MD5Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制器
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 用户信息服务
     */
    @Autowired
    IUserInfoService userInfoService;

    /**
     * 用户服务
     */
    @Autowired
    IUserService userService;

    /**
     * 角色服务
     */
    @Autowired
    IRoleService roleService;

    @Autowired
    IShiroService shiroService;

    /**
     * 得到用户信息列表
     *
     * @param queryInfo 查询信息
     * @return {@link String}
     */
    @Log("查看用户信息列表")
    @RequiresPermissions({"/userList"})
    @RequestMapping("/userInfoList")
    public String getUserInfoList(@RequestBody QueryInfo queryInfo){
        List<UserInfo> userInfos = userInfoService.getUserInfoList(queryInfo.getPageNum(),queryInfo.getPagesize(),queryInfo.getUsername(),queryInfo.getDepartment(),queryInfo.getNumber());
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("number",userInfoService.getUserInfoCount(queryInfo.getUsername(),queryInfo.getDepartment(),queryInfo.getNumber()));
        result.put("userInfos",userInfos);
        result.put("state",200);
        String res = JSON.toJSONString(result);
        return res;
    }

    /**
     * 获取用户列表
     * 得到用户列表
     *
     * @param username 用户名
     * @param roleName 角色名
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @return {@link String}
     */
    @Log("查看用户列表")
    @RequiresPermissions("/userMan")
    @RequestMapping("/userList")
    public String getUserList(String username,String roleName,int pageNum,int pageSize){
        int number = userService.getAllUserCount(username,roleName);
        List<User> users = userService.getAllUser(username,roleName,(pageNum-1)*pageSize,pageSize);
        for(int i=0;i<users.size();i++){
            if(users.get(i).getRoles().size()!=0) {
                for (int j = 0; j < users.get(i).getRoles().size(); j++) {
                    users.get(i).setRole(users.get(i).getRoles().get(j).getRoleName() + ";");
                }
                users.get(i).setRole(users.get(i).getRole().substring(0, users.get(i).getRole().length() - 1));
            }
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("number",number);
        result.put("userInfos",users);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 更新用户状态
     * 修改用户状态
     *
     * @param id    id
     * @param state 状态
     * @return {@link String}
     */
    @Log("修改用户状态")
    @RequiresPermissions("/userMan")
    @RequestMapping("/updateState")
    public String updateUserState(String id,int state){
        if(userService.updateUserState(id,state)>0){
            return "success";
        }else {
            return "false";
        }


    }

    /**
     * 添加用户
     *
     * @param user 用户
     * @return {@link String}
     */
    @Log("添加用户")
    @RequiresPermissions("/addUser")
    @RequestMapping("/addUser")
    public String addUser(@RequestBody User user){
        if(shiroService.getUserByName(user.getUsername())!=null){
            return "repetition";
        }
        user.setId(String.valueOf(UUID.randomUUID()));
        int i = userService.addUser(user.getId(),user.getUsername(), MD5Utils.generatePassword(user.getPassword()));
        return i>0?"success":"error";
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return {@link String}
     */
    @Log("删除用户")
    @RequiresPermissions("/delUser")
    @RequestMapping("/deleteUser")
    public String deleteUser(String id){
        int i= userService.deleteUser(id);
        roleService.deleteUserRole(id);
        return i>0?"success":"error";
    }

    /**
     * 得到更新用户
     * 得到修改用户原信息
     *
     * @param id id
     * @return {@link String}
     */
    @RequestMapping("/getUpdateUser")
    public String getUpdateUser(String id){
        User user = userService.getUpdateUser(id);
        return JSON.toJSONString(user);
    }

    /**
     * 删除用户
     * 修改用户
     *
     * @param user 用户
     * @return {@link String}
     */
    @Log("修改用户")
    @RequestMapping("/editUser")
    public String deleteUser(@RequestBody User user){
        user.setPassword(MD5Utils.generatePassword(user.getPassword()));
        int i= userService.editUser(user);
        return i>0?"success":"error";
    }

    /**
     * 得到学生信息
     * 获得学生个人信息
     *
     * @param id id
     * @return {@link String}
     */
    @Log("查看详细信息")
    @RequestMapping("/getStudentInfo")
    public String getStudentInfo(String id){
        StudentInfo studentInfo = userInfoService.getStudentInfoById(id);
        Map<String,Object> result = new HashMap<>();
        if(studentInfo==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("student",studentInfo);
        return JSON.toJSONString(result);
    }

    /**
     * 得到老师的信息
     * 得到老师个人信息
     *
     * @param id id
     * @return {@link String}
     */
    @Log("查看详细信息")
    @RequestMapping("/getTeacherInfo")
    public String getTeacherInfo(String id){
        TeacherInfo teacherInfo = userInfoService.getTeacherInfoById(id);
        Map<String,Object> result = new HashMap<>();
        if(teacherInfo==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("teacher",teacherInfo);

        return JSON.toJSONString(result);
    }

    /**
     * 通过id获取成绩
     * 根据id获得班级
     *
     * @param id id
     * @return {@link String}
     */
    @RequestMapping("/getGradeById")
    public String getGradeById(String id){
        Map<String,Object> result = new HashMap<>();
        result.put("grade",userInfoService.getGradeById(id));
        return JSON.toJSONString(result);
    }

    /**
     * 修改学生信息
     * 修改学生详细信息
     *
     * @param sphone        sphone
     * @param scontact      scontact
     * @param scontactphone scontactphone
     * @param id            id
     * @return {@link String}
     */
    @Log("修改详细信息")
    @RequiresPermissions("/updateUserInfoBtn")
    @RequestMapping("/editStudentInfo")
    public String editStudentInfo(String sphone,String scontact,String scontactphone,String id){
        int i = userInfoService.editStudentInfo(sphone,scontact,scontactphone,id);
        Map<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 编辑老师信息
     * 修改老师信息
     *
     * @param tphone        tphone
     * @param tcontact      tcontact
     * @param tcontactphone tcontactphone
     * @param id            id
     * @return {@link String}
     */
    @Log("修改详细信息")
    @RequiresPermissions("/updateUserInfoBtn")
    @RequestMapping("/editTeacherInfo")
    public String editTeacherInfo(String tphone,String tcontact,String tcontactphone,String id){
        int i = userInfoService.editTeacherInfo(tphone,tcontact,tcontactphone,id);
        Map<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }
}
