package com.pp.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestCteateTable {
    @Test
    public void testCreateTable() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }


    @Test
    public void testCreateTable1() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //设置流程定义的key，候选用户
        String key = "evection";
        String candidateUser = "pan";
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)//设置候选用户
                .list();
        //输出
        for (Task task : list){
            System.out.println(task.getProcessDefinitionId());
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());//为null只是任务候选人，而非执行人
        }

    }
}
