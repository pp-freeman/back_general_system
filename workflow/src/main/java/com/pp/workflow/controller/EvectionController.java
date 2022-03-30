package com.pp.workflow.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pp.workflow.Service.ActFlowCommService;
import com.pp.workflow.common.CommonResult;
import com.pp.workflow.dao.TbEvectionMapper;
import com.pp.workflow.domain.FormEntity;
import com.pp.workflow.domain.TbEvection;
import com.pp.workflow.domain.TbEvectionExample;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/evection")
public class EvectionController {

    @Autowired
    TbEvectionMapper tbEvectionMapper;

    @Autowired
    ActFlowCommService actFlowCommService;
    /**
     * 查询本人的出差申请
     * @param pageInfo
     * @return
     */
    @PostMapping("/findAll")
    public CommonResult<List<TbEvection>> findAll(@RequestBody com.pp.workflow.domain.PageInfo pageInfo){
        //设置分页信息
        PageHelper.startPage(pageInfo.getPagenum(), pageInfo.getPagesize());
        TbEvectionExample evectionExample = new TbEvectionExample();
        evectionExample.createCriteria().andUseridEqualTo(pageInfo.getUserId());
        List<TbEvection> list = tbEvectionMapper.selectByExample(evectionExample);
        //取分页信息
        PageInfo<TbEvection> result = new PageInfo<TbEvection>(list);
        return new CommonResult<>(result.getList(), tbEvectionMapper.countByExample(evectionExample));
    }

    /**
     * 新增出差申请
     * @param formEntity
     */
    @PostMapping("/add")
    public CommonResult<String> addEvection(@RequestBody FormEntity formEntity){
        TbEvection evection = formEntity.getTbEvection();
        evection.setUserid(formEntity.getUserId());
        int code = tbEvectionMapper.insert(evection);
        if(code != 0){
            Integer id = evection.getId();
            String formKey = "evection";
            String beanName = formKey + "Service";
            //使用流程变量设置字符串（格式 ： Evection:Id 的形式）
            //使用正在执行对象表中的一个字段BUSINESS_KEY(Activiti提供的一个字段)，让启动的流程（流程实例）关联业务
            String bussinessKey = formKey+":" +id;
            ProcessInstance processInstance = actFlowCommService.startProcess(formKey, formEntity.getTbEvection(), bussinessKey, id, formEntity.getVariables());
            //		流程实例ID
            String processInstanceId = processInstance.getProcessInstanceId();
            log.info("流程实例ID is {}",processInstanceId);
            List<Task> taskList = actFlowCommService.myTaskList(formEntity.getUserId(), formKey);
            if(taskList.size() != 0) {
                for (Task task : taskList) {
                    if(task.getProcessInstanceId().toString().equals(processInstanceId)) {
                        log.info("流程实例ID是 {}",task.getProcessDefinitionId().toString());
                        log.info("任务Id是 is {}",task.getId().toString());
                        actFlowCommService.completeProcess("同意",task.getId().toString(), formEntity.getUserId(), id);
                    }
                }
            }
        }
        return new CommonResult<>("success");
    }


    @PostMapping("/edit")
    public CommonResult<String> editEvection(@RequestBody FormEntity formEntity){
        try {
            tbEvectionMapper.updateByPrimaryKey(formEntity.getTbEvection());
            actFlowCommService.completeMyProcess(formEntity.getTbEvection().getId(), formEntity.getUserId());
        }catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>("error", 400);
        }
        return new CommonResult<>("success");
    }
}
