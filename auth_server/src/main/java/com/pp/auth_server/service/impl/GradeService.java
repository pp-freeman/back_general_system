package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IGradeDao;
import com.pp.auth_server.domain.Grade;
import com.pp.auth_server.service.IGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService implements IGradeService {

    @Autowired
    IGradeDao gradeDao;

    //修改单个学生成绩
    public int editStudentGrade(Grade grade){
        return gradeDao.editStudentGrade(grade);
    }

    //得到学生的所有课程
    public List<Grade> getStudentCourse(int pageStart,int pageSize,String id, String semester){
        return gradeDao.getStudentCourse(pageStart,pageSize,id,semester);
    }
    //得到所有学生总数
    public int getStudentCourseCount(String id,String semester){
        return gradeDao.getStudentCourseCount(id,semester);
    }
}
