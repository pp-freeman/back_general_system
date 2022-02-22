package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.Grade;
import com.pp.auth_server.service.IGradeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 成绩
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    IGradeService gradeService;

    /**
     * 编辑学生成绩
     *
     * @param grade 年级
     * @return {@link String}
     */
    @Log("保存学生成绩")
    @RequiresPermissions("/showGradeCourse")
    @RequestMapping("/editStudentGrade")
    public String editStudentGrade(@RequestBody Grade grade){
        int i = gradeService.editStudentGrade(grade);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 编辑所有学生成绩
     *
     * @param grades 成绩
     * @return {@link String}
     */
    @Log("保存学生成绩")
    @RequiresPermissions("/showGradeCourse")
    @RequestMapping("/editAllStudentGrade")
    public String editAllStudentGrade(@RequestBody List<Grade> grades){
        for (Grade g : grades) {
            gradeService.editStudentGrade(g);
        }
        HashMap<String,Object> result = new HashMap<>();
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 得到学生的年级
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param id       id
     * @param semester 学期
     * @return {@link String}
     */
    @RequestMapping("/getStudentGrade")
    public String getStudentGrade(int pageNum,int pageSize,String id,String semester){
        List<Grade> grades = gradeService.getStudentCourse((pageNum-1)*pageSize,pageSize,id,semester);
        for(Grade g : grades){
            if(Integer.parseInt(g.getTotalgrade())>=90){
                g.setGpa("4.0");
            }else {
                g.setGpa(String.format("%.1f", Integer.parseInt(g.getTotalgrade()) * 0.038));
            }
        }
        int number = gradeService.getStudentCourseCount(id,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(grades == null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("number",number);
        result.put("courses",grades);
        return JSON.toJSONString(result);
    }
}
