package com.pp.workflow;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestStartProcess {
    @Test
    public void startProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();


        HashMap<String, Object> assigneeMap = new HashMap<>();
        assigneeMap.put("users", "lisa,test1");
        assigneeMap.put("departments", "test2");
        assigneeMap.put("finances", "test3");
        assigneeMap.put("generals", "test4");
        assigneeMap.put("num", "5");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1",assigneeMap);

        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id:" + processInstance.getId());
        System.out.println("当前活动id:" + processInstance.getActivityId());
    }

    @Test
    public void deleteProcess() {
        String deploymentId = "255001";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId,true);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设
    }


    @Test
    public void findGroupTaskList() {
        // 流程定义key
        String processDefinitionKey = "Process_1";
        // 任务候选人
        String candidateUser = "lisa";
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        //查询组任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(candidateUser)//根据候选人查询
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }


    @Test
    public void taskRollback(){
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ManagementService managementService = processEngine.getManagementService();
        //根据要跳转的任务ID获取其任务
        List<HistoricTaskInstance> hisTaskList = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId("140001")
                .list();
        for(HistoricTaskInstance historicTaskInstance : hisTaskList) {
            if(historicTaskInstance.getName().equals("创建出差申请")){
                //进而获取流程实例
                ProcessInstance instance = runtimeService
                        .createProcessInstanceQuery()
                        .processInstanceId(historicTaskInstance.getProcessInstanceId())
                        .singleResult();
                //取得流程定义
                ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());
                //获取历史任务的Activity
                ActivityImpl hisActivity = definition.findActivity(historicTaskInstance.getTaskDefinitionKey());
                //实现跳转
                managementService.executeCommand(new JumpCmd(instance.getId(), hisActivity.getId()));
            }
        }

    }

    public class JumpCmd implements Command<ExecutionEntity> {

        private String processInstanceId;
        private String activityId;
        public static final String REASION_DELETE = "deleted";

        public JumpCmd(String processInstanceId, String activityId) {
            this.processInstanceId = processInstanceId;
            this.activityId = activityId;
        }


        public ExecutionEntity execute(CommandContext commandContext) {
            ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);

            executionEntity.destroyScope(REASION_DELETE);
            ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
            ActivityImpl activity = processDefinition.findActivity(activityId);
            executionEntity.executeActivity(activity);

            return executionEntity;
        }

    }

    @Test
    public void testHistory() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey("evection:48")
                .singleResult();
        // 历史相关Service
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        String result = "";
        int num = 0;
        for (HistoricActivityInstance hiact : list) {
            String row = "";
            if (num == list.size()) {
                if (hiact.getEndTime() != null) {
                    row += hiact.getActivityName() + "  " + "已通过" + "  " + hiact.getAssignee() + " " + hiact.getEndTime() + "\n";
                } else {
                    row += hiact.getActivityName() +"  " +"待审核" + "\n";
                }
            } else {
                if (hiact.getEndTime() != null) {
                    row += hiact.getActivityName() + "  " + "已通过" + "  " + hiact.getAssignee() + " " + hiact.getEndTime() + "\n";
                } else {
                    row += hiact.getActivityName() +"  " +"回退" + "\n";
                }
            }
            num ++;
            result += row;
        }
        System.out.println(result);
    }

}
