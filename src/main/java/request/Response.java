package request;

import com.example.diploma.Project;
import com.example.diploma.User;

import java.util.ArrayList;

public class Response {
    ServReaction servReaction;
    ArrayList<Project> projects=new ArrayList<Project>();
    ArrayList<User> users=new ArrayList<User>();

    public Response() {}

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
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
