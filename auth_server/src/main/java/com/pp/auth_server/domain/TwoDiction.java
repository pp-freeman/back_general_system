package com.pp.auth_server.domain;

public class TwoDiction {
    private Semester semester;
    private Semester oldSemester;

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setOldSemester(Semester oldSemester) {
        this.oldSemester = oldSemester;
    }

    public Semester getOldSemester() {
        return oldSemester;
    }

    public Semester getSemester() {
        return semester;
    }


}
