package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.common.CommonResult;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.impl.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/workflow")
public class WorkFlowController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoleService roleService;

    @Value("${service-url.workflow}")
    private String userServiceUrl;

    @PostMapping("/addEvection")
    @RequiresPermissions({"/evection"})
    public CommonResult addEvection(@RequestBody TbEvection tbEvection) {
        EvectionFormEntity evectionFormEntity = new EvectionFormEntity();
        evectionFormEntity.setTbEvection(tbEvection);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        evectionFormEntity.setUserId(user.getId());
        evectionFormEntity.setVariables(roleService.getAllRoleAndUser());
        System.out.println(JSON.toJSONString(evectionFormEntity));
        return restTemplate.postForObject(userServiceUrl + "/evection/add", evectionFormEntity, CommonResult.class);
    }

    @PostMapping("/editEvection")
    @RequiresPermissions({"/evection"})
    public CommonResult editEvection(@RequestBody TbEvection tbEvection) {
        EvectionFormEntity evectionFormEntity = new EvectionFormEntity();
        evectionFormEntity.setTbEvection(tbEvection);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        evectionFormEntity.setUserId(user.getId());
        return restTemplate.postForObject(userServiceUrl + "/evection/edit", evectionFormEntity, CommonResult.class);
    }


    @PostMapping("/getEvectionList")
    @RequiresPermissions({"/evection"})
    public CommonResult getEvectionList(@RequestBody PageInfo pageInfo) {
        return restTemplate.postForObject(userServiceUrl + "/evection/findAll",  pageInfo, CommonResult.class);
    }

    /**
     * ????????????????????????
     * @param pageInfo
     * @return
     */
    @PostMapping("/getFlowList")
    @RequiresPermissions({"/evection"})
    public CommonResult getFlowList(@RequestBody PageInfo pageInfo) {
        return restTemplate.postForObject(userServiceUrl + "/flow/findTaskList",  pageInfo, CommonResult.class);
    }

    /**
     * ??????????????????
     * @param taskId
     * @param id
     * @return
     */
    @GetMapping("/agreeEvection")
    @RequiresPermissions({"/evection"})
    public CommonResult agreeEvection(@RequestParam String taskId, String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return restTemplate.getForObject(userServiceUrl + "/flow/completeTask?taskId={1}&userId={2}&id={3}", CommonResult.class, taskId, user.getId(), id);
    }


    /**
     * ??????????????????
     * @param taskId
     * @return
     */
    @GetMapping("/rollBackEvection")
    @RequiresPermissions({"/evection"})
    public CommonResult rollBackEvection(@RequestParam String taskId) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return restTemplate.getForObject(userServiceUrl + "/flow/rollBackTask?taskId={1}&userId={2}", CommonResult.class, taskId, user.getId());
    }

    /**
     * ???????????????????????????
     * @param taskId
     * @return
     */
    @GetMapping("/appRollBackEvection")
    @RequiresPermissions({"/evection"})
    public CommonResult appRollBackEvection(@RequestParam String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return restTemplate.getForObject(userServiceUrl + "/flow/appRollBackTask?id={1}&userId={2}", CommonResult.class, id, user.getId());
    }

    /**
     * ????????????????????????
     * @return
     */
    @PostMapping("/findAllFlow")
    @RequiresPermissions({"/evection"})
    public CommonResult findAllFlow(@RequestBody PageInfo pageInfo) {
        return restTemplate.postForObject(userServiceUrl + "/flow/findAll", pageInfo, CommonResult.class);
    }

    /**
     * ????????????????????????
     * @return
     */
    @GetMapping("/deleteFlow")
    @RequiresPermissions({"/evection"})
    public CommonResult deleteFlow(@RequestParam(value = "devKey") String devKey) {
        return restTemplate.getForObject(userServiceUrl + "/flow/deleteFlow?devKey={1}", CommonResult.class, devKey);
    }

    /**
     * ???????????????????????????????????????
     * @param flowKey
     * @return
     */
    @GetMapping("/getUploadFlow")
    @RequiresPermissions({"/evection"})
    public CommonResult getUploadFlow(@RequestParam(value = "flowKey") String flowKey) {
        return restTemplate.getForObject(userServiceUrl + "/flow/getUploadFlow?flowKey={1}", CommonResult.class, flowKey);
    }

}
