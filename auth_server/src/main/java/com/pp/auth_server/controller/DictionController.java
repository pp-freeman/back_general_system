package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.Semester;
import com.pp.auth_server.service.IDictionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

/**
 * 措辞控制器
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/dic")
public class DictionController {

    @Autowired
    IDictionService dictionService;

    /**
     * 增加字典项
     * @param semester
     * @return
     */
    @Log("添加字典项")
    @RequiresPermissions("/addDiction")
    @RequestMapping("/addDiction")
    public String addDiction(@RequestBody Semester semester){
        HashMap<String,Object> result = new HashMap<>();
        Semester semester1 = dictionService.getDictionByAll(semester);
        if(semester1!=null){
            //字典项已经存在
            result.put("state",300);
            return JSON.toJSONString(result);
        }
        semester.setId(UUID.randomUUID().toString());
        int i = dictionService.addDic(semester);

        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除字典项
     * @param semester
     * @return
     */
    @Log("删除字典项")
    @RequiresPermissions("/deleteDiction")
    @RequestMapping("/deleteDictin")
    public String deleteDictin(@RequestBody Semester semester){
        int i = dictionService.deleteDiction(semester);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else {
            result.put("state", 200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到修改字典
     * @param semester
     * @return
     */
    @RequiresPermissions("/editDiction")
    @RequestMapping("/getUpdateDic")
    public String getUpdateDic(@RequestBody Semester semester){
        Semester semester1 = dictionService.getUpdateDic(semester);
        HashMap<String,Object> result = new HashMap<>();
        result.put("diction",semester1);
        return JSON.toJSONString(result);
    }

    /**
     * 修改字典项
     * @param semester
     * @return
     */
    @Log("修改字典项")
    @RequiresPermissions("/editDiction")
    @RequestMapping("/editDiction")
    public String editDiction(@RequestBody Semester semester){
        int i = dictionService.editDiction(semester);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else {
            result.put("state", 200);
        }
        return JSON.toJSONString(result);
    }
}
