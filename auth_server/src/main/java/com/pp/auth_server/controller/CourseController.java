package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.dao.IUserInfoDao;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.ICourseService;
import com.pp.auth_server.service.IDocService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

/**
 * 课程
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    ICourseService courseService;
    @Autowired
    IUserInfoDao userInfoService;
    @Autowired
    IDocService docService;

    /**
     * 得到所有下拉框数据
     * @return
     */
    @RequestMapping("/getFormdata")
    public String getFormdata(){
        List<String> academy =  courseService.getAllAcademy();
        List<String> gradeYear = courseService.getAllGradeYear();
        List<Semester> semesters =  courseService.getAllSemester("学期");
        List<Semester> classRoom = courseService.getAllSemester("教室");
        List<String> dictionType = courseService.getAllDictionType();
        List<String> sclass = courseService.getAllClass();
        List<TeacherInfo> teacherInfos = courseService.getAllTeacher();
        HashMap<String,Object> result = new HashMap<>();
        result.put("gradeYearList",gradeYear);
        result.put("academyList",academy);
        result.put("semesterList",semesters);
        result.put("sclass",sclass);
        result.put("classRoom",classRoom);
        result.put("teacher",teacherInfos);
        result.put("dictionType",dictionType);
        return JSON.toJSONString(result);
    }

    /**
     * 字典列表
     * @param dictionType
     * @return
     */
    @Log("查看字典")
    @RequiresPermissions("/dictionMan")
    @RequestMapping("/getDicList")
    public String getDicList(String dictionType){
        List<Semester> semesters = courseService.getAllDiction(dictionType);
        HashMap<String,Object> result = new HashMap<>();
        if(semesters==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("diction",semesters);
        return JSON.toJSONString(result);
    }
    /**
     * 添加课程
     * @param course
     * @return
     */
    @Log("添加课程")
    @RequiresPermissions("/addCourse")
    @RequestMapping("/addCourse")
    public String addCourse(@RequestBody Course course){
        HashMap<String,Object> result = new HashMap<>();
        course.setCoursecode(UUID.randomUUID().toString());
        List<Course> courses = courseService.getClassRoomCourse(course.getSpotflag(),course.getSemester());
        //判断教室时间段有没有重合
        for(Course c : courses){
            if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(course.getWeeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(course.getWeeknum().split(",")[0]))){
                if(c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])){
                    if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(course.getTimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(course.getTimeflag().split(",")[1]))){
                        //排课时间重合
                        result.put("state",500);
                        return JSON.toJSONString(result);
                    }
                }
            }
        }
        List<Course> courses1 = courseService.getCourseByTeacher(course.getTline(),course.getSemester());
        //判断教师时间段有没有重合
        for(Course c : courses1){
            if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(course.getWeeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(course.getWeeknum().split(",")[0]))){
                if(c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])){
                    if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(course.getTimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(course.getTimeflag().split(",")[1]))){
                        //排课时间重合
                        result.put("state",501);
                        return JSON.toJSONString(result);
                    }
                }
            }
        }
        List<String> idList = new ArrayList<>();
        String s[] = course.getExecourses().split(",");
        for(int i=0;i<s.length;i++){
            List<Course> courses2 = courseService.getClassCourseList(course.getBeginacademy(),course.getBegingrade(),s[i],course.getSemester());
            for(Course c : courses2){
                if(!course.getCoursecode().equals(c.getCoursecode())) {
                    if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(course.getWeeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(course.getWeeknum().split(",")[0]))){
                        if(c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])){
                            if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(course.getTimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(course.getTimeflag().split(",")[1]))){
                                //排课时间重合
                                result.put("state",502);
                                return JSON.toJSONString(result);
                            }
                        }
                    }
                }
            }
            idList.add(s[i]);
        }
        if(course.getCoursetype().equals("必修课")){
            course.setAtusum(courseService.getClassCount(idList,course.getBeginacademy(),course.getBegingrade()));
        }
        if(course.getCoursetype().equals("选修课")){
            course.setAtusum(0);
        }

        if(course.getAttendexpect()<courseService.getClassCount(idList,course.getBeginacademy(),course.getBegingrade())){
            result.put("state",300); //班级人数大于课程的预计人数
        }else{
            int i = courseService.addCourse(course);
            if(course.getCoursetype().equals("必修课")){
                List<String> classList = Arrays.asList(course.getExecourses().split(","));
                List<StudentInfo> studentInfos = courseService.getStudentByClassList(classList,course.getBeginacademy(),course.getBegingrade());
                for(StudentInfo s1 : studentInfos){
                    courseService.addSc(s1.getId(),course.getCoursecode());
                }
            }
            if(i==0){
                result.put("state",404);
            }else{
                result.put("state",200);
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到开课的所有课程列表
     * @param pageNum
     * @param pageSize
     * @param coursetype
     * @param beginacademy
     * @param begingrade
     * @param coursename
     * @param semester
     * @return
     */
    @Log("查看开课课程")
    @RequiresPermissions(value = {"/showCourse","/choiceAble"},logical= Logical.OR)
    @RequestMapping("/getCourseList")
    public String getCourseList(int pageNum,int pageSize,String coursetype,String beginacademy,String begingrade,String coursename,String semester){
        List<Course> courses = courseService.getCourseList((pageNum-1)*pageSize,pageSize,coursetype,beginacademy,begingrade,coursename,semester);
        int number = courseService.getCourseCount(coursetype,beginacademy,begingrade,coursename,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("number",number);
        result.put("courses",courses);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 学生选课
     * @param id
     * @param coursecode
     * @return
     */
    @Log("选课")
    @RequiresPermissions("/takeCourse")
    @RequestMapping("/studentSc")
    public String studentSc(String id,String coursecode){
        HashMap<String,Object> result = new HashMap<>();
        Course course = courseService.getCourseById(coursecode);
        if(course.getAttendexpect()==course.getAtusum()){
            result .put("state",500);
        }else{
            int i = 0;
            try {
                i = courseService.studentSc(id,coursecode);
                courseService.addScCourseNum(coursecode);
                if(i==0){
                    result.put("state",404);
                }else{
                    result.put("state",200);
                }
            }catch (Exception e){
                result.put("state",300);
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到学生选课列表
     * @param pageNum
     * @param pageSize
     * @param coursetype
     * @param coursename
     * @param id
     * @param semester
     * @return
     */
    @Log("查看已选课列表")
    @RequiresPermissions("/choiced")
    @RequestMapping("/getScCourseList")
    public String getScCourseList(int pageNum,int pageSize,String coursetype,String coursename,String id,String semester){
        List<Course> courses = courseService.getScCourseList((pageNum-1)*pageSize,pageSize,coursetype,coursename,id,semester);
        int number = courseService.getScCourseCount(coursetype,coursename,id,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("number",number);
        result.put("courses",courses);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 得到我的学生课表
     * @param coursetype
     * @param semester
     * @param coursename
     * @param id
     * @return
     */
    @Log("查看我的学期课表")
    @RequiresPermissions("/myCSchedule")
    @RequestMapping("/getMyCourseList")
    public String getAllScCourseList(String coursetype,String semester,String coursename,String id){
        List<Course> courses = courseService.getScCourseList(0,30,coursetype,coursename,id,semester);
        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 我的周课表学生
     * @param semester
     * @param id
     * @return
     */
    @Log("查看我的周课表")
    @RequiresPermissions("/myWeekTableTime")
    @RequestMapping("/getMyWeekCourseList")
    public String getMyWeekCourseList(String week,String semester,String id){
        List<AdjustClassAndCourse> courses = courseService.getWeekCourseList(semester,id);
        List<TimeAble> timeAbles = new ArrayList<>();
        if(!semester.equals("")){
            List<AdjustClassAndCourse> courses1 = new ArrayList<>();
            for(AdjustClassAndCourse c : courses){
                if(Integer.parseInt(week)<=Integer.parseInt(c.getWeeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getWeeknum().split(",")[0])){
                    courses1.add(c);
                }
            }
            for(AdjustClassAndCourse c : courses1){
                TimeAble t = new TimeAble();
                if(c.getAstate()!=null&&c.getAstate().equals("1")){
                    if(c.getType().equals("1")){
                        if(Integer.parseInt(week)<=Integer.parseInt(c.getAweeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getAweeknum().split(",")[0])){
                        }else{
                            String[] arr = c.getTimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getSpotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                            timeAbles.add(t);
                        }
                    }else if(c.getType().equals("2")){
                        String[] arr = c.getTimeflag().split(",");
                        t.setDay(arr[0]);
                        t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                        t.setPeriod(arr[1]);
                        t.setName(c.getCoursename());
                        t.setRoom(c.getAspotflag());
                        t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                        t.setType(c.getCoursetype());
                        t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                        timeAbles.add(t);
                    }else{
                        if(Integer.parseInt(week)<=Integer.parseInt(c.getAweeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getAweeknum().split(",")[0])){
                            String[] arr = c.getAtimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getAspotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getAweeknum().split(",")[0]+"-"+c.getAweeknum().split(",")[1]+"周");
                            timeAbles.add(t);
                        }else{
                            String[] arr = c.getTimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getSpotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                            timeAbles.add(t);
                        }
                    }
                }else {
                    String[] arr = c.getTimeflag().split(",");
                    t.setDay(arr[0]);
                    t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                    t.setPeriod(arr[1]);
                    t.setName(c.getCoursename());
                    t.setRoom(c.getSpotflag());
                    t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                    t.setType(c.getCoursetype());
                    t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                    timeAbles.add(t);
                }
                if(semester==""){
                    timeAbles = null;
                }
            }
        }


        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }


    /**
     * 得到我的周课表老师
     * @param week
     * @param semester
     * @param id
     * @return
     */
    @Log("查看我的周课表")
    @RequiresPermissions("/myTeacherWeekAble")
    @RequestMapping("/getMyWeekTeacherCourseList")
    public String getMyWeekTeacherCourseList(String week,String semester,String id){
        List<AdjustClassAndCourse> courses = courseService.getWeekTeacherCourseList(semester,id);
        List<TimeAble> timeAbles = new ArrayList<>();
        if(!semester.equals("")){
            List<AdjustClassAndCourse> courses1 = new ArrayList<>();
            for(AdjustClassAndCourse c : courses){
                if(Integer.parseInt(week)<=Integer.parseInt(c.getWeeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getWeeknum().split(",")[0])){
                    courses1.add(c);
                }
            }
            for(AdjustClassAndCourse c : courses1){
                TimeAble t = new TimeAble();
                if(c.getAstate()!=null&&c.getAstate().equals("1")){
                    if(c.getType().equals("1")){
                        if(Integer.parseInt(week)<=Integer.parseInt(c.getAweeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getAweeknum().split(",")[0])){

                        }else{
                            String[] arr = c.getTimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getSpotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                            timeAbles.add(t);
                        }
                    }else if(c.getType().equals("2")){
                        String[] arr = c.getTimeflag().split(",");
                        t.setDay(arr[0]);
                        t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                        t.setPeriod(arr[1]);
                        t.setName(c.getCoursename());
                        t.setRoom(c.getAspotflag());
                        t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                        t.setType(c.getCoursetype());
                        t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                        timeAbles.add(t);

                    }else{
                        if(Integer.parseInt(week)<=Integer.parseInt(c.getAweeknum().split(",")[1])&&Integer.parseInt(week)>=Integer.parseInt(c.getAweeknum().split(",")[0])){
                            String[] arr = c.getAtimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getAspotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getAweeknum().split(",")[0]+"-"+c.getAweeknum().split(",")[1]+"周");
                            timeAbles.add(t);
                        }else{
                            String[] arr = c.getTimeflag().split(",");
                            t.setDay(arr[0]);
                            t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                            t.setPeriod(arr[1]);
                            t.setName(c.getCoursename());
                            t.setRoom(c.getSpotflag());
                            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                            t.setType(c.getCoursetype());
                            t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                            timeAbles.add(t);
                        }
                    }
                }else {
                    String[] arr = c.getTimeflag().split(",");
                    t.setDay(arr[0]);
                    t.setLength(String.valueOf(Integer.parseInt(arr[2]) - Integer.parseInt(arr[1]) + 1));
                    t.setPeriod(arr[1]);
                    t.setName(c.getCoursename());
                    t.setRoom(c.getSpotflag());
                    t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
                    t.setType(c.getCoursetype());
                    t.setWeek(c.getWeeknum().split(",")[0] + "-" + c.getWeeknum().split(",")[1] + "周");
                    timeAbles.add(t);
                }
                if(semester==""){
                    timeAbles = null;
                }
            }
        }


        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }


    /**
     * 学生学期课表
     * @param coursetype
     * @param semester
     * @param coursename
     * @return
     */
    @Log("查看学生学期课表")
    @RequiresPermissions("/sCSchedule")
    @RequestMapping("/getStudentCourseList")
    public String getStudentCourseList(String coursetype,String semester,String coursename,String sno){
        String id = userInfoService.getSnoById(sno);
        List<Course> courses = courseService.getScCourseList(0,30,coursetype,coursename,id,semester);
        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 得到我的老师课表
     * @param coursetype
     * @param semester
     * @param coursename
     * @param id
     * @return
     */
    @Log("查看我的学期课表")
    @RequiresPermissions("/myTeacherSchedule")
    @RequestMapping("/getMyTeacherCourseList")
    public String getMyTeacherScCourseList(String coursetype,String semester,String coursename,String id){
        List<Course> courses = courseService.getTeacherCourseList(0,30,coursetype,coursename,id,semester);
        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("state",200);
        result.put("timeAble",timeAbles);
        return JSON.toJSONString(result);
    }

    /**
     * 教师学期课表
     * @param coursetype
     * @param semester
     * @param coursename
     * @param tno
     * @return
     */
    @Log("查看老师学期课表")
    @RequiresPermissions("/tCSchedule")
    @RequestMapping("/getTeacherCourseList")
    public String getTeacherScCourseList(String coursetype,String semester,String coursename,String tno){
        String id = userInfoService.getTnoById(tno);
        List<Course> courses = courseService.getTeacherCourseList(0,30,coursetype,coursename,id,semester);

        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 得到班级学期课表
     * @param academy
     * @param grade
     * @param exeCourse
     * @param semester
     * @return
     */
    @Log("查看班级学期课表")
    @RequiresPermissions("/cCSchedule")
    @RequestMapping("/getClassCourseList")
    public String getClassCourseList(String academy,String grade,String exeCourse,String semester){
        List<Course> courses = courseService.getClassCourseList(academy,grade,exeCourse,semester);
        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("state",200);
        result.put("timeAble",timeAbles);
        return JSON.toJSONString(result);
    }

    /**
     * 得到教室学期课表
     * @param spotflag
     * @param semester
     * @return
     */
    @Log("查看教室学期课表")
    @RequiresPermissions("/classRoomTimeAble")
    @RequestMapping("/getClassRoomCourseList")
    public String getClassRoomCourseList(String spotflag,String semester){
        List<Course> courses = courseService.getClassRoomCourse(spotflag,semester);
        List<TimeAble> timeAbles = new ArrayList<>();
        for(Course c : courses){
            TimeAble t = new TimeAble();
            String[] arr = c.getTimeflag().split(",");
            t.setDay(arr[0]);
            t.setLength(String.valueOf(Integer.parseInt(arr[2])-Integer.parseInt(arr[1])+1));
            t.setPeriod(arr[1]);
            t.setName(c.getCoursename());
            t.setRoom(c.getSpotflag());
            t.setTeacher(userInfoService.getTeacherInfoById(c.getTline()).getTname());
            t.setType(c.getCoursetype());
            t.setWeek(c.getWeeknum().split(",")[0]+"-"+c.getWeeknum().split(",")[1]+"周");
            timeAbles.add(t);
        }
        if(semester==""){
            timeAbles = null;
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("timeAble",timeAbles);
        result.put("state",200);
        return JSON.toJSONString(result);
    }
    /**
     * 学生退课
     * @param id
     * @param coursecode
     * @return
     */
    @Log("退课")
    @RequiresPermissions("/outCourse")
    @RequestMapping("/dropCourse")
    public String dropCourse(String id,String coursecode){
        HashMap<String,Object> result = new HashMap<>();
        int i = 0;

        i = courseService.dropCourse(id,coursecode);
        courseService.subScCourseNum(coursecode);
        
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 教师的所有课程
     * @param pageNum
     * @param pageSize
     * @param coursetype
     * @param semester
     * @param coursename
     * @param id
     * @return
     */
    @Log("查看教师所有课程")
    @RequiresPermissions(value = {"/document","/adjustClass","/logGrade"},logical = Logical.OR)
    @RequestMapping("/TeacherCourseList")
    public String TeacherScCourseList(int pageNum,int pageSize,String coursetype,String semester,String coursename,String id){
        List<Course> courses = courseService.getTeacherCourseList((pageNum-1)*pageSize,pageSize,coursetype,coursename,id,semester);
        int num = courseService.getTeacherCourseListCount(coursetype,coursename,id,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(courses == null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("number",num);
        result.put("course",courses);
        return JSON.toJSONString(result);
    }

    /**
     * 通过id得到课程
     * @param id
     * @return
     */
    @RequestMapping("/getCourseById")
    public String getCourseById(String id) {
        return JSON.toJSONString(courseService.getCourseById(id));
    }

    /**
     * 保存课程总结
     * @param coursecode
     * @param goods
     * @return
     */
    @RequiresPermissions("/showGradeCourse")
    @RequestMapping("/addSummary")
    public String addSummary(String coursecode,String goods){
        int i = courseService.addSummary(goods,coursecode);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i == 0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到课程总结
     * @param coursecode
     * @return
     */
    @RequiresPermissions("/showGradeCourse")
    @RequestMapping("/getSummary")
    public String getSummary(String coursecode){
        String string = courseService.getSummary(coursecode);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("goods",string);
        return JSON.toJSONString(result);
    }

    /**
     * 修改课程
     * @param course
     * @return
     */
    @Log("修改课程")
    @RequiresPermissions("/editCourse")
    @RequestMapping("/editCourse")
    public String editCourse(@RequestBody Course course) {
        HashMap<String,Object> result = new HashMap<>();
        List<Course> courses = courseService.getClassRoomCourse(course.getSpotflag(),course.getSemester());
        for(Course c : courses){
            if(!course.getCoursecode().equals(c.getCoursecode())) {
                if (!(Integer.parseInt(c.getWeeknum().split(",")[0]) > Integer.parseInt(course.getWeeknum().split(",")[1]) || Integer.parseInt(c.getWeeknum().split(",")[1]) < Integer.parseInt(course.getWeeknum().split(",")[0]))) {
                    if (c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])) {
                        if (!(Integer.parseInt(c.getTimeflag().split(",")[1]) > Integer.parseInt(course.getTimeflag().split(",")[2]) || Integer.parseInt(c.getTimeflag().split(",")[2]) < Integer.parseInt(course.getTimeflag().split(",")[1]))) {
                            //排课时间重合
                            result.put("state", 500);
                            return JSON.toJSONString(result);
                        }
                    }
                }
            }
        }
        List<Course> courses1 = courseService.getCourseByTeacher(course.getTline(),course.getSemester());
        //判断教师时间段有没有重合
        for(Course c : courses1){
            if(!course.getCoursecode().equals(c.getCoursecode())) {
                if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(course.getWeeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(course.getWeeknum().split(",")[0]))){
                    if(c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])){
                        if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(course.getTimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(course.getTimeflag().split(",")[1]))){
                            //排课时间重合
                            result.put("state",501);
                            return JSON.toJSONString(result);
                        }
                    }
                }
            }
        }
        List<String> idList = new ArrayList<>();
        String s[] = course.getExecourses().split(",");
        for(int i=0;i<s.length;i++){
            if(!course.getExecourses().equals("")){
                List<Course> courses2 = courseService.getClassCourseList(course.getBeginacademy(),course.getBegingrade(),s[i],course.getSemester());
                for(Course c : courses2){
                    if(!course.getCoursecode().equals(c.getCoursecode())) {
                        if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(course.getWeeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(course.getWeeknum().split(",")[0]))){
                            if(c.getTimeflag().split(",")[0].equals(course.getTimeflag().split(",")[0])){
                                if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(course.getTimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(course.getTimeflag().split(",")[1]))){
                                    //排课时间重合
                                    result.put("state",502);
                                    return JSON.toJSONString(result);
                                }
                            }
                        }
                    }
                }
            }
            idList.add(s[i]);
        }
        if(course.getCoursetype().equals("必修课")){
            course.setAtusum(courseService.getClassCount(idList,course.getBeginacademy(),course.getBegingrade()));
        }
        if(course.getCoursetype().equals("选修课")) {
            course.setAtusum(0);
        }
        if(course.getAttendexpect()<courseService.getClassCount(idList,course.getBeginacademy(),course.getBegingrade())){
            result.put("state",300);
        }else{
            int i = courseService.editCourse(course);
            if(course.getCoursetype().equals("必修课")){
                courseService.dropCourseByCourseCode(course.getCoursecode());
                List<String> classList = Arrays.asList(course.getExecourses().split(","));
                List<StudentInfo> studentInfos = courseService.getStudentByClassList(classList,course.getBeginacademy(),course.getBegingrade());
                for(StudentInfo s1 :  studentInfos){
                    courseService.addSc(s1.getId(),course.getCoursecode());
                }
            }else{
                courseService.dropCourseByCourseCode(course.getCoursecode());
            }

            if(i==0){
                result.put("state",404);
            }else{
                result.put("state",200);
            }
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除课程
     * @param id
     * @return
     */
    @Log("删除课程")
    @RequiresPermissions("/deleteCourse")
    @RequestMapping("/deleteCourse")
    public String deleteCourse(String id){
        Document document = docService.getDocumentById(id);
        if(document != null){
            File file = new File(document.getPath());
            if(file!=null) {
                file.delete();
            }
        }
        int i = courseService.deleteCourse(id);
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 修改卷面成绩比例
     * @param course
     * @return
     */
    @Log("修改卷面成绩比例")
    @RequiresPermissions("/editProportion")
    @RequestMapping("/editProportion")
    public String editProportion(@RequestBody Course course){
        int i = courseService.editProportion(course.getProportion(),course.getCoursecode());
        HashMap<String,Object> result = new HashMap<>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到选择该课程的所有学生
     * @param bclass
     * @param coursecode
     * @return
     */
    @Log("查看课程的所有学生")
    @RequiresPermissions("/showGradeCourse")
    @RequestMapping("/getCourseStudentList")
    public String getCourseStudentList(String execourse,String coursecode){
        List<Grade> grades = courseService.getCourseStudentList(execourse,coursecode);
        HashMap<String,Object> result = new HashMap<>();
        if(grades==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("studentList",grades);
        return JSON.toJSONString(result);
    }
}
