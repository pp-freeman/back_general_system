package com.pp.workflow;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivitiDemo {


    @Test
    public void testDeployment() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        File file = new File("C://Users//pengpan//IdeaProjects//back_general_system//workflow//src//main//resources//bpmn//evection5.bpmn");
        try {
            InputStream inputStream = new FileInputStream(file);
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream("evection5.bpmn",inputStream)
                    .name("evection流程")
                    .deploy();
            System.out.println("id:" + deployment.getId());
            System.out.println("name:" + deployment.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
