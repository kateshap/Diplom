package request;

import com.example.diploma.Project;

import java.util.ArrayList;

public class Response {
    ServReaction servReaction;
    ArrayList<Project> projects;
    ArrayList<String> users;

    public Response() {}

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public Response(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public Response(ServReaction servReaction) {
        this.servReaction = servReaction;
    }

    public ServReaction getServReaction() {
        return servReaction;
    }

    public void setServReaction(ServReaction servReaction) {
        this.servReaction = servReaction;
    }
}
