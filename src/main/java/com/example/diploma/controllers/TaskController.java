package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class TaskController {
    @FXML
    private ListView<String> listField;

    @FXML
    private ComboBox<String> taskForStatusField;

    @FXML
    private Button changeStatus;


    public Socket socket;
    String userRole;
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
    void createTask(ActionEvent event) throws IOException {
//        FXMLLoader loader=new FXMLLoader(getClass().getResource("CreateTaskOldVersion.fxml"));
//        Parent root=loader.load();
//
//        TaskController taskController=loader.getController();
//        taskController.getTasks(socket);
//
//        contentPane.getChildren().removeAll();
//        contentPane.getChildren().setAll(root);

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreateTaskOldVersion.fxml"));
        Parent root1=(Parent) fxmlLoader.load();

        CreateTaskController сreateTaskController=fxmlLoader.getController();
        //сreateTaskController.getInfoForTasks(socket);

        Stage stage=new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void initialize() {

    }

    public void getTasks(Socket socket, String userRole) throws IOException {
        this.socket=socket;
        this.userRole=userRole;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYUSER);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByUser=msg.getTasks();

        for (Task task : tasksByUser) {
            taskNameByUser.add(task.getTaskName());
        }

        ObservableList<String> allTasks = FXCollections.observableArrayList(taskNameByUser);
        listField.setItems(allTasks);


        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYUSER);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByUser=msg.getTasks();

        for (Task task : tasksByUser) {
            taskNameByUser.add(task.getTaskName());
        }

        ObservableList<String> listTasksByUser = FXCollections.observableArrayList(taskNameByUser);
        taskForStatusField.setItems(listTasksByUser);
    }

    @FXML
    void change_status(ActionEvent event) {
        String taskName = taskForStatusField.getSelectionModel().getSelectedItem().toString();

        int taskId=0;

        for (Task task : tasksByUser) {
            if(task.getTaskName().equals(taskName)){
                taskId=task.getTaskId();
            }
        }
        String status = "выполнена";

        Task task=new Task();
        task.setTaskId(taskId);
        task.setTaskName(taskName);
        task.setStatus(status);

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATESTATUS, task);
        sender.sendRequest(req);
    }

}
