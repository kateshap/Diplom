package com.example.diploma;

public class ProjectUsers {
    public int projectusersid;
    public int userid;
    public int projectid;
    public String role;

    public ProjectUsers(int userid, int projectid) {
        this.userid = userid;
        this.projectid = projectid;
    }

    public ProjectUsers() {
    }

    public int getProjectusersid() {
        return projectusersid;
    }

    public void setProjectusersid(int projectusersid) {
        this.projectusersid = projectusersid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
