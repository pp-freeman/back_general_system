package com.pp.auth_server.domain;

public class Course {
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


    @Override
    public String toString() {
        return "Course{" +
                "coursecode='" + coursecode + '\'' +
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
                '}';
    }

    public void setAttendexpect(Integer attendexpect) {
        this.attendexpect = attendexpect;
    }

    public void setAtusum(Integer atusum) {
        this.atusum = atusum;
    }

    public Integer getAttendexpect() {
        return attendexpect;
    }

    public Integer getAtusum() {
        return atusum;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getProportion() {
        return proportion;
    }

    public void setWeeknum(String weeknum) {
        this.weeknum = weeknum;
    }

    public String getWeeknum() {
        return weeknum;
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

    public String getSpotflag() {
        return spotflag;
    }



    public String getCreadit() {
        return creadit;
    }

    public String getPeriod() {
        return period;
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


    public void setCreadit(String creadit) {
        this.creadit = creadit;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
