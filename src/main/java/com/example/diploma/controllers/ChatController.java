package com.example.diploma.controllers;


import animatefx.animation.FadeIn;
import com.example.diploma.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;
//import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;



public class ChatController extends Thread implements Initializable {
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn;
    @FXML
    public TextField fileChoosePath;
    @FXML
    public ImageView proImage;
    @FXML
    public Circle showProPic;
    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;

    public Socket socket;

    Sender sender;
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
    Request req;
    Response msg;
    User user;




//    public void connectSocket() {
//        try {
//            socket = new Socket("localhost", 8889);
//            System.out.println("Socket is connected with server!");
//            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            writer = new PrintWriter(socket.getOutputStream(), true);
//            this.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void run() {
        try {
            while (true) {
                msg = sender.getResp();

                String[] tokens =  msg.getMessage().split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for(int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);

                if (cmd.equalsIgnoreCase(user.getFirstName() + ":")) {
                    continue;
                } else if(fulmsg.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                msgRoom.appendText(msg.getMessage() + "\n");
            }

            //socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    public void setProfile() {
//        for (User user : users) {
//            if (Controller.username.equalsIgnoreCase(user.name)) {
                fullName.setText("user.fullName");
                fullName.setOpacity(1);
                email.setText("user.email");
                email.setOpacity(1);
                phoneNo.setText("user.phoneNo");
                gender.setText("user.gender");
//            }
//        }
    }

    public void handleSendEvent(MouseEvent event) {
        send();
//        for(User user : users) {
//            System.out.println(user.name);
//        }
    }


    public void send() {
        String msg = msgField.getText();

        sender = new Sender(socket);
        req = new Request(ClientsAction.SENDMESSAGE,user.getFirstName() + ": " + msg);
        sender.sendRequest(req);

        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    // Changing profile pic

    public boolean saveControl = false;

    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void getInfClient(Socket socket){
        this.socket=socket;

        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETINFCLIENT);
        sender.sendRequest(req);
        try {
            msg = sender.getResp();
        } catch (IOException e) {
            e.printStackTrace();
        }
        user=msg.getUser();

//        if(Controller.gender.equalsIgnoreCase("Male")) {
//            image = new Image("icons/user.png", false);
//        } else {
//            image = new Image("icons/female.png", false);
//            proImage.setImage(image);
//        }
        image = new Image("female.png", false);
        proImage.setImage(image);
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(user.getFirstName());
        this.start();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
