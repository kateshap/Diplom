package com.example.diploma.controllers;


import com.example.diploma.Project;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import request.Request;
import request.Response;
import request.Sender;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MenuController {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private AnchorPane blackPane;
    @FXML
    private VBox buttonsPane;

    @FXML
    private ImageView menu;

    public Socket socket;
    public String userRole;
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
        blackPane.setVisible(false);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),blackPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),buttonsPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        menu.setOnMouseClicked(event->{
            blackPane.setVisible(true);

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),blackPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.8);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),buttonsPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        blackPane.setOnMouseClicked(event->{
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),blackPane);
            fadeTransition1.setFromValue(0.8);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1->{
                blackPane.setVisible(false);
            });

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),buttonsPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
    }

    @FXML
    void onProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
        Parent root = loader.load();

        ProfileController profileController = loader.getController();
        profileController.socket = socket;
        profileController.userRole = userRole;

        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(root);
    }



    @FXML
    void onProject(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Project.fxml"));
        Parent root=loader.load();

        ProjectController projectController=loader.getController();
        projectController.getProjects(socket,userRole);

        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(root);
    }
    @FXML
    void onTask(ActionEvent event) throws IOException {
        if(userRole.equals("teamMember")){
            FXMLLoader Loader = new FXMLLoader(getClass().getResource("Task.fxml"));
            Loader.load();

            TaskController taskController=Loader.getController();
            taskController.initTable(socket,userRole);

            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
        if(userRole.equals("director")){
            FXMLLoader Loader = new FXMLLoader(getClass().getResource("AnalysisByProjects.fxml"));
            Loader.load();

            AnalysisByProjectsController analysisByProjectsController =Loader.getController();
            analysisByProjectsController.getQueries(socket,userRole);

            Parent p = Loader.getRoot();
            Stage stage = new Stage();
            Scene scene = new Scene(p);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void onSettings(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("Settings.fxml"));
        Parent root=loader.load();

        SettingsController settingsController=loader.getController();
        settingsController.socket=socket;
        settingsController.userRole=userRole;

        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(root);
    }


}

