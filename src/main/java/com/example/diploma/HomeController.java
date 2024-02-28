package com.example.diploma;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private ListView<String> listViewField;

    @FXML
    private ComboBox usersFieldOnCreateProject;

    @FXML
    private ComboBox projectsFieldOnCreateProject;

    @FXML
    private Button AddUserButton;

    @FXML
    private ComboBox taskForStatusField;

    @FXML
    private Button changeStatus;


    private Socket socket;
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
    void initialize() throws IOException {
        socket = Data.socket;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSBYAUTHOR);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsByAuthor=msg.getProjects();

        for (Project project : projectsByAuthor) {
            projectNameByAuthor.add(project.getProjectName());
        }

        ObservableList<String> projectItems = FXCollections.observableArrayList(projectNameByAuthor);
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
        //usersFieldOnCreateProject.setItems(userItems);

        projectsFieldOnCreateProject.setItems(projectItems);



        CreateProjectButton.setOnAction(event ->{
            try {
                createNewProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //openNewScene("/com/example/diploma/Home.fxml");
            //SignIn.fxml
        });

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSBYUSER);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsByUser=msg.getProjects();

        for (Project project : projectsByUser) {
            projectNameByUser.add(project.getProjectName());
        }

        ObservableList<String> allProjects = FXCollections.observableArrayList(projectNameByUser);
        listViewField.setItems(allProjects);

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
    void updateUsers(MouseEvent event) throws IOException {
        usersFieldOnCreateProject.getItems().clear();
        userFullNameOnCreateProject.clear();
        String projectItem = projectsFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
        int projectId=0;

        for (Project project : projectsByAuthor) {
            if(project.getProjectName().equals(projectItem)){
                projectId=project.getProjectId();
            }
        }
        Project project=new Project(projectId);

        req = new Request(ClientsAction.GETUSERSBYPROJECT,project);
        sender.sendRequest(req);
        msg = sender.getResp();
        usersOnCreateProject=msg.getUsers();

        for (User user : usersOnCreateProject) {
            userFullNameOnCreateProject.add(user.getFullname());
        }
        ObservableList<String> userItemsOnCreateProject = FXCollections.observableArrayList(userFullNameOnCreateProject);
        usersFieldOnCreateProject.setItems(userItemsOnCreateProject);

    }

    @FXML
    void addProjectUsers(ActionEvent event) {
        String projectItem = projectsFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
        String userItem = usersFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
        int projectId=0;

        for (Project project : projectsByAuthor) {
            if(project.getProjectName().equals(projectItem)){
                projectId=project.getProjectId();
            }
        }

        int userId=0;

        for (User user : usersOnCreateProject) {
            if(user.getFullname().equals(userItem)){
                userId=user.getUserId();
            }
        }

        ProjectUsers projectUsers=new ProjectUsers(userId, projectId);
        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.ADDPROJECTUSERS, projectUsers);
        sender.sendRequest(req);
    }

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