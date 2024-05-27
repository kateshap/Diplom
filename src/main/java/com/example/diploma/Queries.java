package com.example.diploma;

import java.util.ArrayList;

public class Queries {
   String program;
   Integer projectsCount;

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

    public Queries(String program, Integer projectsCount) {
        this.program = program;
        this.projectsCount = projectsCount;
    }
}
