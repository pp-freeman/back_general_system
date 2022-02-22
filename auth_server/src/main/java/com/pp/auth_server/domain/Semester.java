package com.pp.auth_server.domain;

public class Semester {
    private String id;
    private String semesterno;
    private String semestername;
    private String state;

    @Override
    public String toString() {
        return "Semester{" +
                "semesterno='" + semesterno + '\'' +
                ", semestername='" + semestername + '\'' +
                '}';
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setSemesterno(String semesterno) {
        this.semesterno = semesterno;
    }

    public void setSemestername(String semestername) {
        this.semestername = semestername;
    }

    public String getSemesterno() {
        return semesterno;
    }

    public String getSemestername() {
        return semestername;
    }
}
