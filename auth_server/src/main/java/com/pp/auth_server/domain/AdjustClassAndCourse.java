package com.pp.auth_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AdjustClassAndCourse {
    private String id;
    private String acourseid;
    private String type;
    private String aresult;
    private String adesc;
    private String aweeknum;
    private String atimeflag;
    private String aspotflag;
    private String astate;
    @JsonFormat(timezone = "CMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
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
    private String refuseresult;
    private String beginweek ="01";
    private String endweek ="01";
    private String beginclass = "01";
    private String endclass = "01";
    private String day = "1";


    public void setId(String id) {
        this.id = id;
    }

    public void setRefuseresult(String refuseresult) {
        this.refuseresult = refuseresult;
    }

    public String getRefuseresult() {
        return refuseresult;
    }

    public void setAcourseid(String acourseid) {
        this.acourseid = acourseid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAresult(String aresult) {
        this.aresult = aresult;
    }

    public void setAdesc(String adesc) {
        this.adesc = adesc;
    }

    public void setAweeknum(String aweeknum) {
        this.aweeknum = aweeknum;
    }

    @Override
    public String toString() {
        return "AdjustClassAndCourse{" +
                "id='" + id + '\'' +
                ", acourseid='" + acourseid + '\'' +
                ", type='" + type + '\'' +
                ", aresult='" + aresult + '\'' +
                ", adesc='" + adesc + '\'' +
                ", aweeknum='" + aweeknum + '\'' +
                ", atimeflag='" + atimeflag + '\'' +
                ", aspotflag='" + aspotflag + '\'' +
                ", astate='" + astate + '\'' +
                ", createtime=" + createtime +
                ", coursecode='" + coursecode + '\'' +
                ", coursename='" + coursename + '\'' +
                ", coursetype='" + coursetype + '\'' +
                ", begingrade='" + begingrade + '\'' +
                ", beginacademy='" + beginacademy + '\'' +
                ", semester='" + semester + '\'' +
                ", execourses='" + execourses + '\'' +
                ", tline='" + tline + '\'' +
                ", timeflag='" + timeflag + '\'' +
                ", spotflag='" + spotflag + '\'' +
                ", attendexpect=" + attendexpect +
                ", atusum=" + atusum +
                ", creadit='" + creadit + '\'' +
                ", period='" + period + '\'' +
                ", weeknum='" + weeknum + '\'' +
                ", proportion='" + proportion + '\'' +
                ", beginweek='" + beginweek + '\'' +
                ", endweek='" + endweek + '\'' +
                ", beginclass='" + beginclass + '\'' +
                ", endclass='" + endclass + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public void setAtimeflag(String atimeflag) {
        this.atimeflag = atimeflag;
    }

    public void setAspotflag(String aspotflag) {
        this.aspotflag = aspotflag;
    }

    public void setAstate(String astate) {
        this.astate = astate;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public String getId() {
        return id;
    }

    public String getAcourseid() {
        return acourseid;
    }

    public String getType() {
        return type;
    }

    public String getAresult() {
        return aresult;
    }

    public String getAdesc() {
        return adesc;
    }

    public String getAweeknum() {
        return aweeknum;
    }

    public String getAtimeflag() {
        return atimeflag;
    }

    public String getAspotflag() {
        return aspotflag;
    }

    public String getAstate() {
        return astate;
    }

    public Date getCreatetime() {
        return createtime;
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
}
