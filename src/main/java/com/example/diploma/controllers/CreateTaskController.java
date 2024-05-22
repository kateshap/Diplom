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
import javafx.scene.control.TextField;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CreateTaskController {
    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> usersField;

    @FXML
    private Button createTaskButton;

    @FXML
    private TextField durationField;

    @FXML
    private ComboBox<String> dependencyField;

    public Socket socket;
    Sender sender;
    ArrayList<String> projectNameByAuthor=new ArrayList<String>();
    ArrayList<String> projectNameByUser=new ArrayList<String>();
    ArrayList<String> taskNameByProject=new ArrayList<String>();
    ArrayList<Project> projectsByAuthor;
    ArrayList<Project> projectsByUser;
    ArrayList<Task> tasksByProject;
    ArrayList<String> userFullName=new ArrayList<String>();
    ArrayList<String> userFullNameOnCreateProject=new ArrayList<String>();
    ArrayList<User> users;
    ArrayList<User> usersOnCreateProject;
    Request req;
    Response msg;
    Project project;

    @FXML
    void create_task(ActionEvent event) throws IOException {
        String taskName = nameField.getText();
        //LocalDate date = dateField.getValue();
        String taskNameItem = dependencyField.getSelectionModel().getSelectedItem().toString();
        String userNameItem = usersField.getSelectionModel().getSelectedItem().toString();
        String taskDuration=durationField.getText();

//        int projectId=0;
//
//        for (Project project : projectsByAuthor) {
//            if(project.getProjectName().equals(taskNameItem)){
//                projectId=project.getProjectId();
//            }
//        }

        int userId=0;

        for (User user : users) {
            if(user.getFullname().equals(userNameItem)){
                userId=user.getUserId();
            }
        }

        int parentId=0;
        LocalDate beginDate = null;
        LocalDate executeDate = null;

        for (Task task : tasksByProject) {
            if(task.getTaskName().equals(taskNameItem)){
                parentId=task.getTaskId();
                beginDate=task.getExecuteDate();
                executeDate= beginDate.plusDays(Integer.parseInt(taskDuration));

            }
        }

        Task task=new Task(taskName,beginDate,executeDate, Integer.parseInt(taskDuration), project.getProjectId(),userId,"назначена", parentId);


        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.CREATETASK, task);
        sender.sendRequest(req);
    }

    public void getInfoForTasks(Socket socket,Project project) throws IOException {
        this.socket=socket;
        this.project=project;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYPROJECT);
        req.setProject(project);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByProject=msg.getTasks();

        for (Task task : tasksByProject) {
            taskNameByProject.add(task.getTaskName());
        }

        ObservableList<String> allTasks = FXCollections.observableArrayList(taskNameByProject);
        dependencyField.setItems(allTasks);


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
