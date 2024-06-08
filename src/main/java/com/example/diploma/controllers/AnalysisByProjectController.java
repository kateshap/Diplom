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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private AnchorPane projectСardPane;

    @FXML
    private Label projectName;

    @FXML
    private Label projectDir;

    @FXML
    private Label projectMan;

    @FXML
    private Label dateDayBegin;

    @FXML
    private Label dateMonthBegin;

    @FXML
    private Label dateYearBegin;

    @FXML
    private Label dateDayExecute;

    @FXML
    private Label dateMonthExecute;

    @FXML
    private Label dateYearExecute;

    @FXML
    private Label tasksCount;

    @FXML
    private Label projectProgram;
    @FXML
    private ListView<String> teamMembersList;


    Socket socket;
    String userRole;
    Sender sender;
    ArrayList<String> projectNameByUser=new ArrayList<String>();
    ArrayList<Integer> projectIdByUser=new ArrayList<Integer>();
    ArrayList<Project> projectsByUser;
    Request req;
    Response msg;
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
        loadDataForCard();
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
        loadDataForCard();
    }

    void loadDataForCard() throws IOException {
        sender = new Sender(socket);
        req = new Request(ClientsAction.GETCARDINFO);
        req.setProject(curProject);
        sender.sendRequest(req);
        msg = sender.getResp();

        projectName.setText(msg.getProject().getProjectName());
        projectDir.setText(msg.getProject().getDirectorName());
        projectMan.setText(msg.getProject().getManagerName());
        dateDayBegin.setText(String.valueOf(msg.getProject().getBeginDate().getDayOfMonth()));
        dateMonthBegin.setText(String.valueOf(msg.getProject().getBeginDate().getMonth()));
        dateYearBegin.setText(String.valueOf(msg.getProject().getBeginDate().getYear()));
        dateDayExecute.setText(String.valueOf(msg.getProject().getExecuteDate().getDayOfMonth()));
        dateMonthExecute.setText(String.valueOf(msg.getProject().getExecuteDate().getMonth()));
        dateYearExecute.setText(String.valueOf(msg.getProject().getExecuteDate().getYear()));
        projectProgram.setText(msg.getProject().getProgram());

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETCARDTASKSCOUNT);
        req.setProject(curProject);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksCount.setText(String.valueOf(msg.getProject().getTasksCount()));

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETCARDTEAMMEMBERS);
        req.setProject(curProject);
        sender.sendRequest(req);
        msg = sender.getResp();



//        for(String teamMember: msg.getProject().getTeamMembers()){
//            teamMembersList
//        }

        ObservableList<String> allProjects = FXCollections.observableArrayList(msg.getProject().getTeamMembers());
        teamMembersList.setItems(allProjects);//список всех проектов (как руководителя и участника)



        //tasksCount.setText(String.valueOf(msg.getProject().getTasksCount()));



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
