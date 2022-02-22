package com.pp.auth_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AdjustClass {
    private String id;
    private String acourseid;
    private String type;
    private String aresult;
    private String adesc;
    private String aweeknum;
    private String atimeflag;
    private String aspotflag;
    private String astate;
    private String refuseresult;
    @JsonFormat(timezone = "CMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    public void setRefuseresult(String refuseresult) {
        this.refuseresult = refuseresult;
    }

    public String getRefuseresult() {
        return refuseresult;
    }

    public void setAcourseid(String acourseid) {
        this.acourseid = acourseid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAcourseid() {
        return acourseid;
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

    public void setAtimeflag(String atimeflag) {
        this.atimeflag = atimeflag;
    }

    public void setAspotflag(String aspotflag) {
        this.aspotflag = aspotflag;
    }

    public String getAspotflag() {
        return aspotflag;
    }

    public void setAstate(String astate) {
        this.astate = astate;
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



    public String getAstate() {
        return astate;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    @Override
    public String toString() {
        return "AdjustClass{" +
                "acourseid='" + acourseid + '\'' +
                ", type='" + type + '\'' +
                ", aresult='" + aresult + '\'' +
                ", adesc='" + adesc + '\'' +
                ", aweeknum='" + aweeknum + '\'' +
                ", atimeflag='" + atimeflag + '\'' +
                ", aspotflag='" + aspotflag + '\'' +
                ", astate='" + astate + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
