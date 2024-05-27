package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Queries;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import request.ClientsAction;
import request.Request;
import request.Response;
import request.Sender;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class AnalysisByProjectsController {
    @FXML
    private BarChart<String,Integer > barChartField;
    @FXML
    private StackedBarChart StackedBarField;

    @FXML
    private PieChart pieChartField;
    @FXML
    private TableView<Task> tableView;

    @FXML
    private TableColumn<Task, String> TaskName;

    @FXML
    private TableColumn<Task, LocalDate> TaskBeginDate;

    @FXML
    private TableColumn<Task, LocalDate> TaskExecuteDate;

    @FXML
    private TableColumn<Task, Integer> TaskDuration;

    @FXML
    private TableColumn<Task, String> TaskStatus;

    @FXML
    private TableColumn<Task, Integer> TaskPercent;

    @FXML
    private TableColumn<Task, Integer> TaskDelay;

    @FXML
    private TableColumn<Task, String> TaskUser;


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
    ArrayList<Queries> projectsCompletedTasks;
    ArrayList<Queries> usersCountProjects;
    ObservableList<Task> keyTasksByDirector;

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
        barChartField.setLegendVisible(false);

        barChartField.getData().add(series);


        sender = new Sender(socket);
        req = new Request(ClientsAction.GETPROJECTSCOMPLETEDTASKS);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectsCompletedTasks= msg.getQueries();

        XYChart.Series<String,Integer > series1=new XYChart.Series<>();

        for (Queries query : projectsCompletedTasks) {
            series1.getData().add(new XYChart.Data(query.getProject(),query.getCompletedTasks()));
        }

        XYChart.Series<String,Integer > series2=new XYChart.Series<>();

        for (Queries query : projectsCompletedTasks) {
            series2.getData().add(new XYChart.Data(query.getProject(),query.getOutstandingTasks()));
        }

        StackedBarField.getData().addAll(series1,series2);

        TaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        TaskBeginDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("beginDate"));
        TaskExecuteDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("executeDate"));
        TaskDuration.setCellValueFactory(new PropertyValueFactory<Task, Integer>("duration"));
        TaskDelay.setCellValueFactory(new PropertyValueFactory<Task, Integer>("delay"));
        TaskStatus.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
        TaskPercent.setCellValueFactory(new PropertyValueFactory<Task, Integer>("percent"));
        TaskUser.setCellValueFactory(new PropertyValueFactory<Task, String>("userName"));

        //TaskBeginDate.setVisible(false);
        //TaskExecuteDate.setVisible(false);
        TaskDuration.setVisible(false);
        TaskDelay.setVisible(false);
        TaskPercent.setVisible(false);
        //TaskUser.setVisible(false);

        loadDataForTable();

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETUSERSCOUNTPROJECTS);
        sender.sendRequest(req);
        msg = sender.getResp();

        usersCountProjects= msg.getQueries();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Queries query : usersCountProjects) {
            pieChartData.add(new PieChart.Data(query.getUser(),query.getProjectsCount()));


        }

        pieChartData.forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), " (", data.pieValueProperty(), ")"
                )
            )
        );


        pieChartField.getData().addAll(pieChartData);
    }

    void loadDataForTable() throws IOException {
        tableView.getItems().clear();

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETKEYTASKSBYDIRECTOR);
        sender.sendRequest(req);
        msg = sender.getResp();

        keyTasksByDirector = FXCollections.observableArrayList(msg.getTasks());

        tableView.setItems(keyTasksByDirector);
    }
}
