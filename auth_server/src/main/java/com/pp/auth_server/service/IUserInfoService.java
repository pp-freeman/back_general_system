package com.pp.auth_server.service;

import com.pp.auth_server.domain.StudentInfo;
import com.pp.auth_server.domain.TeacherInfo;
import com.pp.auth_server.domain.UserInfo;

import java.util.List;

public interface IUserInfoService {
    public List<UserInfo> getUserInfoList(int pageNum, int pageSize, String username, String department, String number);

    public int getUserInfoCount(String username, String department, String number);

    //根据id查学生信息
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
