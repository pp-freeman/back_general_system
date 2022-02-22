package com.pp.auth_server.domain;


import java.util.List;

/**
 * 用户实体
 */
public class User {
    private String id;
    private String username;
    private String password;
    private String clickPassword;
    private int state;
    private String role;
    private List<Role> roles;
    private String verifyCode;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", role='" + role + '\'' +
                ", roles=" + roles +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClickPassword(String clickPassword) {
        this.clickPassword = clickPassword;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public String getClickPassword() {
        return clickPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getState() {
        return state;
    }

    public String getRole() {
        return role;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
