package com.pp.auth_server.service;

import com.pp.auth_server.domain.*;

import java.util.List;

public interface ICourseService {
    //得到所有学生的年级
    public List<String> getAllGradeYear();
    //得到所有学生的学院
    public List<String> getAllAcademy();
    //得到所有的学期
    public List<Semester> getAllSemester(String state);
    //得到所有字典
    public List<Semester> getAllDiction(String dictionType);
    //得到所有字典类型
    public List<String> getAllDictionType();
    //得到所有班级
    public List<String> getAllClass();
    public List<TeacherInfo> getAllTeacher();
    //添加课程
    public int addCourse(Course course);
    //通过班级得到人数
    public int getClassCount(List<String> idList, String beginacademy, String begingrade);
    //得到课程列表
    public List<Course> getCourseList(int pageStart, int pageSize, String coursetype, String beginacademy, String begingrade, String coursename, String semester);

    public int getCourseCount(String coursetype, String beginacademy, String begingrade, String coursename, String semester);

    public int addSc(String studentSno, String coursecode);

    //通过班级集合获得学生
    public List<StudentInfo> getStudentByClassList(List<String> ClassList, String beginacademy, String begingrade);
    public int studentSc(String id, String coursecode);
    public void addScCourseNum(String coursecode);
    public void subScCourseNum(String coursecode);
    public List<Course> getScCourseList(int pageStart, int pageSize, String coursetype, String coursename, String id, String semester);

    public int getScCourseCount(String coursetype, String coursename, String id, String semester);

    public int dropCourse(String id, String coursecode);

    public Course getCourseById(String id);

    //根据id修改课程
    public int editCourse(Course course);

    //删除课程所有开课记录
    public int dropCourseByCourseCode(String coursecode);
    //删除课程
    public int deleteCourse(String id);
    //通过id得到老师课程
    public List<Course> getTeacherCourseList(int pageStart, int pageSize, String coursetype, String coursename, String id, String semester);
    //得到教师课程列表
    public List<Course> getClassCourseList(String academy, String grade, String exeCourse, String semester);
    //得到教师课程列表总数
    public int getTeacherCourseListCount(String coursetype, String coursename, String id, String semester);
    //修改卷面比例
    public int editProportion(String proportion, String coursecode);
    //得到该课程的所有学生
    public List<Grade> getCourseStudentList(String execourse, String coursecode);
    //得到我的周课表（学生)
    public List<AdjustClassAndCourse> getWeekCourseList(String semester, String id);
    //得到我的周课表（老师）
    public List<AdjustClassAndCourse> getWeekTeacherCourseList(String semester, String id);
    //通过课程名字得到课程
    public Course getCourseByName(String coursename);
    //得到教室所有课程
    public List<Course> getClassRoomCourse(String spotflag, String semester);
    //得到教师所有课程
    public List<Course> getCourseByTeacher(String tline, String semester);
    //保存课程总结
    public int addSummary(String summary, String coursecode);
    //得到课程总结
    public String getSummary(String coursecode);
}
