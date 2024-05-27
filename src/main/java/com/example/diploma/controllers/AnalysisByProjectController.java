package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Queries;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class AnalysisByProjectController {
    @FXML
    private BarChart<String,Integer > barChartField;
    @FXML
    private StackedBarChart StackedBarField;

    @FXML
    private PieChart pieChartField;

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
    ArrayList<Queries> programsCountProjects;

    @FXML
    void initialize() {}

    public void getQueries(Socket socket,String userRole) throws IOException {
        this.socket = socket;
        this.userRole=userRole;

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROGRAMSCOUNTPROJECTS);
        sender.sendRequest(req);
        msg = sender.getResp();

        programsCountProjects= msg.getQueries();

        XYChart.Series<String,Integer > series=new XYChart.Series<>();

        for (Queries query : programsCountProjects) {
            series.getData().add(new XYChart.Data(query.getProgram(),query.getProjectsCount()));
        }

        barChartField.getData().add(series);
    }
}
