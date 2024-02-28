package com.example.diploma;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private int taskId;
    private String taskName;
    private LocalDate executeDate;
    private int projectId;
    private int userId;
    private String status;

    public Task(){ }

    public Task(String taskName, LocalDate executeDate, int projectId, int userId, String status) {
        this.taskName = taskName;
        this.executeDate = executeDate;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
    }

    public Task(int taskId, String taskName, LocalDate executeDate, int projectId, int userId, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.executeDate = executeDate;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
    }

    public Task(String taskName, int projectId, int userId, String status) {
        this.taskName = taskName;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
    }

    public int getTaskId() { return taskId; }

    public void setTaskId(int taskId) { this.taskId = taskId; }


    public String getTaskName() { return taskName;}

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(LocalDate executeDate) {
        this.executeDate = executeDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
