package com.example.diploma;

import java.time.LocalDate;

public class Task {
    private int taskId;
    private String taskName;
    private LocalDate beginDate;
    private LocalDate executeDate;
    private int duration;
    private int projectId;
    private int userId;
    private String status;
    private int parentId;
    private int childId;
    private int percent;
    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Task(int taskId, String taskName, LocalDate beginDate, LocalDate executeDate, int duration, int projectId, int userId, String status, int parentId, int childId, int percent,int delay) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.beginDate = beginDate;
        this.executeDate = executeDate;
        this.duration = duration;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
        this.parentId = parentId;
        this.childId = childId;
        this.percent = percent;
        this.delay=delay;
    }

    public Task( String taskName, LocalDate beginDate, LocalDate executeDate, int duration, int projectId, int userId, String status, int parentId) {
        this.taskName = taskName;
        this.beginDate = beginDate;
        this.executeDate = executeDate;
        this.duration = duration;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
        this.parentId = parentId;
    }

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
