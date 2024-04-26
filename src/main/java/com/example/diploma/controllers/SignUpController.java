package com.example.diploma.controllers;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.diploma.Data;
import com.example.diploma.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import request.*;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField password_field;

    @FXML
    private TextField signUpLastName;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField login_field;

    @FXML
    private TextField email_field;

    @FXML
    private Button SignUpButton;

    @FXML
    private CheckBox signUpCheckBoxMale;

    @FXML
    private CheckBox signUpCheckBoxFemale;

    @FXML
    private AnchorPane SignUpScene;


    int port = 3124;
    InetAddress ip = null;
    private Socket socket;
    Sender sender;

    @FXML
    void initialize() {
        SignInController signInController =new SignInController();
        SignUpButton.setOnAction(event ->{
            try {
                signUpNewUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //openNewScene("/com/example/diploma/Home.fxml");
            //SignIn.fxml
        });

    }

    private void signUpNewUser() throws IOException {
        //DatabaseHandler dbHandler = new DatabaseHandler();


        // Создайте сокет и подключитесь к серверу по указанному хосту и порту
        ip = InetAddress.getLocalHost();
        socket = new Socket(ip, port);
        //Data.socket=socket;
//        FXMLLoader loader=new FXMLLoader(getClass().getResource("Menu.fxml"));
//        MenuController menuController=loader.getController();
//        menuController.socket=socket;


        String firstName = signUpName.getText();
        String lastName = signUpLastName.getText();
        String login = login_field.getText();
        String password = password_field.getText();
        String email = email_field.getText();
        String gender = "";

        if (signUpCheckBoxMale.isSelected())
            gender = "Мужской";
        else
            gender = "Женский";
        User user = new User(firstName, lastName, login, password, gender, email,lastName+" "+firstName);

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.SIGNUP, user);
        sender.sendRequest(req);

        Response msg = sender.getResp();

        if (msg.getServReaction() == ServReaction.SUCCESS) {

            //openNewScene("/com/example/diploma/Home.fxml");
            openNewScene("Menu.fxml");
        }
    }





        public void openNewScene(String window) throws IOException {
//            AnchorPane scene1= FXMLLoader.load(getClass().getResource(window));
//            SignUpScene.getChildren().removeAll();
//            SignUpScene.getChildren().setAll(scene1);

            FXMLLoader loader=new FXMLLoader(getClass().getResource(window));
            Parent root=loader.load();

            MenuController menuController=loader.getController();
            menuController.socket=socket;

            SignUpScene.getChildren().removeAll();
            SignUpScene.getChildren().setAll(root);

//        SignUpButton.getScene().getWindow().hide();
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource(window));
//
//        try {
//            loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Parent root = loader.getRoot();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.showAndWait();

    }
}
