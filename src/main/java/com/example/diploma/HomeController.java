package com.example.diploma;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import request.*;

public class HomeController {

    @FXML
    private TextField textFieldProjectName;

    @FXML
    private Button CreateProjectButton;

    @FXML
    private AnchorPane HomeScene;

    @FXML
    private Button createTaskButton;

    @FXML
    private TextField nameField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox usersField;

    @FXML
    private ComboBox projectsField;

    private Socket socket;
    Sender sender;
    ArrayList<String> projectName=new ArrayList<String>();
    ArrayList<Project> projects;
    ArrayList<String> userFullName=new ArrayList<String>();
    ArrayList<User> users;


    @FXML
    void initialize() throws IOException {
        socket = Data.socket;

        sender = new Sender(socket);
        Request req = new Request(ClientsAction.GETPROJECTS);
        sender.sendRequest(req);
        Response msg = sender.getResp();

        projects=msg.getProjects();

        for (Project project : projects) {
            projectName.add(project.getProjectName());
        }

        ObservableList<String> projectItems = FXCollections.observableArrayList(projectName);
        projectsField.setItems(projectItems);

        req = new Request(ClientsAction.GETUSERS);
        sender.sendRequest(req);
        msg = sender.getResp();
        users=msg.getUsers();

        for (User user : users) {
            userFullName.add(user.getFullname());
        }

        ObservableList<String> userItems = FXCollections.observableArrayList(userFullName);
        usersField.setItems(userItems);

        CreateProjectButton.setOnAction(event ->{
            try {
                createNewProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //openNewScene("/com/example/diploma/Home.fxml");
            //SignIn.fxml
        });

    }

    @FXML
    void create_task(ActionEvent event) {
        String taskName = nameField.getText();
        LocalDate date = dateField.getValue();
        String projectNameItem = projectsField.getSelectionModel().getSelectedItem().toString();
        String userNameItem = usersField.getSelectionModel().getSelectedItem().toString();

        int projectId=0;

        for (Project project : projects) {
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

    private void createNewProject() throws IOException {

        String projectName = textFieldProjectName.getText();

        Project project=new Project(projectName);

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.CREATEPROJECT, project);
        sender.sendRequest(req);

//        Response msg = sender.getResp();
//
//        if (msg.getServReaction() == ServReaction.SUCCESS) {
//            openNewScene("/com/example/diploma/Home.fxml");
//        }
    }
}