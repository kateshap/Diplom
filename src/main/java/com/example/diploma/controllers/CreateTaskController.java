package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateTaskController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> projectsField;

    @FXML
    private ComboBox<String> usersField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button createTaskButton;

    public Socket socket;
    Sender sender;
    ArrayList<String> projectNameByAuthor=new ArrayList<String>();
    ArrayList<String> projectNameByUser=new ArrayList<String>();
    ArrayList<String> taskNameByUser=new ArrayList<String>();
    ArrayList<Project> projectsByAuthor;
    ArrayList<Project> projectsByUser;
    ArrayList<Task> tasksByUser;
    ArrayList<String> userFullName=new ArrayList<String>();
    ArrayList<String> userFullNameOnCreateProject=new ArrayList<String>();
    ArrayList<User> users;
    ArrayList<User> usersOnCreateProject;
    Request req;
    Response msg;

    @FXML
    void create_task(ActionEvent event) {
        String taskName = nameField.getText();
        LocalDate date = dateField.getValue();
        String projectNameItem = projectsField.getSelectionModel().getSelectedItem().toString();
        String userNameItem = usersField.getSelectionModel().getSelectedItem().toString();

        int projectId=0;

        for (Project project : projectsByAuthor) {
            if(project.getProjectName().equals(projectNameItem)){
                projectId=project.getProjectId();
            }
        }

        int userId=0;

        for (User user : users) {
            if(user.getFullname().equals(userNameItem)){
                userId=user.getUserId();
            }
        }

        Task task=new Task(taskName,date, projectId,userId,"назначена" );


        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.CREATETASK, task);
        sender.sendRequest(req);
    }

    public void getInfoForTasks(Socket socket) throws IOException {
        this.socket=socket;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSBYAUTHOR);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsByAuthor=msg.getProjects();

        for (Project project : projectsByAuthor) {
            projectNameByAuthor.add(project.getProjectName());
        }

        ObservableList<String> projectItems = FXCollections.observableArrayList(projectNameByAuthor);
        projectsField.setItems(projectItems);//для задач


        req = new Request(ClientsAction.GETUSERS);
        sender.sendRequest(req);
        msg = sender.getResp();
        users=msg.getUsers();

        for (User user : users) {
            userFullName.add(user.getFullname());
        }

        ObservableList<String> userItems = FXCollections.observableArrayList(userFullName);
        usersField.setItems(userItems);//для задач

    }
}
