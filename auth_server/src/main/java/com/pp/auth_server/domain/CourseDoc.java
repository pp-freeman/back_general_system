package com.pp.auth_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CourseDoc {
    private String coursecode;
    private String coursename;
    private String coursetype;
    private String begingrade;
    private String beginacademy;
    private String semester;
    private String execourses;
    private String tline;
    private String timeflag;
    private String spotflag;
    private Integer attendexpect;
    private Integer atusum;
    private String creadit;
    private String period;
    private String weeknum;
    private String proportion;
    private String beginweek;
    private String endweek;
    private String beginclass;
    private String endclass;
    private String day;
    private String path;
    private String dstate;
    @JsonFormat(timezone = "CMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadtime;
    private String isbn;
    private String name;
    private String author;
    private String press;
    private String presstime;
    private String price;
    private String tstate;
    private String tname;
    private String refuseresult;

    public void setRefuseresult(String refuseresult) {
        this.refuseresult = refuseresult;
    }

    public String getRefuseresult() {
        return refuseresult;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTname() {
        return tname;
    }

    public void setCoursecode(String coursecode) {
        this.coursecode = coursecode;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }

    public void setBegingrade(String begingrade) {
        this.begingrade = begingrade;
    }

    public void setBeginacademy(String beginacademy) {
        this.beginacademy = beginacademy;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setExecourses(String execourses) {
        this.execourses = execourses;
    }

    public void setTline(String tline) {
        this.tline = tline;
    }

    public void setTimeflag(String timeflag) {
        this.timeflag = timeflag;
    }

    public void setSpotflag(String spotflag) {
        this.spotflag = spotflag;
    }

    public void setAttendexpect(Integer attendexpect) {
        this.attendexpect = attendexpect;
    }

    public void setAtusum(Integer atusum) {
        this.atusum = atusum;
    }

    public void setCreadit(String creadit) {
        this.creadit = creadit;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setWeeknum(String weeknum) {
        this.weeknum = weeknum;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public void setBeginweek(String beginweek) {
        this.beginweek = beginweek;
    }

    public void setEndweek(String endweek) {
        this.endweek = endweek;
    }

    public void setBeginclass(String beginclass) {
        this.beginclass = beginclass;
    }

    public void setEndclass(String endclass) {
        this.endclass = endclass;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDstate(String dstate) {
        this.dstate = dstate;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public void setPresstime(String presstime) {
        this.presstime = presstime;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTstate(String tstate) {
        this.tstate = tstate;
    }

    public String getCoursecode() {
        return coursecode;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getCoursetype() {
        return coursetype;
    }

    public String getBegingrade() {
        return begingrade;
    }

    public String getBeginacademy() {
        return beginacademy;
    }

    public String getSemester() {
        return semester;
    }

    public String getExecourses() {
        return execourses;
    }

    public String getTline() {
        return tline;
    }

    public String getTimeflag() {
        return timeflag;
    }

    public String getSpotflag() {
        return spotflag;
    }

    public Integer getAttendexpect() {
        return attendexpect;
    }

    public Integer getAtusum() {
        return atusum;
    }

    public String getCreadit() {
        return creadit;
    }

    public String getPeriod() {
        return period;
    }

    public String getWeeknum() {
        return weeknum;
    }

    public String getProportion() {
        return proportion;
    }

    public String getBeginweek() {
        return beginweek;
    }

    public String getEndweek() {
        return endweek;
    }

    public String getBeginclass() {
        return beginclass;
    }

    public String getEndclass() {
        return endclass;
    }

    public String getDay() {
        return day;
    }

    public String getPath() {
        return path;
    }

    public String getDstate() {
        return dstate;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPress() {
        return press;
    }

    public String getPresstime() {
        return presstime;
    }

    public String getPrice() {
        return price;
    }

    public String getTstate() {
        return tstate;
    }
}
