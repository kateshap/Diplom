package com.example.diploma;


import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private AnchorPane blackPane;
    @FXML
    private VBox buttonsPane;

    @FXML
    private ImageView menu;

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
        Parent fxml = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(fxml);
    }

    @FXML
    void onProject(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Project.fxml"));
        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(fxml);
    }

    @FXML
    void onSettings(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Settings.fxml"));
        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(fxml);
    }

    @FXML
    void onTask(ActionEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Task.fxml"));
        contentPane.getChildren().removeAll();
        contentPane.getChildren().setAll(fxml);
    }
}

