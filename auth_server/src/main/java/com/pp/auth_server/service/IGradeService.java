package com.pp.auth_server.service;

import com.pp.auth_server.domain.Grade;

import java.util.List;

public interface IGradeService {
    //修改单个学生成绩
    public int editStudentGrade(Grade Grade);
    //得到学生的所有课程
    public List<Grade> getStudentCourse(int pageStart, int pageSize, String id, String semester);
    //得到所有学生总数
    public int getStudentCourseCount(String id, String semester);
}
