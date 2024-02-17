package request;

import com.example.diploma.Project;
import com.example.diploma.User;

public class Request {
    ClientsAction clientsAction;
    User user;
    Project project;

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
