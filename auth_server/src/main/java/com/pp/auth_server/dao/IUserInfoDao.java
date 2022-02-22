package com.pp.auth_server.dao;

import com.pp.auth_server.domain.StudentInfo;
import com.pp.auth_server.domain.TeacherInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserInfoDao {

    //查询学生列表
    public List<StudentInfo> getStudentInfoList(String username, String department, String number);
    //查询老师列表
    public List<TeacherInfo> getTeacherInfoList(String username, String department, String number);
    //查询学生总数
    public int getStudentInfoCount(String username, String department, String number);
    //查询老师总数
    public int getTeacherInfoCount(String username, String department, String number);

    //根据id查学会说学生信息
    public StudentInfo getStudentInfoById(String id);

    public String getGradeById(String id);

    public TeacherInfo getTeacherInfoById(String id);
    //通过学号查询学生id
    public String getSnoById(String sno);
    //通过职工号查询老师id
    public String getTnoById(String tno);
    //根据id修改学生详细信息
    public int editStudentInfo(String sphone, String scontact, String scontactphone, String id);
    //根据id修改老师信息
    public int editTeacherInfo(String tphone, String tcontact, String tcontactphone, String id);
}
