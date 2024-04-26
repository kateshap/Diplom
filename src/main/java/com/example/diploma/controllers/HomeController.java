//package com.example.diploma.controllers;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import com.example.diploma.*;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import request.*;
//
//public class HomeController {
//
//    @FXML
//    private TextField textFieldProjectName;
//
//    @FXML
//    private Button CreateProjectButton;
//
//    @FXML
//    private AnchorPane HomeScene;
//
//    @FXML
//    private Button createTaskButton;
//
//    @FXML
//    private TextField nameField;
//
//    @FXML
//    private DatePicker dateField;
//
//    @FXML
//    private ComboBox usersField;
//
//    @FXML
//    private ComboBox projectsField;
//    @FXML
//    private ListView<String> listViewField;
//
//    @FXML
//    private ComboBox usersFieldOnCreateProject;
//
//    @FXML
//    private ComboBox projectsFieldOnCreateProject;
//
//    @FXML
//    private Button AddUserButton;
//
//    @FXML
//    private ComboBox taskForStatusField;
//
//    @FXML
//    private Button changeStatus;
//
//    @FXML
//    private AnchorPane blackPane, buttonPane;
//
//    @FXML
//    private ImageView menu;
//
//
//
//    private Socket socket;
//    Sender sender;
//    ArrayList<String> projectNameByAuthor=new ArrayList<String>();
//    ArrayList<String> projectNameByUser=new ArrayList<String>();
//    ArrayList<String> taskNameByUser=new ArrayList<String>();
//    ArrayList<Project> projectsByAuthor;
//    ArrayList<Project> projectsByUser;
//    ArrayList<Task> tasksByUser;
//    ArrayList<String> userFullName=new ArrayList<String>();
//    ArrayList<String> userFullNameOnCreateProject=new ArrayList<String>();
//    ArrayList<User> users;
//    ArrayList<User> usersOnCreateProject;
//    Request req;
//    Response msg;
//
//    @FXML
//    void initialize() throws IOException {
////        blackPane.setVisible(false);
////
////        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),blackPane);
////        fadeTransition.setFromValue(1);
////        fadeTransition.setToValue(0);
////        fadeTransition.play();
////
////        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),buttonPane);
////        translateTransition.setByX(-600);
////        translateTransition.play();
////
////        menu.setOnMouseClicked(event->{
////            blackPane.setVisible(true);
////
////            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),blackPane);
////            fadeTransition1.setFromValue(0);
////            fadeTransition1.setToValue(0.8);
////            fadeTransition1.play();
////
////            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),buttonPane);
////            translateTransition1.setByX(+600);
////            translateTransition1.play();
////        });
////
////        blackPane.setOnMouseClicked(event->{
////            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),blackPane);
////            fadeTransition1.setFromValue(0.8);
////            fadeTransition1.setToValue(0);
////            fadeTransition1.play();
////
////            fadeTransition1.setOnFinished(event1->{
////                blackPane.setVisible(false);
////            });
////
////            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),buttonPane);
////            translateTransition1.setByX(-600);
////            translateTransition1.play();
////        });
//
//        socket = Data.socket;
//
//        sender = new Sender(socket);
//        req = new Request(ClientsAction.GETPROJECTSBYAUTHOR);
//        sender.sendRequest(req);
//        msg = sender.getResp();
//
//        projectsByAuthor=msg.getProjects();
//
//        for (Project project : projectsByAuthor) {
//            projectNameByAuthor.add(project.getProjectName());
//        }
//
//        ObservableList<String> projectItems = FXCollections.observableArrayList(projectNameByAuthor);
//        projectsField.setItems(projectItems);//для задач
//
//
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
//        usersField.setItems(userItems);//для задач
//
//        projectsFieldOnCreateProject.setItems(projectItems);//для проектов
//
//
//
//        CreateProjectButton.setOnAction(event ->{
//            try {
//                createNewProject();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //openNewScene("/com/example/diploma/Home.fxml");
//            //SignIn.fxml
//        });//для проектов
//
//        sender = new Sender(socket);
//        req = new Request(ClientsAction.GETPROJECTSBYUSER);
//        sender.sendRequest(req);
//        msg = sender.getResp();
//
//        projectsByUser=msg.getProjects();
//
//        for (Project project : projectsByUser) {
//            projectNameByUser.add(project.getProjectName());
//        }
//
//        ObservableList<String> allProjects = FXCollections.observableArrayList(projectNameByUser);
//        listViewField.setItems(allProjects);//список всех проектов для проектов
//
//        sender = new Sender(socket);
//        req = new Request(ClientsAction.GETTASKSBYUSER);
//        sender.sendRequest(req);
//        msg = sender.getResp();
//
//        tasksByUser=msg.getTasks();
//
//        for (Task task : tasksByUser) {
//            taskNameByUser.add(task.getTaskName());
//        }
//
//        ObservableList<String> listTasksByUser = FXCollections.observableArrayList(taskNameByUser);
//        taskForStatusField.setItems(listTasksByUser);
//    }
//
//
//    @FXML
//    void updateUsers(MouseEvent event) throws IOException {
//        usersFieldOnCreateProject.getItems().clear();
//        userFullNameOnCreateProject.clear();
//        String projectItem = projectsFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
//        int projectId=0;
//
//        for (Project project : projectsByAuthor) {
//            if(project.getProjectName().equals(projectItem)){
//                projectId=project.getProjectId();
//            }
//        }
//        Project project=new Project(projectId);
//
//        req = new Request(ClientsAction.GETUSERSBYPROJECT,project);
//        sender.sendRequest(req);
//        msg = sender.getResp();
//        usersOnCreateProject=msg.getUsers();
//
//        for (User user : usersOnCreateProject) {
//            userFullNameOnCreateProject.add(user.getFullname());
//        }
//        ObservableList<String> userItemsOnCreateProject = FXCollections.observableArrayList(userFullNameOnCreateProject);
//        usersFieldOnCreateProject.setItems(userItemsOnCreateProject);
//
//    }
//
//    @FXML
//    void addProjectUsers(ActionEvent event) {
//        String projectItem = projectsFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
//        String userItem = usersFieldOnCreateProject.getSelectionModel().getSelectedItem().toString();
//        int projectId=0;
//
//        for (Project project : projectsByAuthor) {
//            if(project.getProjectName().equals(projectItem)){
//                projectId=project.getProjectId();
//            }
//        }
//
//        int userId=0;
//
//        for (User user : usersOnCreateProject) {
//            if(user.getFullname().equals(userItem)){
//                userId=user.getUserId();
//            }
//        }
//
//        ProjectUsers projectUsers=new ProjectUsers(userId, projectId);
//        Sender sender = new Sender(socket);
//        Request req = new Request(ClientsAction.ADDPROJECTUSERS, projectUsers);
//        sender.sendRequest(req);
//    }
//
//    @FXML
//    void create_task(ActionEvent event) {
//        String taskName = nameField.getText();
//        LocalDate date = dateField.getValue();
//        String projectNameItem = projectsField.getSelectionModel().getSelectedItem().toString();
//        String userNameItem = usersField.getSelectionModel().getSelectedItem().toString();
//
//        int projectId=0;
//
//        for (Project project : projectsByAuthor) {
//            if(project.getProjectName().equals(projectNameItem)){
//                projectId=project.getProjectId();
//            }
//        }
//
//        int userId=0;
//
//        for (User user : users) {
//            if(user.getFullname().equals(userNameItem)){
//                userId=user.getUserId();
//            }
//        }
//
//        Task task=new Task(taskName,date, projectId,userId,"назначена" );
//
//
//        Sender sender = new Sender(socket);
//        Request req = new Request(ClientsAction.CREATETASK, task);
//        sender.sendRequest(req);
//    }
//
//    private void createNewProject() throws IOException {
//
//        String projectName = textFieldProjectName.getText();
//
//        Project project=new Project(projectName);
//
//        Sender sender = new Sender(socket);
//        Request req = new Request(ClientsAction.CREATEPROJECT, project);
//        sender.sendRequest(req);
//
////        Response msg = sender.getResp();
////
////        if (msg.getServReaction() == ServReaction.SUCCESS) {
////            openNewScene("/com/example/diploma/Home.fxml");
////        }
//    }
//
//    @FXML
//    void change_status(ActionEvent event) {
//        String taskName = taskForStatusField.getSelectionModel().getSelectedItem().toString();
//
//        int taskId=0;
//
//        for (Task task : tasksByUser) {
//            if(task.getTaskName().equals(taskName)){
//                taskId=task.getTaskId();
//            }
//        }
//        String status = "выполнена";
//
//        Task task=new Task();
//        task.setTaskId(taskId);
//        task.setTaskName(taskName);
//        task.setStatus(status);
//
//        Sender sender = new Sender(socket);
//        Request req = new Request(ClientsAction.UPDATESTATUS, task);
//        sender.sendRequest(req);
//    }
//}