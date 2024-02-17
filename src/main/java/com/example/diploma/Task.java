package com.example.diploma;

import java.util.Date;

public class Task {
    private String taskName;
    private Date executeDate;
    private int projectId;
    private int userId;
    private String status;

    public Task(String taskName, Date executeDate, int projectId, int userId, String status) {
        this.taskName = taskName;
        this.executeDate = executeDate;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
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
