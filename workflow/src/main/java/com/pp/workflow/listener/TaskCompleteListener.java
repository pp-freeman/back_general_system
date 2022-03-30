package com.pp.workflow.listener;

import com.pp.workflow.dao.TbEvectionMapper;
import com.pp.workflow.domain.TbEvection;
import com.pp.workflow.domain.TbEvectionTask;
import com.pp.workflow.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskCompleteListener implements TaskListener {


    @Override
    // 短信通知申请人流程通过
    public void notify(DelegateTask delegateTask) {
        TbEvectionMapper tbEvectionMapper = (TbEvectionMapper) SpringContextUtil.getBean("tbEvectionMapper");
        if (tbEvectionMapper != null) {
            // 获取processEngine
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            log.info("eventName=={}",delegateTask.getEventName());
            if (delegateTask.getEventName().equals("delete")) {
                delegateTask.getProcessDefinitionId();
                ProcessInstance processInstance = runtimeService
                        .createProcessInstanceQuery()
                        .processInstanceId(delegateTask.getProcessInstanceId())
                        .singleResult();
                if (processInstance != null) {
                    String businessKey = processInstance.getBusinessKey();
                    if (!StringUtils.isBlank(businessKey)) {
                        String id = businessKey.split(":")[1];
                        TbEvection evection = tbEvectionMapper.selectByPrimaryKey(Integer.valueOf(id));
                        log.info("短信通知:" + evection.getUserid());
                        if(evection.getState() != null) {
                            if(!evection.getState().equals("已拒绝")) {
                                evection.setState("已通过");
                            }
                        } else{
                            evection.setState("已通过");
                        }
                        tbEvectionMapper.updateByPrimaryKey(evection);
                    }
                }
            }
        }
    }
}
