package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.ProjectUsers;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectController {

    @FXML
    private Button createProjectButton;
    @FXML
    private ListView<String> listField;

    @FXML
    private ComboBox usersFieldOnCreateProject;

    @FXML
    private ComboBox projectsFieldOnCreateProject;

    @FXML
    private Button chatButton;

    @FXML
    private VBox projectsVbox;



    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    Socket socket;
    String userRole;
    Sender sender;
    ArrayList<String> projectNameByAuthor=new ArrayList<String>();
    ArrayList<String> projectNameByUser=new ArrayList<String>();
    ArrayList<Integer> projectIdByUser=new ArrayList<Integer>();
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
    void createProject(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreateProject.fxml"));
//        Parent root1=(Parent) fxmlLoader.load();
//        Stage stage=new Stage();
//        stage.setScene(new Scene(root1));
//        stage.show();

        FXMLLoader loader=new FXMLLoader(getClass().getResource("CreateProject.fxml"));
        Parent root=loader.load();

        CreateProjectController createProjectController=loader.getController();
        createProjectController.socket=socket;

        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SignIn.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void initialize() throws IOException {

    }

    public void getProjects(Socket socket,String userRole) throws IOException {
        this.socket = socket;
        this.userRole=userRole;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSBYUSER);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsByUser = msg.getProjects();

        for (Project project : projectsByUser) {
            projectNameByUser.add(project.getProjectName());
            projectIdByUser.add(project.getProjectId());
        }

        ObservableList<String> allProjects = FXCollections.observableArrayList(projectNameByUser);
        //listField.setItems(allProjects);//список всех проектов (как руководителя и участника)

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSBYAUTHOR);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsByAuthor = msg.getProjects();

        for (Project project : projectsByAuthor) {
            projectNameByAuthor.add(project.getProjectName());
        }


        ObservableList<String> projectItems = FXCollections.observableArrayList(projectNameByAuthor);
        projectsFieldOnCreateProject.setItems(projectItems);// combobox со всеми проектами
        projectsFieldOnCreateProject.getSelectionModel().selectFirst();

        for (int i = 0; i < allProjects.size(); i++) {
            Button button = new Button();
                button.setOnMouseClicked((MouseEvent t) ->
                {
                    System.out.println(button.getId());
                    FXMLLoader Loader = new FXMLLoader();
                    Loader.setLocation(getClass().getResource("projectInfo.fxml"));

                    try {
                        Loader.load();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProjectInfoController projectInfoController = Loader.getController();
                    projectInfoController.project=projectsByUser.get(Integer.parseInt(button.getId()));
                    try {
                        projectInfoController.initTable(socket,userRole);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent p = Loader.getRoot();
                    //Stage stage = (Stage) projectsVbox.getScene().getWindow();
                    Stage stage = new Stage();
                    Scene scene = new Scene(p);
                    stage.setScene(scene);
                    stage.show();

                });
            button.setText(allProjects.get(i));
            button.setId(String.valueOf(i));

            projectsVbox.setSpacing(10);

            projectsVbox.getChildren().add(button);
        }
    }

    @FXML
    void updateUsers(MouseEvent event) throws IOException { // combobox со всеми участниками выбранного проекта
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
        if(usersFieldOnCreateProject.getSelectionModel().isEmpty()){}
        else{
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

    }

    @FXML
    void openChat(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Chat.fxml"));
        Parent root=loader.load();

        ChatController chatController=loader.getController();
        chatController.getInfClient(socket);

        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
