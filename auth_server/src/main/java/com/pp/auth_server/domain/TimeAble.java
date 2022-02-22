package com.pp.auth_server.domain;

public class TimeAble {
    private String day;
    private String length;
    private String name;
    private String period;
    private String room;
    private String teacher;
    private String type;
    private String week;

    public TimeAble() {
    }

    public TimeAble(String day, String length, String name, String period, String room, String teacher, String type, String week) {
        this.day = day;
        this.length = length;
        this.name = name;
        this.period = period;
        this.room = room;
        this.teacher = teacher;
        this.type = type;
        this.week = week;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public String getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getType() {
        return type;
    }

    public String getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return "TimeAble{" +
                "day='" + day + '\'' +
                ", length='" + length + '\'' +
                ", name='" + name + '\'' +
                ", period='" + period + '\'' +
                ", room='" + room + '\'' +
                ", teacher='" + teacher + '\'' +
                ", type='" + type + '\'' +
                ", week='" + week + '\'' +
                '}';
    }
}
