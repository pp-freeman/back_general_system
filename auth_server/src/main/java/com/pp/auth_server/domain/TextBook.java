package com.pp.auth_server.domain;

public class TextBook {
    private String id;
    private String isbn;
    private String name;
    private String author;
    private String press;
    private String presstime;
    private String price;
    private String tstate;
    private String courseid;

    public void setId(String id) {
        this.id = id;
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

    public String getTstate() {
        return tstate;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getId() {
        return id;
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



    public String getCourseid() {
        return courseid;
    }
}
