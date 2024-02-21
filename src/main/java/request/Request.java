package request;

import com.example.diploma.Project;
import com.example.diploma.ProjectUsers;
import com.example.diploma.Task;
import com.example.diploma.User;

public class Request {
    ClientsAction clientsAction;
    User user;
    Project project;
    Task task;
    ProjectUsers projectUsers;

    public Request(ClientsAction clientsAction,  ProjectUsers projectUsers) {
        this.clientsAction = clientsAction;
        this.projectUsers = projectUsers;
    }

    public ProjectUsers getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(ProjectUsers projectUsers) {
        this.projectUsers = projectUsers;
    }

    public Request(ClientsAction clientsAction, Task task) {
        this.clientsAction = clientsAction;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setClientsAction(ClientsAction clientsAction) {
        this.clientsAction = clientsAction;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Request(ClientsAction clientsAction, Project project) {
        this.clientsAction = clientsAction;
        this.project = project;
    }

    public Request(ClientsAction clientsAction, User user) {
        this.clientsAction = clientsAction;
        this.user = user;
    }

    public Request(ClientsAction clientsAction) {
        this.clientsAction = clientsAction;
    }

    public Request(User user) {
        this.user = user;
    }

    public ClientsAction getClientsAction() {
        return clientsAction;
    }

    public User getUser() {
        return user;
    }
}
