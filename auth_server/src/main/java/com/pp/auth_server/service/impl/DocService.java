package com.pp.auth_server.service.impl;

import com.pp.auth_server.dao.IDocDao;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.IDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocService implements IDocService {

    @Autowired
    IDocDao docDao;

    //为课程选教材
    public int addTextBook(TextBook textBook){
        return docDao.addTextBook(textBook);
    }

    //删除教材
    public void deleteTextBook(String id){
        docDao.deleteTextBook(id);
    }
    //存储文件信息
    public int addDocument(Document document){
        return docDao.addDocument(document);
    }
    //删除存在的文件信息
    public void deleteDocument(String id){
         docDao.deleteDocument(id);
    }
    //通过id找到文件信息
    public Document getDocumentById(String id){
        return docDao.getDocumentById(id);
    }

    //得到课程的doc信息
    public CourseDoc getCourserAndDoc(String id){
        return docDao.getCourserAndDoc(id);
    }
    //获得待审核的课程
    public List<CourseDoc> getTextbookList(int pageStart, int pageSize, String tstate, String semester){
        return docDao.getTextbookList(pageStart,pageSize,tstate,semester);
    }
    //获得待审核的课程总数
    public int getTextbookListCount(String tstate,String semester){
        return docDao.getTextbookListCount(tstate,semester);
    }
    //审核检查通过
    public int acceptBookAudit(String id){
        return docDao.acceptBookAudit(id);
    }
    //拒绝教材
    public int refuseBookAudit(String id){
        return docDao.refuseBookAudit(id);
    }
    //得到待审核的实施大纲
    public List<CourseDoc> getDocList(int pageStart,int pageSize,String dstate,String semester){
        return docDao.getDocList(pageStart,pageSize,dstate,semester);
    }
    //得到待审核的实施大纲总数
    public int getDocListCount(String dstate,String semester){
        return docDao.getDocListCount(dstate,semester);
    }
    //通过实施大纲
    public int acceptDocAudit(String id){
        return docDao.acceptDocAudit(id);
    }
    //拒绝实施大纲
    public int refuseDocAudit(String id,String refuseresult){
        return docDao.refuseDocAudit(id,refuseresult);
    }

    //通过课程得到实施大纲
    public CourseDoc getDocByCourseId(String id){
        return docDao.getDocByCourseId(id);
    }
    //调停课
    public int addAdjustClass(AdjustClass adjustClass){
        return docDao.addAdjustClass(adjustClass);
    }
    //删除调停课申请
    public void deleteAdjustClass(String coursecode){
        docDao.deleteAdjustClass(coursecode);
    }
    //得到调停课申请列表
    public List<AdjustClassAndCourse> getAdjustClassList(int pageStart,int pageSize,String id,String semester,String astate){
        return docDao.getAdjustClassList(pageStart,pageSize,id,semester,astate);
    }
    //得到调停课申请列表总数
    public int getAdjustClassListCount(String id,String semester,String astate){
        return docDao.getAdjustClassListCount(id,semester,astate);
    }
    //通过课程id获得申请
    public AdjustClassAndCourse getAdjustById(String id){
        return docDao.getAdjustById(id);
    }
    //接收调课申请
    public int acceptAdjust(String id){
        return docDao.acceptAdjust(id);
    }
    //拒绝调课申请
    public int refuseAdjust(String id,String refuseresult){
        return docDao.refuseAdjust(id,refuseresult);
    }
}
