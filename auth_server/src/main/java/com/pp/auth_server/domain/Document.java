package com.pp.auth_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Document {
    private String id;
    private String path;
    private String courseid;
    private String dstate;
    private String refuseresult;
    @JsonFormat(timezone = "CMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadtime;

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", courseid='" + courseid + '\'' +
                ", state='" + dstate + '\'' +
                ", uploadtime='" + uploadtime + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }


    public void setRefuseresult(String refuseresult) {
        this.refuseresult = refuseresult;
    }

    public String getRefuseresult() {
        return refuseresult;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setDstate(String dstate) {
        this.dstate = dstate;
    }

    public String getDstate() {
        return dstate;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Date getUploadtime() {
        return uploadtime;
    }
}
