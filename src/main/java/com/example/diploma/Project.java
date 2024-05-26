package com.example.diploma;

import java.time.LocalDate;

public class Project {
    private String projectName;
    private int userId;
    private int projectId;
    private String program;
    LocalDate beginDate;

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Project(int projectId) {
        this.projectId = projectId;
    }

    public Project(String projectName, String program, LocalDate beginDate) {
        this.projectName = projectName;
        this.program = program;
        this.beginDate = beginDate;
    }


}
