package com.pp.auth_server.service;

import com.pp.auth_server.domain.*;

import java.util.List;

public interface IDocService {
    //为课程选教材
    public int addTextBook(TextBook textBook);
    //删除教材
    public void deleteTextBook(String id);
    //存储文件信息
    public int addDocument(Document document);
    //删除存在的文件信息
    public void deleteDocument(String id);
    //通过id找到文件信息
    public Document getDocumentById(String id);
    //得到课程的doc信息
    public CourseDoc getCourserAndDoc(String id);
    //获得待审核的课程
    public List<CourseDoc> getTextbookList(int pageStart, int pageSize, String tstate, String semester);
    //获得待审核的课程总数
    public int getTextbookListCount(String tstate, String semester);
    //审核检查通过
    public int acceptBookAudit(String id);
    //拒绝教材
    public int refuseBookAudit(String id);
    //得到待审核的实施大纲
    public List<CourseDoc> getDocList(int pageStart, int pageSize, String dstate, String semester);
    //得到待审核的实施大纲总数
    public int getDocListCount(String dstate, String semester);
    //通过实施大纲
    public int acceptDocAudit(String id);
    //拒绝实施大纲
    public int refuseDocAudit(String id, String refuseresult);
    //通过课程得到实施大纲
    public CourseDoc getDocByCourseId(String id);
    //调停课
    public int addAdjustClass(AdjustClass adjustClass);
    //删除调停课申请
    public void deleteAdjustClass(String coursecode);
    //得到调停课申请列表
    public List<AdjustClassAndCourse> getAdjustClassList(int pageStart, int pageSize, String id, String semester, String astate);
    //得到调停课申请列表总数
    public int getAdjustClassListCount(String id, String semester, String astate);
    //通过课程id获得申请
    public AdjustClassAndCourse getAdjustById(String id);
    //接收调课申请
    public int acceptAdjust(String id);
    //拒绝调课申请
    public int refuseAdjust(String id, String refuseresult);

}
