package com.pp.workflow.domain;

import java.io.Serializable;

public class TbEvection implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.evectionName
     *
     * @mbggenerated
     */
    private String evectionname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.num
     *
     * @mbggenerated
     */
    private Double num;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.destination
     *
     * @mbggenerated
     */
    private String destination;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.begindate
     *
     * @mbggenerated
     */
    private String begindate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.enddate
     *
     * @mbggenerated
     */
    private String enddate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.reson
     *
     * @mbggenerated
     */
    private String reson;

    // 申请类型
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.state
     *
     * @mbggenerated
     */
    private String state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_evection.userid
     *
     * @mbggenerated
     */
    private String userid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_evection
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.id
     *
     * @return the value of tb_evection.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.id
     *
     * @param id the value for tb_evection.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.evectionName
     *
     * @return the value of tb_evection.evectionName
     *
     * @mbggenerated
     */
    public String getEvectionname() {
        return evectionname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.evectionName
     *
     * @param evectionname the value for tb_evection.evectionName
     *
     * @mbggenerated
     */
    public void setEvectionname(String evectionname) {
        this.evectionname = evectionname == null ? null : evectionname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.num
     *
     * @return the value of tb_evection.num
     *
     * @mbggenerated
     */
    public Double getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.num
     *
     * @param num the value for tb_evection.num
     *
     * @mbggenerated
     */
    public void setNum(Double num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.destination
     *
     * @return the value of tb_evection.destination
     *
     * @mbggenerated
     */
    public String getDestination() {
        return destination;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.destination
     *
     * @param destination the value for tb_evection.destination
     *
     * @mbggenerated
     */
    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.begindate
     *
     * @return the value of tb_evection.begindate
     *
     * @mbggenerated
     */
    public String getBegindate() {
        return begindate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.begindate
     *
     * @param begindate the value for tb_evection.begindate
     *
     * @mbggenerated
     */
    public void setBegindate(String begindate) {
        this.begindate = begindate == null ? null : begindate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.enddate
     *
     * @return the value of tb_evection.enddate
     *
     * @mbggenerated
     */
    public String getEnddate() {
        return enddate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.enddate
     *
     * @param enddate the value for tb_evection.enddate
     *
     * @mbggenerated
     */
    public void setEnddate(String enddate) {
        this.enddate = enddate == null ? null : enddate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.reson
     *
     * @return the value of tb_evection.reson
     *
     * @mbggenerated
     */
    public String getReson() {
        return reson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.reson
     *
     * @param reson the value for tb_evection.reson
     *
     * @mbggenerated
     */
    public void setReson(String reson) {
        this.reson = reson == null ? null : reson.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.state
     *
     * @return the value of tb_evection.state
     *
     * @mbggenerated
     */
    public String getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.state
     *
     * @param state the value for tb_evection.state
     *
     * @mbggenerated
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_evection.userid
     *
     * @return the value of tb_evection.userid
     *
     * @mbggenerated
     */
    public String getUserid() {
        return userid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_evection.userid
     *
     * @param userid the value for tb_evection.userid
     *
     * @mbggenerated
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_evection
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", evectionname=").append(evectionname);
        sb.append(", num=").append(num);
        sb.append(", destination=").append(destination);
        sb.append(", begindate=").append(begindate);
        sb.append(", enddate=").append(enddate);
        sb.append(", reson=").append(reson);
        sb.append(", state=").append(state);
        sb.append(", userid=").append(userid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}