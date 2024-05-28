package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Queries;
import com.example.diploma.Task;
import com.example.diploma.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
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

public class AnalysisByProjectController {
    @FXML
    private BarChart<String,Integer > barChartField;
    @FXML
    private TableView<Task> tableView;

    @FXML
    private TableColumn<Task, String> TaskName;

    @FXML
    private TableColumn<Task, LocalDate> TaskBeginDate;

    @FXML
    private TableColumn<Task, LocalDate> TaskExecuteDate;

    @FXML
    private TableColumn<Task, String> TaskUser;

    @FXML
    private ComboBox<String> projectComboBox;


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
    ArrayList<Queries> usersCountTasks;
    ObservableList<Task> outstandingTasks;
    Project curProject;

    @FXML
    void initialize() {}

    public void getQueries(Socket socket,String userRole) throws IOException {
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

        projectComboBox.setItems(allProjects);
        projectComboBox.getSelectionModel().selectFirst();

        curProject=projectsByUser.get(0);

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETUSERSCOUNTTASKS);
        req.setProject(curProject);
        sender.sendRequest(req);
        msg = sender.getResp();

        usersCountTasks= msg.getQueries();

        XYChart.Series<String,Integer > series=new XYChart.Series<>();

        for (Queries query : usersCountTasks) {
            series.getData().add(new XYChart.Data(query.getUser(),query.getTasksCount()));
        }
        barChartField.setLegendVisible(false);

        barChartField.getData().add(series);

        TaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        TaskBeginDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("beginDate"));
        TaskExecuteDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("executeDate"));
        TaskUser.setCellValueFactory(new PropertyValueFactory<Task, String>("userName"));

        loadDataForTable();
    }

    @FXML
    private void comboAction(ActionEvent event) throws IOException {
        barChartField.getData().clear();

        String projectName=projectComboBox.getSelectionModel().getSelectedItem();

        for (Project project : projectsByUser) {
            if(projectName.equals(project.getProjectName())){
                curProject=project;
                sender = new Sender(socket);
                req = new Request(ClientsAction.GETUSERSCOUNTTASKS);
                req.setProject(curProject);
                sender.sendRequest(req);
                msg = sender.getResp();

                usersCountTasks= msg.getQueries();

                XYChart.Series<String,Integer > series=new XYChart.Series<>();

                for (Queries query : usersCountTasks) {
                    series.getData().add(new XYChart.Data(query.getUser(),query.getTasksCount()));
                }
                barChartField.setLegendVisible(false);

                barChartField.getData().add(series);
            }
        }

        loadDataForTable();
    }

    void loadDataForTable() throws IOException {
        tableView.getItems().clear();

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETOUSTANDINGTASKS);
        req.setProject(curProject);
        sender.sendRequest(req);
        msg = sender.getResp();

        outstandingTasks = FXCollections.observableArrayList(msg.getTasks());

        tableView.setItems(outstandingTasks);
    }
}
