package com.example.diploma;

import java.util.ArrayList;

public class Queries {
   String program;
   Integer projectsCount;

   String project;
   Integer completedTasks;
   Integer outstandingTasks;

   String user;

    public Queries(String program, Integer projectsCount) {
        this.program = program;
        this.projectsCount = projectsCount;
    }

    public Queries(String project, Integer completedTasks, Integer outstandingTasks) {
        this.project = project;
        this.completedTasks = completedTasks;
        this.outstandingTasks = outstandingTasks;
    }

    public Queries() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Integer getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Integer getOutstandingTasks() {
        return outstandingTasks;
    }

    public void setOutstandingTasks(Integer outstandingTasks) {
        this.outstandingTasks = outstandingTasks;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }


}
