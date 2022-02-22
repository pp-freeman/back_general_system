package com.pp.auth_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class LogDO {
    private String id;
    private String name;
    private String userId;
    private String operation;
    private Integer time;
    private String method;
    private String params;
    private String ip;
    @JsonFormat(timezone = "CMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LogDO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", operation='" + operation + '\'' +
                ", time=" + time +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", ip='" + ip + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getOperation() {
        return operation;
    }

    public Integer getTime() {
        return time;
    }

    public String getMethod() {
        return method;
    }

    public String getParams() {
        return params;
    }

    public String getIp() {
        return ip;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }
}
