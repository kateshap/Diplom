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
import javafx.scene.input.MouseEvent;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class CreateProjectController {
//    @FXML
//    private TextField textFieldProjectName;
//
//    @FXML
//    private Button CreateProjectButton;
//
//    @FXML
//    private ComboBox<?> usersFieldOnCreateProject;
//
//    @FXML
//    private ComboBox<?> projectsFieldOnCreateProject;
//
//    @FXML
//    private Button AddUserButton;

    @FXML
    private TextField projectNameField;

    @FXML
    private Button createProjectButton;

    @FXML
    private TextField programField;

    @FXML
    private DatePicker beginDateField;


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

    void initialize() throws IOException {
//        req = new Request(ClientsAction.GETUSERS);
//        sender.sendRequest(req);
//        msg = sender.getResp();
//        users=msg.getUsers();
//
//        for (User user : users) {
//            userFullName.add(user.getFullname());
//        }
//
//        ObservableList<String> userItems = FXCollections.observableArrayList(userFullName);
//        usersField.setItems(userItems);
//        //usersFieldOnCreateProject.setItems(userItems);
//
//        projectsFieldOnCreateProject.setItems(projectItems);

    }

    @FXML
    void createNewProject(ActionEvent event) {
        String projectName = projectNameField.getText();
        String program = programField.getText();
        LocalDate date = beginDateField.getValue();

        Project project=new Project(projectName,program,date);

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.CREATEPROJECT, project);
        sender.sendRequest(req);
    }

    @FXML
    void updateUsers(MouseEvent event) {

    }

}