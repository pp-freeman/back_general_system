package com.pp.workflow.Service;

import com.pp.workflow.dao.TbEvectionMapper;
import com.pp.workflow.domain.TbEvection;
import com.pp.workflow.domain.TbEvectionExample;
import com.pp.workflow.domain.TbEvectionTask;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ActFlowCommService {

    @Autowired
    TbEvectionMapper tbEvectionMapper;

    /**
     * 部署流程定义
     */
//    public void saveNewDeploy(FlowInfo flowInfo) {
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource(flowInfo.getFilepath()) // 添加bpmn资源
//                .name(flowInfo.getFlowkey())
//                .deploy();
////        4、输出部署信息
//        System.out.println("流程部署id：" + deployment.getId());
//        System.out.println("流程部署名称：" + deployment.getName());
//    }

    /**
     * 启动流程实例
     */
    public ProcessInstance startProcess(String formKey, TbEvection evection, String bussinessKey, Integer id, Map<String, Object> variables) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        variables.put("bussinessKey", bussinessKey);
        variables.put("evection", evection);
//		启动流程
        log.info("【启动流程】，formKey ：{},bussinessKey:{}", formKey, bussinessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(formKey, bussinessKey, variables);
//		流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("【启动流程】- 成功，processDefinitionId：{}", processDefinitionId);
        return processInstance;
    }

    /**
     * 查看个人任务列表
     */
    public List<Task> myTaskList(String userid, String processDefinitionKey) {
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();

        //查询组任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(userid)//根据候选人查询
                .list();
        return list;
    }

    /**
     * 查看个人任务信息
     */
    public List<TbEvectionTask> myTaskInfoList(String userid) {

        /**
         * 根据负责人id  查询任务
         */
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 查询任务
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser(userid)//根据候选人查询
                .list();

        List<TbEvectionTask> listmap = new ArrayList<>();
        for (Task task : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", task.getId());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("createTime", task.getCreateTime());
            ProcessInstance processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            if (processInstance != null) {
                String businessKey = processInstance.getBusinessKey();
                if (!StringUtils.isBlank(businessKey)) {
                    String type = businessKey.split(":")[0];
                    String id = businessKey.split(":")[1];
                    // 出差申请
                    TbEvectionTask tbEvectionTask = new TbEvectionTask();
                    tbEvectionTask.setTaskId(task.getId());
                    TbEvection evection = tbEvectionMapper.selectByPrimaryKey(Integer.valueOf(id));
                    evection.setType(type);
                    tbEvectionTask.setTbEvection(evection);
                    listmap.add(tbEvectionTask);
                }
            }

        }
        return listmap;
    }


    /**
     * 完成提交任务
     */
    public void completeProcess(String remark, String taskId, String userId, Integer id) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);
        log.info("userID:" + userId +"任务拾取成功" + "taskID:" + taskId);
        //任务Id 查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();


        if (task == null) {
            log.error("completeProcess - task is null!!");
            return;
        }


        //任务对象  获取流程实例Id
        String processInstanceId = task.getProcessInstanceId();

        //设置审批人的userId
        Authentication.setAuthenticatedUserId(userId);

        //添加记录
        taskService.addComment(taskId, processInstanceId, remark);
        log.info("-----------完成任务操作 开始----------");
        log.info("任务Id=" + taskId);
        log.info("负责人id=" + userId);
        log.info("流程实例id=" + processInstanceId);
        //完成办理
        taskService.complete(taskId);
        //修改业务状态
        TbEvection evection = tbEvectionMapper.selectByPrimaryKey(id);
        if (evection.getState() != null) {
            if (!evection.getState().equals("已通过")) {
                evection.setState(task.getName());
            }
        } else {
            evection.setState(task.getName());
        }
        tbEvectionMapper.updateByPrimaryKey(evection);
        log.info("-----------完成任务操作 结束----------");
    }


    /**
     * 拾取任务
     */
    public void claimProcess(String businessKey, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)//根据候选人查询
                .singleResult();
        taskService.claim(task.getId(), userId);
        log.info("userID:" + userId +"任务拾取成功" + "taskID:" + task.getId());
    }


    public void completeMyProcess(Integer id, String userId){
        // 回退任务
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey("evection:"+id)
                .taskAssignee(userId)
                .singleResult();
        //添加记录
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "回退");
        log.info("-----------完成任务操作 开始----------");
        log.info("任务Id=" + task.getId());
        log.info("负责人id=" + userId);
        log.info("流程实例id=" + task.getProcessInstanceId());
        //完成办理
        taskService.complete(task.getId());
        //修改业务状态
        TbEvection evection = tbEvectionMapper.selectByPrimaryKey(Integer.valueOf(id));
        evection.setState(task.getName());
        tbEvectionMapper.updateByPrimaryKey(evection);
        log.info("-----------完成任务操作 结束----------");
    }
    /**
     * 任务回退
     * @param taskId
     */
    public void taskRollback(String type,String taskId){
        // 获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ManagementService managementService = processEngine.getManagementService();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //根据要跳转的任务ID获取其任务
        List<HistoricTaskInstance> hisTaskList = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .list();
        for(HistoricTaskInstance historicTaskInstance : hisTaskList) {
            if(historicTaskInstance.getName().equals("创建出差申请") && !historicTaskInstance.getAssignee().equals("") && historicTaskInstance.getAssignee() != null){
                //进而获取流程实例
                ProcessInstance instance = runtimeService
                        .createProcessInstanceQuery()
                        .processInstanceId(historicTaskInstance.getProcessInstanceId())
                        .singleResult();
                //修改任务状态
                String businessKey = instance.getBusinessKey();
                String id = businessKey.split(":")[1];
                // 出差申请
                TbEvection evection = tbEvectionMapper.selectByPrimaryKey(Integer.valueOf(id));
                evection.setState("已拒绝");
                tbEvectionMapper.updateByPrimaryKey(evection);
                //取得流程定义
                ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());
                //获取历史任务的Activity
                ActivityImpl hisActivity = definition.findActivity(historicTaskInstance.getTaskDefinitionKey());
                //实现跳转
                managementService.executeCommand(new JumpCmd(instance.getId(), hisActivity.getId()));
                log.info("流程："+ instance.getProcessDefinitionId() + "回退成功！");
                claimProcess(businessKey, evection.getUserid());
                // 短信通知
                if(type.equals("rollback")) {
                    log.info("短信通知流程被拒绝" + evection.getUserid());
                }
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



//
//    /**
//     * 查询历史记录
//     *
//     * @param businessKey
//     */
//    public void searchHistory(String businessKey) {
//        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
//        if (CollectionUtils.isEmpty(list1)) {
//            return;
//        }
//        String processDefinitionId = list1.get(0).getProcessDefinitionId();
//        // 历史相关Service
//        List<HistoricActivityInstance> list = historyService
//                .createHistoricActivityInstanceQuery()
//                .processDefinitionId(processDefinitionId)
//                .orderByHistoricActivityInstanceStartTime()
//                .asc()
//                .list();
//        for (HistoricActivityInstance hiact : list) {
//            if (StringUtils.isBlank(hiact.getAssignee())) {
//                continue;
//            }
//            System.out.println("活动ID:" + hiact.getId());
//            System.out.println("流程实例ID:" + hiact.getProcessInstanceId());
//            User user = userService.findOneUserById(Long.valueOf(hiact.getAssignee()));
//            System.out.println("办理人ID：" + hiact.getAssignee());
//            System.out.println("办理人名字：" + user.getUsername());
//            System.out.println("开始时间：" + hiact.getStartTime());
//            System.out.println("结束时间：" + hiact.getEndTime());
//            System.out.println("==================================================================");
//        }
//    }
}
