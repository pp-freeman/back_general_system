package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.ICourseDao;
import com.pp.auth_server.dao.IUserInfoDao;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.ICourseService;
import com.pp.auth_server.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {
    @Autowired
    ICourseDao courseDao;

    @Autowired
    IUserInfoDao userInfoDao;

    @Autowired
    RedisUtil redisUtil;

    //得到所有学生的年级
    public List<String> getAllGradeYear(){
        if(redisUtil.get("gradeyear")==null){
            redisUtil.set("gradeyear",courseDao.getAllGradeYear());
            redisUtil.expire("gradeyear",30*60);
            return (List<String>) redisUtil.get("gradeyear");
        }else{
            return (List<String>) redisUtil.get("gradeyear");
        }
    }
    //得到所有学生的学院
    public List<String> getAllAcademy(){
        if(redisUtil.get("academy")==null){
            redisUtil.set("academy",courseDao.getAllAcademy());
            redisUtil.expire("academy",30*60);
            return (List<String>) redisUtil.get("academy");
        }else{
            return (List<String>) redisUtil.get("academy");
        }
    }
    //得到所有字典
    public List<Semester> getAllDiction(String dictionType){
        return courseDao.getAllDiction(dictionType);
    }
    //得到所有字典类型
    public List<String> getAllDictionType(){
        if(redisUtil.get("dictionType")==null){
            redisUtil.set("dictionType",courseDao.getAllDictionType());
            redisUtil.expire("dictionType",30*60);
            return (List<String>) redisUtil.get("dictionType");
        }else{
            return (List<String>) redisUtil.get("dictionType");
        }
    }
    //得到所有的学期
    public List<Semester> getAllSemester(String state){
        if(redisUtil.get("dictionType"+state)==null){
            redisUtil.set("dictionType"+state,courseDao.getAllSemester(state));
            redisUtil.expire("dictionType"+state,30*60);
            return (List<Semester>) redisUtil.get("dictionType"+state);
        }else{
            return (List<Semester>) redisUtil.get("dictionType"+state);
        }
    }
    public List<String> getAllClass(){
        if(redisUtil.get("class")==null){
            redisUtil.set("class",courseDao.getAllClass());
            redisUtil.expire("class",30*60);
            return (List<String>) redisUtil.get("class");
        }else{
            return (List<String>) redisUtil.get("class");
        }
    }

    public List<TeacherInfo> getAllTeacher(){
        if(redisUtil.get("teacher")==null){
            redisUtil.set("teacher",userInfoDao.getTeacherInfoList(null,null,null));
            redisUtil.expire("teacher",30*60);
            return (List<TeacherInfo>) redisUtil.get("teacher");
        }else{
            return (List<TeacherInfo>) redisUtil.get("teacher");
        }
    }

    //添加课程
    public int addCourse(Course course){
        return courseDao.addCourse(course);
    }
    //通过班级得到人数
    public int getClassCount(List<String> idList,String beginacademy,String begingrade){
        return courseDao.getClassCount(idList,beginacademy,begingrade);
    }
    //得到课程列表
    public List<Course> getCourseList(int pageStart,int pageSize,String coursetype,String beginacademy,String begingrade,String coursename,String semester){
        return courseDao.getCourseList(pageStart,pageSize,coursetype,beginacademy,begingrade,coursename,semester);
    }

    public int getCourseCount(String coursetype,String beginacademy,String begingrade,String coursename,String semester){
        return courseDao.getCourseCount(coursetype,beginacademy,begingrade,coursename,semester);
    }

    public int addSc(String studentSno,String coursecode){
        return courseDao.addSc(studentSno,coursecode);
    }

    public List<StudentInfo> getStudentByClassList(List<String> ClassList,String beginacademy,String begingrade){
        return courseDao.getStudentByClassList(ClassList,beginacademy,begingrade);
    }

    public int studentSc(String id,String coursecode){
        return courseDao.studentSc(id,coursecode);
    }
    public void addScCourseNum(String coursecode){
        courseDao.addScCourseNum(coursecode);
    }
    public void subScCourseNum(String coursecode){
        courseDao.subScCourseNum(coursecode);
    }

    public List<Course> getScCourseList(int pageStart,int pageSize,String coursetype,String coursename,String id,String semester){
        return courseDao.getScCourseList(pageStart,pageSize,coursetype,coursename,id,semester);
    }

    public int getScCourseCount(String coursetype,String coursename,String id,String semester){
        return courseDao.getScCourseCount(coursetype,coursename,id,semester);
    }

    public int dropCourse(String id,String coursecode){
        return courseDao.dropCourse(id,coursecode);
    }

    public Course getCourseById(String id){
        return courseDao.getCourseById(id);
    }

    //根据id修改课程
    public int editCourse(Course course){
        return courseDao.editCourse(course);
    }

    //删除课程所有开课记录
    public int dropCourseByCourseCode(String coursecode){
        return courseDao.dropCourseByCourseCode(coursecode);
    }

    //删除课程
    public int deleteCourse(String id){
        return courseDao.deleteCourse(id);
    }

    //通过id得到老师课程
    public List<Course> getTeacherCourseList(int pageStart,int pageSize,String coursetype,String coursename,String id,String semester){
        return courseDao.getTeacherCourseList(pageStart,pageSize,coursetype,coursename,id,semester);
    }

    //得到教师课程列表
    public List<Course> getClassCourseList(String academy,String grade,String exeCourse,String semester){
        return courseDao.getClassCourseList(academy,grade,exeCourse,semester);
    }

    //得到教师课程列表总数
    public int getTeacherCourseListCount(String coursetype,String coursename,String id,String semester){
        return courseDao.getTeacherCourseListCount(coursetype,coursename,id,semester);
    }

    //修改卷面比例
    public int editProportion(String proportion,String coursecode){
        return courseDao.editProportion(proportion,coursecode);
    }

    //得到该课程的所有学生
    public List<Grade> getCourseStudentList(String execourse, String coursecode){
        return courseDao.getCourseStudentList(execourse,coursecode);
    }

    //得到我的周课表（学生)
    public List<AdjustClassAndCourse> getWeekCourseList(String semester,String id){
        return courseDao.getWeekCourseList(semester,id);
    }

    //得到我的周课表（老师）
    public List<AdjustClassAndCourse> getWeekTeacherCourseList(String semester, String id){
        return courseDao.getWeekTeacherCourseList(semester,id);
    }
    //通过课程名字得到课程
    public Course getCourseByName(String coursename){
        return courseDao.getCourseByName(coursename);
    }
    //得到教室所有课程
    public List<Course> getClassRoomCourse(String spotflag,String semester){
        return courseDao.getClassRoomCourse(spotflag,semester);
    }
    //得到教师所有课程
    public List<Course> getCourseByTeacher(String tline,String semester){
        return courseDao.getCourseByTeacher(tline,semester);
    }
    //保存课程总结
    public int addSummary(String summary,String coursecode){
        return courseDao.addSummary(summary,coursecode);
    }
    //得到课程总结
    public String getSummary(String coursecode){
        return courseDao.getSummary(coursecode);
    }
}
