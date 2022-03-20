package com.pp.code_generator.generator;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.code_generator.common.CommonResult;
import com.pp.code_generator.utils.RedisUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/code")
public class GeneratorController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RedisUtil redisUtil;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value(value = "${spring.datasource.url}")
    private String url;

    @Value(value = "${spring.datasource.username}")
    private String userName;

    @Value(value = "${spring.datasource.password}")
    private String password;


    @GetMapping(value = "/addField")
    public CommonResult<String> addFile(@RequestParam String json) {
        String result = "";
        if (redisUtil.get("addFiledString") != null) {
            String oldJson = redisUtil.get("addFiledString").toString();
            List<EntityField> list = JSON.parseArray(oldJson, EntityField.class);
            EntityField entityField = JSON.parseObject(json, EntityField.class);
            list.add(entityField);
            result = JSON.toJSONString(list);
            redisUtil.set("addFiledString", result);
            redisUtil.expire("addFiledString", 2*60);
        } else {
            EntityField entityField = JSON.parseObject(json, EntityField.class);
            List<EntityField> list = new ArrayList<>();
            list.add(entityField);
            result = JSON.toJSONString(list);
            redisUtil.set("addFiledString", result);
        }
        return new CommonResult<>(result);
    }

    @GetMapping(value = "/getFieldList")
    public CommonResult<List<EntityField>> getFieldList() {
        String result = "";
        if (redisUtil.get("addFiledString") != null) {
            result = redisUtil.get("addFiledString").toString();
        } else {
            return new CommonResult<>();
        }
        List<EntityField> entityFields = JSON.parseArray(result, EntityField.class);
        return new CommonResult<>(entityFields);
    }

    @GetMapping(value = "/delFieldList")
    public CommonResult<String> delFieldList() {
        if (redisUtil.get("addFiledString") != null) {
            redisUtil.del("addFiledString");
        }
        return new CommonResult<>("success");
    }


    @PostMapping(value = "/generate-code")
    public CommonResult<String> generator(@Valid @RequestBody FormEntity formEntity) throws JsonProcessingException, ClassNotFoundException {

        Iterator<EntityField> iterator = formEntity.getEntityFields().iterator();
        while (iterator.hasNext()) {
            EntityField entityField = iterator.next();
            if (entityField.getFieldName() == null) {
                iterator.remove();
            }
        }

        Map<String, Object> map = objectMapper.readValue(objectMapper.writeValueAsString(formEntity), Map.class);

        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));

        String resultBaseDir = "code_generator/src/main/java/com/pp/code_generator/out/";
        String templateBaseDir = "code_generator/src/main/resources/templates/generator/";
        String sqlBaseDir = "code_generator/src/main/resources/sql";

        // entity
        String entityResultPath = resultBaseDir + "mybatis/entity/" + (String) formEntity.getEntityName() + ".java";
        String entityTemplatePath = templateBaseDir + "Entity.ftl";
        generatorCode(entityResultPath, entityTemplatePath, config, map);

        // mapper
        String mapperResultPath = resultBaseDir + "mybatis/mapper/" + (String) formEntity.getEntityName() + "Mapper.java";
        String mapperTemplatePath = templateBaseDir + "Mapper.ftl";
        generatorCode(mapperResultPath, mapperTemplatePath, config, map);

        // createInDTO
        String createInDTOResultPath = resultBaseDir + "controller/dto/" + (String) formEntity.getEntityName() + "CreateInDTO.java";
        String createInDTOTemplatePath = templateBaseDir + "InDTO_Create.ftl";
        generatorCode(createInDTOResultPath, createInDTOTemplatePath, config, map);

        // updateInDTO
        String updateInDTOResultPath = resultBaseDir + "controller/dto/" + (String) formEntity.getEntityName() + "UpdateInDTO.java";
        String updateInDTOTemplatePath = templateBaseDir + "InDTO_Update.ftl";
        generatorCode(updateInDTOResultPath, updateInDTOTemplatePath, config, map);

        // queryInDTO
        String queryInDTOResultPath = resultBaseDir + "controller/dto/" + (String) formEntity.getEntityName() + "QueryInDTO.java";
        String queryInDTOTemplatePath = templateBaseDir + "InDTO_Query.ftl";
        generatorCode(queryInDTOResultPath, queryInDTOTemplatePath, config, map);

        // controller
        String controllerResultPath = resultBaseDir + "controller/" + (String) formEntity.getEntityName() + "Controller.java";
        String controllerTemplatePath = templateBaseDir + "Controller.ftl";
        generatorCode(controllerResultPath, controllerTemplatePath, config, map);

        // createInBO
        String createInBOResultPath = resultBaseDir + "service/bo/" + (String) formEntity.getEntityName() + "CreateInBO.java";
        String createInBOTemplatePath = templateBaseDir + "InBO_Create.ftl";
        generatorCode(createInBOResultPath, createInBOTemplatePath, config, map);

        // updateInBO
        String updateInBOResultPath = resultBaseDir + "service/bo/" + (String) formEntity.getEntityName() + "UpdateInBO.java";
        String updateInBOTemplatePath = templateBaseDir + "InBO_Update.ftl";
        generatorCode(updateInBOResultPath, updateInBOTemplatePath, config, map);

        // queryInBO
        String queryInBOResultPath = resultBaseDir + "service/bo/" + (String) formEntity.getEntityName() + "QueryInBO.java";
        String queryInBOTemplatePath = templateBaseDir + "InBO_Query.ftl";
        generatorCode(queryInBOResultPath, queryInBOTemplatePath, config, map);

        // service
        String serviceResultPath = resultBaseDir + "service/" + (String) formEntity.getEntityName() + "Service.java";
        String serviceTemplatePath = templateBaseDir + "Service.ftl";
        generatorCode(serviceResultPath, serviceTemplatePath, config, map);

        // serviceImpl
        String serviceImplResultPath = resultBaseDir + "service/impl/" + (String) formEntity.getEntityName() + "ServiceImpl.java";
        String serviceImplTemplatePath = templateBaseDir + "ServiceImpl.ftl";
        generatorCode(serviceImplResultPath, serviceImplTemplatePath, config, map);

        // sql
        String sqlResultPath = sqlBaseDir + "sql/sql.sql";
        String sqlTemplatePath = templateBaseDir + "Sql.ftl";
        generatorCode(sqlResultPath, sqlTemplatePath, config, map);

        formEntity.setInitFlag(0);


        Connection conn = null;
        try {
            //读取sql语句
            StringBuilder buffer1 = new StringBuilder();
            StringBuilder buffer = new StringBuilder();
            InputStream is = new FileInputStream("C:\\Users\\pengpan\\IdeaProjects\\back_general_system\\code_generator\\src\\main\\resources\\sqlsql\\sql.sql");
            String line; // 用来保存每行读取的内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine(); // 读取第一行
            boolean flag = false;
            while (line != null) { // 如果 line 为空说明读完了
                if (!flag) {
                    buffer1.append(line); // 将读到的内容添加到 buffer 中
                    buffer1.append("\n"); // 添加换行符
                    flag = true;
                } else {
                    buffer.append(line); // 将读到的内容添加到 buffer 中
                    buffer.append("\n"); // 添加换行符
                }
                line = reader.readLine(); // 读取下一行
            }
            reader.close();
            is.close();
            System.out.println(buffer.toString());
            System.out.println(buffer1.toString());
            //创建数据库表
            Class.forName(driver);
            //测试url中是否包含useSSL字段，没有则添加设该字段且禁用
            if( url.indexOf("?") == -1 ){
                url = url + "?useSSL=false" ;
            }
            else if( url.indexOf("useSSL=false") == -1 || url.indexOf("useSSL=true") == -1 )
            {
                url = url + "&useSSL=false";
            }

            conn = DriverManager.getConnection(url, userName, password);

            Statement stat = conn.createStatement();
            // 事务
            conn.setAutoCommit( false ); // 更改JDBC事务的默认提交方式
            stat.executeUpdate(buffer1.toString());
            stat.executeUpdate(buffer.toString());
            conn.commit(); //提交JDBC事务
            conn.setAutoCommit( true ); // 恢复JDBC事务的默认提交方式
            // 释放资源
            stat.close();
            conn.close();
        } catch (Exception e) {
            return new CommonResult<>("数据表创建失败!", 500);
        }
        return new CommonResult<>("success");
    }

    // ----------------------------------------------

    @GetMapping("/generator")
    public String test(ModelMap map) {
        FormEntity formEntity = new FormEntity();
        formEntity.setTableName("demo");
        formEntity.setEntityName("Demo");

        List<EntityField> entityFields = new ArrayList<>();

        EntityField entityField = new EntityField();
        entityField.setFieldName("id");
        entityField.setFieldType("String");
        entityField.setDbColumn("id");
        entityField.setDbType("varchar(36)");
        entityField.setNotNull("on");
        entityField.setComment("id");
        entityFields.add(entityField);

        EntityField entityField1 = new EntityField();
        entityField1.setFieldName("accountId");
        entityField1.setFieldType("String");
        entityField1.setDbColumn("account_id");
        entityField1.setDbType("varchar(36)");
        entityField1.setNotNull("on");
        entityField1.setComment("account id");
        entityFields.add(entityField1);

        formEntity.setEntityFields(entityFields);
        formEntity.setInitFlag(1);

        map.put("formEntity", formEntity);

        return "generator";
    }

    @GetMapping(value = "/generate-code")
    public String generator() {
        return "redirect:generator";
    }

    private void generatorCode(String resultPath, String templatePath, Configuration config, Map<String, Object> map) {
        try {
            prepareFile(resultPath);
            Template template = config.getTemplate(templatePath, "UTF-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultPath), "UTF-8"));
            template.process(map, out);
            out.flush();
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();

            File file = new File(resultPath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private String prepareFile(String resultPath) throws IOException {

        // if file exists, delete it
        File file = new File(resultPath);
        if (file.exists()) {
            file.delete();
        }

        // if parent directory do not exists, create the parent directory
        if (!file.getParentFile().exists()) {
            boolean mkdir = file.getParentFile().mkdirs();
            if (!mkdir) {
                throw new RuntimeException("mkdir parent directory failed");
            }
        }

        // create file
        file.createNewFile();

        return resultPath;
    }

}

