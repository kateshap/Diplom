package request;

import com.example.diploma.Project;
import com.example.diploma.Queries;
import com.example.diploma.Task;
import com.example.diploma.User;

import java.util.ArrayList;

public class Response {
    ServReaction servReaction;
    ArrayList<Project> projects=new ArrayList<Project>();
    ArrayList<User> users=new ArrayList<User>();
    ArrayList<Task> tasks=new ArrayList<Task>();
    ArrayList<Queries> queries=new ArrayList<>();

    String Message;
    User user;
    String userRole;

    public Response() {
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Response(ServReaction success, User user) {}

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

    public Response(ServReaction servReaction,String userRole) {
        this.servReaction = servReaction;
        this.userRole=userRole;
    }

    public ArrayList<Queries> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<Queries> queries) {
        this.queries = queries;
    }

    public ServReaction getServReaction() {
        return servReaction;
    }

    public void setServReaction(ServReaction servReaction) {
        this.servReaction = servReaction;
    }
}
