package com.pp.auth_server.dao;

import com.pp.auth_server.domain.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGradeDao {
    //修改单个学生成绩
    public int editStudentGrade(Grade grade);
    //得到学生的所有课程
    public List<Grade> getStudentCourse(int pageStart, int pageSize, String id, String semester);
    //得到所有学生总数
    public int getStudentCourseCount(String id, String semester);
}
