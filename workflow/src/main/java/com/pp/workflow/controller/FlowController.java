package com.pp.workflow.controller;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.pp.workflow.Service.ActFlowCommService;
import com.pp.workflow.common.CommonResult;
import com.pp.workflow.dao.TbEvectionMapper;
import com.pp.workflow.dao.TbFlowMapper;
import com.pp.workflow.domain.*;
import com.pp.workflow.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程管理
 */
@RestController
@RequestMapping("/flow")
@Slf4j
public class FlowController {

    @Autowired
    ActFlowCommService actFlowCommService;

    @Autowired
    TbFlowMapper tbFlowMapper;

    @Autowired
    TbEvectionMapper tbEvectionMapper;

    /**
     * 查询所有流程
     *
     * @return
     */
    @PostMapping("/findAll")
    public CommonResult<List<TbFlow>> findAllFlow(@RequestBody com.pp.workflow.domain.PageInfo pageInfo) {
        PageHelper.startPage(pageInfo.getPagenum(), pageInfo.getPagesize());
        TbFlowExample example = new TbFlowExample();
        List<TbFlow> tbFlows = tbFlowMapper.selectByExample(example);
        com.github.pagehelper.PageInfo<TbFlow> result = new com.github.pagehelper.PageInfo<>(tbFlows);
        return new CommonResult<>(result.getList(), tbFlowMapper.countByExample(example));
    }

