package com.example.diploma.controllers;

import com.example.diploma.*;
import com.example.diploma.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import request.*;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ProjectInfoController {

    @FXML
    private AnchorPane paneField;

    @FXML
    private TableView<Task> tableview;

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
    private TableColumn<Task, String> TaskDependency;

    @FXML
    private TableColumn<Task, String> TaskUser;

    @FXML
    private Button AddTaskBtn;

    @FXML
    private Button DeleteTaskBtn;



    @FXML
    private Button GanttChartButton;

    Socket socket;
    Sender sender;
    Request req;
    Response msg;
    Project project;
    ObservableList<Task> tasksByProject;
    private String userRole;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Request getReq() {
        return req;
    }

    public void setReq(Request req) {
        this.req = req;
    }

    public Response getMsg() {
        return msg;
    }

    public void setMsg(Response msg) {
        this.msg = msg;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    void initTable(Socket socket, String userRole) throws IOException {
        this.socket = socket;
        this.userRole=userRole;

        TaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        TaskBeginDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("beginDate"));
        TaskExecuteDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("executeDate"));
        TaskDuration.setCellValueFactory(new PropertyValueFactory<Task, Integer>("duration"));
        TaskDelay.setCellValueFactory(new PropertyValueFactory<Task, Integer>("delay"));
        TaskStatus.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
        TaskPercent.setCellValueFactory(new PropertyValueFactory<Task, Integer>("percent"));
        TaskDependency.setCellValueFactory(new PropertyValueFactory<Task, String>("parentName"));
        TaskUser.setCellValueFactory(new PropertyValueFactory<Task, String>("userName"));


        if(userRole.equals("manager")){
            tableview.setEditable(true);
            TaskName.setCellFactory(TextFieldTableCell.forTableColumn());
            TaskDuration.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            TaskDelay.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            TaskStatus.setCellFactory(TextFieldTableCell.forTableColumn());
            TaskPercent.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            TaskDependency.setCellFactory(TextFieldTableCell.forTableColumn());
            TaskUser.setCellFactory(TextFieldTableCell.forTableColumn());
        }
        else if(userRole.equals("director")){
            AddTaskBtn.setVisible(false);
            DeleteTaskBtn.setVisible(false);
        }
        else if(userRole.equals("teamMember")){
            AddTaskBtn.setVisible(false);
            DeleteTaskBtn.setVisible(false);
            GanttChartButton.setVisible(false);
            TaskUser.setVisible(false);

            tableview.setEditable(true);
            TaskStatus.setCellFactory(TextFieldTableCell.forTableColumn());
            TaskPercent.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        }

        loadDataForTable();
    }

    void loadDataForTable() throws IOException {
        tableview.getItems().clear();

        if(userRole.equals("manager")){
            sender = new Sender(socket);
            req = new Request(ClientsAction.GETTASKSBYPROJECT);
            req.setProject(project);
            sender.sendRequest(req);
            msg = sender.getResp();
        }
        else if(userRole.equals("director")){
            sender = new Sender(socket);
            req = new Request(ClientsAction.GETTASKSBYPROJECT);
            req.setProject(project);
            sender.sendRequest(req);
            msg = sender.getResp();
        }
        else if(userRole.equals("teamMember")){
            sender = new Sender(socket);
            req = new Request(ClientsAction.GETTASKSBYUSER);
//        req.setProject(project);
            sender.sendRequest(req);
            msg = sender.getResp();

        }

        tasksByProject = FXCollections.observableArrayList(msg.getTasks());
        tableview.setItems(tasksByProject);
    }

    @FXML
    private void AddTask(ActionEvent event) throws IOException {
        if(userRole.equals("manager")){
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreateTask.fxml"));
            Parent root1=(Parent) fxmlLoader.load();

            CreateTaskController сreateTaskController=fxmlLoader.getController();
            сreateTaskController.getInfoForTasks(socket,project);

            Stage stage=new Stage();
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Stage is closing");
                    try {
                        loadDataForTable();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            stage.show();

        }
        else if(userRole.equals("director")){

        }
        else if(userRole.equals("teamMember")){

        }


    }

    @FXML
    void DeleteTask(ActionEvent event) throws IOException {
        if(userRole.equals("manager")){
            Task task = tableview.getSelectionModel().getSelectedItem();

            Sender sender = new Sender(socket);
            Request req = new Request(ClientsAction.DELETETASK, task);
            sender.sendRequest(req);

            msg = sender.getResp();

            if(msg.getServReaction()== ServReaction.SUCCESS){
                tableview.getItems().remove(task);
            }
            else{
                System.out.println("Task has dependency");
            }
        }
        else if(userRole.equals("director")){

        }
        else if(userRole.equals("teamMember")){

        }

    }


    public void EditName(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setTaskName(taskStringCellEditEvent.getNewValue());

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKNAME, task);
        sender.sendRequest(req);
    }

    public void EditDuration(TableColumn.CellEditEvent<Task, Integer> taskIntegerCellEditEvent) throws IOException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setDuration(taskIntegerCellEditEvent.getNewValue());
        task.setExecuteDate(task.getBeginDate().plusDays(task.getDuration()));

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKDURATION, task);
        sender.sendRequest(req);

        msg = sender.getResp();
        Rec(msg.getTasks());

        loadDataForTable();
    }

    public void EditDelay(TableColumn.CellEditEvent<Task, Integer> taskIntegerCellEditEvent) throws IOException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setDelay(taskIntegerCellEditEvent.getNewValue());

        for (Task temp : tasksByProject) {
            if (temp.getTaskId() == task.getParentId()) {
                task.setBeginDate(temp.getExecuteDate().plusDays(task.getDelay()));
                task.setExecuteDate(task.getBeginDate().plusDays(task.getDuration()));
            }
        }

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKDELAY, task);
        sender.sendRequest(req);

        msg = sender.getResp();
        Rec(msg.getTasks());

        loadDataForTable();
    }

    public void EditStatus(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setStatus(taskStringCellEditEvent.getNewValue());

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKSTATUS, task);
        sender.sendRequest(req);
    }

    public void EditPercent(TableColumn.CellEditEvent<Task, Integer> taskIntegerCellEditEvent) {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setPercent(taskIntegerCellEditEvent.getNewValue());

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKPERCENT, task);
        sender.sendRequest(req);
    }

    public void Rec(ArrayList<Task> tasks) throws IOException {
        if(tasks.isEmpty()){
            return;
        }
        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYPROJECT);
        req.setProject(project);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByProject = FXCollections.observableArrayList(msg.getTasks());

        for(Task task : tasks){
            for (Task temp : tasksByProject) {
                if (temp.getTaskId() == task.getParentId()) {
                    task.setBeginDate(temp.getExecuteDate().plusDays(task.getDelay()));
                    task.setExecuteDate(task.getBeginDate().plusDays(task.getDuration()));
                }
            }

                Sender sender = new Sender(socket);
                Request req = new Request(ClientsAction.UPDATETASKPARENTID, task);
                sender.sendRequest(req);
                msg = sender.getResp();
                Rec(msg.getTasks());
        }
    }

    public void EditDependency(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) throws IOException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        boolean f=false;

        for (Task temp : tasksByProject) {
            if (taskStringCellEditEvent.getNewValue().equals(temp.getTaskName())) {
                task.setParentName(taskStringCellEditEvent.getNewValue());
                task.setParentId(temp.getTaskId());
                task.setBeginDate(temp.getExecuteDate().plusDays(task.getDelay()));
                task.setExecuteDate(task.getBeginDate().plusDays(task.getDuration()));
                f=true;
            }
        }
        if(!f){
            System.out.println("this project name isn't exsist");
            return;
        }

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKPARENTID, task);
        sender.sendRequest(req);
        msg = sender.getResp();
        Rec(msg.getTasks());

        loadDataForTable();
    }

    public void EditUser(TableColumn.CellEditEvent<Task, String> taskStringCellEditEvent) throws IOException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        boolean f=false;
        ArrayList<User> users;
        ArrayList<String> userFullName=new ArrayList();

        req = new Request(ClientsAction.GETUSERS);
        sender.sendRequest(req);
        msg = sender.getResp();
        users=msg.getUsers();

        for (User user : users) {
            userFullName.add(user.getFullname());
        }

        for (User user : users) {
            if (taskStringCellEditEvent.getNewValue().equals(user.getFullname())) {
                task.setUserName(taskStringCellEditEvent.getNewValue());
                f=true;
            }
        }
        if(!f){
            System.out.println("this project name isn't exsist");
            return;
        }

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKUSER, task);
        sender.sendRequest(req);
    }

    @FXML
    void showGanttChart(ActionEvent event) {
        ArrayList<java.util.Date> ProjectDates = new ArrayList<java.util.Date>();
        List<Date> beginDates = new ArrayList<>();
        List<Date> executeDates = new ArrayList<>();
        ArrayList<String> taskNames = new ArrayList<>();
        ArrayList<ArrayList<Integer> > childrenArrayId = new ArrayList<>();

        for(int i=0;i<tasksByProject.size();i++){
            ArrayList<Integer> chId=new ArrayList<>();
            chId.add(-1);
            childrenArrayId.add(i,chId);
        }

        for(int i=0;i<tasksByProject.size();i++){
            taskNames.add(tasksByProject.get(i).getTaskName());
            beginDates.add(Date.from(tasksByProject.get(i).getBeginDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            executeDates.add(Date.from(tasksByProject.get(i).getExecuteDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            for (int j=0;j<tasksByProject.size();j++) {
                if (tasksByProject.get(i).getParentId() == tasksByProject.get(j).getTaskId()) {
                    if(childrenArrayId.get(j).get(0)==-1){
                        childrenArrayId.get(j).set(0,tasksByProject.size()-1-i);
                    }
                    else{
                        childrenArrayId.get(j).add(tasksByProject.size()-1-i);
                    }

                }
            }
        }

        Collections.reverse(taskNames);
        Collections.reverse(beginDates);
        Collections.reverse(executeDates);
        Collections.reverse(childrenArrayId);

        final DateAxis xAxis = new DateAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChartController<Date,String> chart = new GanttChartController<>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.DARKRED);
        xAxis.setTickLabelGap(10);
        xAxis.setLowerBound(new GregorianCalendar(2024, Calendar.MAY, 1).getTime());
        xAxis.setUpperBound(new GregorianCalendar(2024, Calendar.JULY, 1).getTime());
        xAxis.averageTickGap();
        xAxis.setTickLength(15);
        xAxis.setMaxWidth(1000);
        xAxis.setMinWidth(1000);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelRotation(90);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.GREEN);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.observableList(taskNames));

        chart.setTitle("test");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        for (int i =0; i<tasksByProject.size(); i++) {
            double length = xAxis.getDisplayPositionDate(beginDates.get(i),executeDates.get(i));
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data(beginDates.get(i), taskNames.get(i), new GanttChartController.ExtraData( length, "BLACK", childrenArrayId.get(i))));
            chart.getData().add(series);
        }

        Scene scene  = new Scene(chart,1200,600);
        Stage stage = new Stage();
        stage.setX(scene.getX()+50);
        stage.setScene(scene);
        stage.show();
    }



}
