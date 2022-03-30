package com.pp.auth_server.controller;

import com.alibaba.fastjson.JSON;
import com.pp.auth_server.anotation.Log;
import com.pp.auth_server.domain.*;
import com.pp.auth_server.service.ICourseService;
import com.pp.auth_server.service.IDocService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 审核部分
 *
 * @author pengpan
 * @date 2020/12/14
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    IDocService docService;

    @Autowired
    ICourseService courseService;
    private static final Logger logger = LoggerFactory.getLogger(DocController.class);


    /**
     * 添加教材
     *
     * @param textBook 课本
     * @return {@link String}
     */
    @Log("添加教材")
    @RequiresPermissions("/editTextbook")
    @RequestMapping("/addTextbook")
    public String addTextbook(@RequestBody TextBook textBook){

        docService.deleteTextBook(textBook.getCourseid());
        textBook.setId(UUID.randomUUID().toString());
        textBook.setPresstime(textBook.getPresstime().substring(0,9));
        int i = docService.addTextBook(textBook);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 上传实施大纲
     * @param multipartFiles
     * @param courseid
     * @return
     * @throws SocketException
     * @throws IOException
     */
    @Log("上传实施大纲")
    @RequiresPermissions("/document")
    @RequestMapping("/uploadDoc")
    public String uploadDoc(MultipartFile[] multipartFiles, String courseid) throws SocketException, IOException {
        String rootPath = ResourceUtils.getURL("resources").getPath();
        File fileDir = new File(rootPath);

        if (!fileDir.exists() && !fileDir.isDirectory()) {

            fileDir.mkdirs();
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        try {
            if (multipartFiles != null && multipartFiles.length > 0) {
                for(int i = 0;i<multipartFiles.length;i++){
                    try {
                        if(multipartFiles[i].getOriginalFilename().split("\\.")[1].equals("doc")||multipartFiles[i].getOriginalFilename().split("\\.")[1].equals("docx")){
                            String storagePath = rootPath+multipartFiles[i].getOriginalFilename().split("\\.")[0]+courseid+"."+multipartFiles[i].getOriginalFilename().split("\\.")[1];
                            logger.info("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                    +"，保存的路径为：" + storagePath);
                            Document document = docService.getDocumentById(courseid);
                            if(document != null){
                                docService.deleteDocument(courseid);
                                File file = new File(document.getPath());
                                if(file!=null) {
                                    file.delete();
                                }
                            }
                            document = new Document();
                            document.setId(UUID.randomUUID().toString());
                            document.setCourseid(courseid);
                            document.setPath(storagePath);
                            Date date = new Date();
                            document.setUploadtime(date);
                            document.setDstate("0");
                            docService.addDocument(document);
                            System.out.println(storagePath);
                            multipartFiles[i].transferTo(new File(storagePath));
                            result.put("state",200);
                        }else{
                            result.put("state",303);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                         result.put("stete",404);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("state",404);
        }
        //前端可以通过状态码，判断文件是否上传成功
        return JSON.toJSONString(result);
    }

    /**
     * 得到课程所属的大纲和教材
     * @param id
     * @return
     */
    @Log("查看大纲和教材")
    @RequiresPermissions(value = {"/showDoc","/showCourseInfo","/showGradeInfo","/logGradeInfo"},logical = Logical.OR)
    @RequestMapping("/getCourserAndDoc")
    public String getCourserAndDoc(String id){
        CourseDoc courseDoc = docService.getCourserAndDoc(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(courseDoc==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("courseDoc",courseDoc);
        return JSON.toJSONString(result);
    }

    /**
     * 得到所有审核的教材
     * @return
     */
    @Log("查看审核的教材")
    @RequiresPermissions("/textbookAudit")
    @RequestMapping("/getTextbookList")
    public String getTextbookList(int pageNum, int pageSize, String tstate, String semester){
        List<CourseDoc> textbookList = docService.getTextbookList((pageNum-1)*pageSize,pageSize,tstate,semester);
        int number = docService.getTextbookListCount(tstate,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(textbookList==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("textbookList",textbookList);
        result.put("number",number);
        return JSON.toJSONString(result);
    }

    /**
     * 接受教材审核
     * @param id
     * @return
     */
    @Log("接受教材审核")
    @RequiresPermissions("/acceptTextbook")
    @RequestMapping("/acceptBookAudit")
    public String acceptBookAudit(String id){
        int i = docService.acceptBookAudit(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 拒绝教材审核
     * @param id
     * @return
     */
    @Log("拒绝教材审核")
    @RequiresPermissions("/refuseTextbook")
    @RequestMapping("/refuseBookAudit")
    public String refuseBookAudit(String id){
        int i = docService.refuseBookAudit(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到所有实施大纲申请
     * @param pageNum
     * @param pageSize
     * @param dstate
     * @param semester
     * @return
     */
    @Log("查看实施大纲申请列表")
    @RequiresPermissions("/docAudit")
    @RequestMapping("/getDocList")
    public String getDocList(int pageNum, int pageSize, String dstate, String semester){
        List<CourseDoc> docList = docService.getDocList((pageNum-1)*pageSize,pageSize,dstate,semester);
        for(CourseDoc c : docList){
            c.setPath(c.getPath().split("/")[c.getPath().split("/").length-1]);
        }
        int number = docService.getDocListCount(dstate,semester);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(docList==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("docList",docList);
        result.put("number",number);
        return JSON.toJSONString(result);
    }

    /**
     * 实施大纲下载
     * @param response
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    @Log("实施大纲下载")
    @RequiresPermissions(value = {"/document","/downloadDoc"},logical = Logical.OR)
    @RequestMapping("/downloadDoc")
    public String downloadDoc(HttpServletResponse response , String id) throws UnsupportedEncodingException {
        CourseDoc courseDoc = docService.getDocByCourseId(id);
        File file = new File(courseDoc.getPath());
        String fileName = courseDoc.getPath().split("/")[courseDoc.getPath().split("/").length-1];
        if (file.exists()) {

            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length",""+file.length());
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                logger.info("文件下载完成！！");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过实施大纲
     * @param id
     * @return
     */
    @Log("通过实施大纲申请")
    @RequiresPermissions("/acceptDoc")
    @RequestMapping("/acceptDocAudit")
    public String acceptDocAudit(String id){
        int i = docService.acceptDocAudit(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 不通过实施大纲
     * @param id
     * @return
     */
    @Log("拒绝实施大纲申请")
    @RequiresPermissions("/refuseDoc")
    @RequestMapping("/refuseDocAudit")
    public String refuseDocAudit(String id,String refuseresult){
        int i = docService.refuseDocAudit(id,refuseresult);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 调停课
     * @param adjustClass
     * @return
     */
    @Log("调停课申请")
    @RequiresPermissions("/addAdjust")
    @RequestMapping("/adjustClass")
    public String adjustClass(@RequestBody AdjustClass adjustClass){
        adjustClass.setId(UUID.randomUUID().toString());
        adjustClass.setCreatetime(new Date());
        HashMap<String,Object> result = new HashMap<String,Object>();
        Course course = courseService.getCourseById(adjustClass.getAcourseid());
        List<Course> courses = courseService.getClassRoomCourse(adjustClass.getAspotflag(),course.getSemester());
        //判断教室时间段有没有重合
        for(Course c : courses){
            if(!(Integer.parseInt(c.getWeeknum().split(",")[0])>Integer.parseInt(adjustClass.getAweeknum().split(",")[1])||Integer.parseInt(c.getWeeknum().split(",")[1])<Integer.parseInt(adjustClass.getAweeknum().split(",")[0]))){
                if(c.getTimeflag().split(",")[0].equals(adjustClass.getAtimeflag().split(",")[0])){
                    if(!(Integer.parseInt(c.getTimeflag().split(",")[1])>Integer.parseInt(adjustClass.getAtimeflag().split(",")[2])||Integer.parseInt(c.getTimeflag().split(",")[2])<Integer.parseInt(adjustClass.getAtimeflag().split(",")[1]))){
                        //排课时间重合
                        result.put("state",500);
                        return JSON.toJSONString(result);
                    }
                }
            }
        }
        docService.deleteAdjustClass(adjustClass.getAcourseid());
        int i = docService.addAdjustClass(adjustClass);
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 得到调停课申请列表
     * @param pageNum
     * @param pageSize
     * @param id
     * @param astate
     * @param semester
     * @return
     */
    @Log("查看调停课列表")
    @RequiresPermissions(value = {"/adjustClass","/adjustAudit"},logical = Logical.OR)
    @RequestMapping("/getAdjustClassList")
    public String getAdjustClassList(int pageNum, int pageSize,String id, String astate, String semester){
        List<AdjustClassAndCourse> adjustClass = docService.getAdjustClassList((pageNum-1)*pageSize,pageSize,id,semester,astate);
        int number = docService.getAdjustClassListCount(id,semester,astate);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(adjustClass==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("adjust",adjustClass);
        result.put("number",number);
        return JSON.toJSONString(result);
    }

    /**
     * 得到修改申请通过id
     * @param id
     * @return
     */
    @RequiresPermissions("/editAdjust")
    @RequestMapping("/getAdjustById")
    public String getAdjustById(String id){
        AdjustClassAndCourse adjustClassAndCourse = docService.getAdjustById(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(adjustClassAndCourse==null){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        result.put("adjust",adjustClassAndCourse);
        return JSON.toJSONString(result);
    }

    /**
     * 修改调停课申请
     * @param adjustClass
     * @return
     */
    @Log("修改调停课申请")
    @RequiresPermissions("/editAdjust")
    @RequestMapping("/editAdjust")
    public String editAdjust(@RequestBody AdjustClass adjustClass){
        HashMap<String,Object> result = new HashMap<String,Object>();
        Course course = courseService.getCourseById(adjustClass.getAcourseid());
        List<Course> courses = courseService.getClassRoomCourse(adjustClass.getAspotflag(),course.getSemester());
        docService.deleteAdjustClass(adjustClass.getAcourseid());
        int i = docService.addAdjustClass(adjustClass);
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除调停课申请
     * @param id
     * @return
     */
    @Log("删除调停课申请")
    @RequiresPermissions("/deleteAdjust")
    @RequestMapping("/deleteAdjust")
    public String deleteAdjust(String id){
        docService.deleteAdjustClass(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("state",200);
        return JSON.toJSONString(result);
    }

    /**
     * 接收调课申请
     * @param id
     * @return
     */
    @Log("通过调课申请")
    @RequiresPermissions("/acceptAdjust")
    @RequestMapping("/acceptAdjust")
    public String acceptAdjust(String id){
        int i = docService.acceptAdjust(id);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 拒绝调课申请
     * @param id
     * @return
     */
    @Log("拒绝调课申请")
    @RequiresPermissions("/refuseAdjust")
    @RequestMapping("/refuseAdjust")
    public String refuseAdjust(String id,String refuseresult){
        int i = docService.refuseAdjust(id,refuseresult);
        HashMap<String,Object> result = new HashMap<String,Object>();
        if(i==0){
            result.put("state",404);
        }else{
            result.put("state",200);
        }
        return JSON.toJSONString(result);
    }
}