    /**
     * 删除流程
     *
     * @param devKey
     * @return
     */
    @GetMapping("/deleteFlow")
    public CommonResult<String> deleteFlow(@RequestParam String devKey) {
        CommonResult<String> result = null;
        try {
            TbFlowExample example = new TbFlowExample();
            example.createCriteria().andFlowkeyEqualTo(devKey);
            tbFlowMapper.deleteByExample(example);
            // 删除activiti流程部署
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 获取所有流程定义
            RuntimeService runtimeService = processEngine.getRuntimeService();
            List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                    .processDefinitionKey(devKey)
                    .list();
            for (ProcessInstance instance : list) {
                tbEvectionMapper.deleteByPrimaryKey(Integer.valueOf(instance.getBusinessKey().split(":")[1]));
            }
            ProcessDefinition processDefinition = processEngine.getRepositoryService()
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(devKey)
                    .singleResult();
            if (processDefinition != null) {
                // 通过流程引擎获取repositoryService
                RepositoryService repositoryService = processEngine
                        .getRepositoryService();
                //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
                log.info("----流程删除成功！！！----");
            }
            result = new CommonResult<>("success");
        } catch (Exception e) {
            result = new CommonResult<>("error", 400);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 下载流程文件
     *
     * @param response
     * @param flowKey
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/downloadFlow")
    public String downloadFlow(HttpServletResponse response, @RequestParam(value = "flowKey") String flowKey) throws UnsupportedEncodingException {
        String rootPath = "C://Users//pengpan//IdeaProjects//back_general_system//workflow//src//main//resources//bpmn//";
        File file = new File(rootPath + flowKey + ".xml");
        String fileName = flowKey + ".xml";
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", "" + file.length());
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("文件下载完成！！");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 得到需要修改的流程定义文件
     *
     * @param flowKey
     * @return
     */
    @GetMapping("/getUploadFlow")
    public CommonResult<String> getUploadFlow(@RequestParam(value = "flowKey") String flowKey) {
        CommonResult<String> result = null;
        try {
            String rootPath = "C://Users//pengpan//IdeaProjects//back_general_system//workflow//src//main//resources//bpmn//";
            File file = new File(rootPath + flowKey + ".xml");
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                DOMSource domSource = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult result1 = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result1);

                // 将转换过的xml的String 样式打印到控制台
                System.out.println(writer.toString());
                result = new CommonResult<>(writer.toString());
            }
        } catch (Exception e) {
            result = new CommonResult<>("error", 400);
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 查询个人任务
     *
     * @param pageInfo
     * @return
     */
    @PostMapping("/findTaskList")
    public CommonResult<List<TbEvectionTask>> findTaskList(@RequestBody PageInfo pageInfo) {
        List<TbEvectionTask> list = actFlowCommService.myTaskInfoList(pageInfo.getUserId());
        return new CommonResult<>(PageUtils.startPage(list, pageInfo.getPagenum(), pageInfo.getPagesize()), list.size());
    }

    /**
     * '
     * 完成任务
     *
     * @param taskId
     * @param userId
     * @param id
     * @return
     */
    @GetMapping("/completeTask")
    public CommonResult<String> completeTask(@RequestParam String taskId, @RequestParam String userId, @RequestParam String id) {
        try {
            actFlowCommService.completeProcess("同意", taskId, userId, Integer.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>("error", 400);
        }
        return new CommonResult<>("success");
    }

    /**
     * '
     * 回滚任务
     *
     * @param taskId
     * @param userId
     * @param id
     * @return
     */
    @GetMapping("/rollBackTask")
    public CommonResult<String> rollBackTask(@RequestParam String taskId, @RequestParam String userId) {
        try {
            // 回退任务
            actFlowCommService.taskRollback("rollback", taskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>("error", 400);
        }
        return new CommonResult<>("success");
    }


    /**
     * '
     * 申请人回退任务
     *
     * @param id
     * @param userId
     * @return
     */
    @GetMapping("/appRollBackTask")
    public CommonResult<String> appRollBackTask(@RequestParam String id, @RequestParam String userId) {
        try {
            // 回退任务
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery()
                    .processInstanceBusinessKey("evection:" + id)
                    .singleResult();
            actFlowCommService.taskRollback("appRollback", task.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>("error", 400);
        }
        return new CommonResult<>("success");
    }

//
//    /**
//     * 查询
//     * @return
//     */
//    @GetMapping("/flow/findFlowTask/{id}")
//    public Map<String,Object> findFlowTask(@PathVariable(name = "id")Long id){
//        String businessKey = "evection:"+id;
//        actFlowCommService.searchHistory(businessKey);
//        return null;
//    }


    @RequestMapping("/uploadWorkFlowFile")
    public String uploadWorkFlowFile(HttpServletRequest request,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     String filename) throws SocketException, IOException {
        TbFlow tbFlow = new TbFlow();
        Map<String, String> result = new HashMap<>();
        String rootPath = "C://Users//pengpan//IdeaProjects//back_general_system//workflow//src//main//resources//bpmn//";
        String filePath = rootPath + filename;
        File fileDir1 = new File(rootPath + filename.split("[.]")[0] + ".xml");
        File fileDir = new File(filePath);
        if (fileDir.exists()) {
            fileDir.delete();
        }
        if (fileDir1.exists()) {
            fileDir1.delete();
        }
        // 文件保存
        FileUtils.copyInputStreamToFile(file.getInputStream(), fileDir);
        FileUtils.copyInputStreamToFile(file.getInputStream(), fileDir1); //png
        // 删除原流程

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessDefinition processDefinition = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(filename.split("[.]")[0])
                .singleResult();
        if (processDefinition != null) {
            // 通过流程引擎获取repositoryService
            RepositoryService repositoryService = processEngine
                    .getRepositoryService();
            //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
            log.info("----流程删除成功！！！----");
        }
        // 重新部署新流程
        RepositoryService repositoryService = processEngine.getRepositoryService();
        File file1 = new File("C://Users//pengpan//IdeaProjects//back_general_system//workflow//src//main//resources//bpmn//" + filename);
        try {
            InputStream inputStream = new FileInputStream(file1);
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(filename, inputStream)
                    .name(filename.split("[.]")[0] + "流程")
                    .deploy();
            tbFlow.setFlowname(filename.split("[.]")[0] + "流程");
            tbFlow.setFilepath("/bpmn/" + filename);
            tbFlow.setFlowkey(filename.split("[.]")[0]);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String dateName = df.format(calendar.getTime());
            tbFlow.setCreatetime(dateName);
            tbFlow.setState("已部署");
            TbFlowExample example = new TbFlowExample();
            example.createCriteria().andFlowkeyEqualTo(filename.split("[.]")[0]);
            if (tbFlowMapper.selectByExample(example).size() != 0) {
                tbFlowMapper.deleteByExample(example);
            }
            tbFlowMapper.insert(tbFlow);
            log.info("----流程部署成功!!!----");
            log.info("id: " + deployment.getId());
            log.info("name: " + deployment.getName());
            result.put("code", "200");
        } catch (FileNotFoundException e) {
            result.put("code", "400");
            e.printStackTrace();
        }
        //前端可以通过状态码，判断文件是否上传成功
        return JSON.toJSONString(result);
    }
}
