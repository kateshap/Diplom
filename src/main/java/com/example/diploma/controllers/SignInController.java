package com.example.diploma.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.diploma.Data;
import com.example.diploma.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import request.*;

public class SignInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button AuthSignInButton;

    @FXML
    private TextField login_field;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private AnchorPane SignInScene;

    int port = 3124;
    InetAddress ip = null;
    private Socket socket;
    Sender sender;
    String userRole;


    @FXML
    void initialize() {
        AuthSignInButton.setOnAction(event ->{
            String loginText = login_field.getText().trim();//trim delete spaces
            String loginPassword = password_field.getText().trim();

            if(!loginText.equals("") && !loginPassword.equals("")){
                try {
                    loginUser(loginText,loginPassword);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
                System.out.println("Login or password is empty");
        });
        loginSignUpButton.setOnAction(event-> {
            try {
                openNewScene("/com/example/diploma/controllers/SignUp.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void loginUser(String loginText, String loginPassword) throws SQLException, ClassNotFoundException, IOException {
        ip = InetAddress.getLocalHost();
        socket = new Socket(ip, port);

        Data.socket=socket;

        User user = new User();
        user.setLogin(loginText);
        user.setPassword(loginPassword);

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.SIGNIN, user);
        sender.sendRequest(req);


        Response msg = sender.getResp();
        userRole=msg.getUserRole();

        if (msg.getServReaction() == ServReaction.SUCCESS) {
            openNewScene("Menu.fxml");
        }

    }

    public void openNewScene(String window) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(window));
        Parent root=loader.load();

        MenuController menuController=loader.getController();
        menuController.socket=socket;
        menuController.userRole=userRole;


        Stage stage=(Stage) SignInScene.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
