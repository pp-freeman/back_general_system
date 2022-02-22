package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.LogDO;
import com.pp.auth_server.service.ISysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/log")
public class SysLogController {

    @Autowired
    ISysLogService sysLogService;

    /**
     * 得到日志列表
     * @param pageNum
     * @param pageSize
     * @param userId
     * @param operation
     * @return
     */
    @Log("查看日志列表")
    @RequiresPermissions("/logManage")
    @RequestMapping("/getLogList")
    public String getLogList(int pageNum,int pageSize,String userId,String operation){
        List<LogDO> logDOS = null;
        int number = 0;
        if (operation.equals("登录")||operation.equals("")||operation.equals("异常")){
             logDOS = sysLogService.getSysLogList((pageNum-1)*pageSize,pageSize,userId,operation);
             number = sysLogService.getSysLogListCount(userId,operation);
        }else {
            logDOS = sysLogService.getOpSysLog((pageNum-1)*pageSize,pageSize,userId,operation);
            number = sysLogService.getOpSysLogCount(userId,operation);
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(logDOS==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("logList",logDOS);
        result.put("number",number);
        return JSON.toJSONString(result);
    }

    /**
     * 删除日志
     * @param logDO
     * @return
     */
    @Log("删除日志")
    @RequiresPermissions("/deleteOneLog")
    @RequestMapping("/deleteSysLog")
    public String deleteSysLog(@RequestBody LogDO logDO){
        int i = sysLogService.deleteLogById(logDO);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除所有日志
     * @param logDOS
     * @return
     */
    @Log("删除所选日志")
    @RequiresPermissions("/deleteAllLog")
    @RequestMapping("/deleteAllSysLog")
    public String deleteAllSysLog(@RequestBody List<LogDO> logDOS){
        int i = 1;
        int j;
        for(LogDO log : logDOS){
            j = sysLogService.deleteLogById(log);
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("state",200);
        return JSON.toJSONString(result);
    }

}
