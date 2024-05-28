package com.example.diploma;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    String projectName;
    int userId;
    int projectId;
    String program;
    LocalDate beginDate;
    LocalDate executeDate;
    String directorName;
    String managerName;
    ArrayList<String> teamMembers;
    int tasksCount;

    public Project() {}

    public Project(int projectId) {
        this.projectId = projectId;
    }

    public Project(String projectName, String program, LocalDate beginDate) {
        this.projectName = projectName;
        this.program = program;
        this.beginDate = beginDate;
    }

    public Project(String projectName,  LocalDate beginDate,LocalDate executeDate, String program,String directorName,String managerName) {
        this.projectName = projectName;
        this.beginDate = beginDate;
        this.executeDate = executeDate;
        this.program = program;
        this.directorName=directorName;
        this.managerName=managerName;
    }

    public LocalDate getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(LocalDate executeDate) {
        this.executeDate = executeDate;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public ArrayList<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

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
}
