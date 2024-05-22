package com.example.diploma.controllers;

import com.example.diploma.DateAxis;
import com.example.diploma.GanttChartController;
import com.example.diploma.Project;
import com.example.diploma.Task;
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
    private TableColumn<Task, Integer> TaskDependency;

    @FXML
    private TableColumn<Task, Integer> TaskUser;


    @FXML
    private Button GanttChartButton;

    Socket socket;
    Sender sender;
    Request req;
    Response msg;
    Project project;
    ObservableList<Task> tasksByProject;

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

    @FXML
    void showGanttChart(ActionEvent event) {
        ArrayList<java.util.Date> ProjectDates = new ArrayList<java.util.Date>();
        List<Date> beginDates = new ArrayList<>();
        List<Date> executeDates = new ArrayList<>();
        ArrayList<String> TaskNames = new ArrayList<>();

        for(Task task: tasksByProject){
            TaskNames.add(task.getTaskName());
            beginDates.add(Date.from(task.getBeginDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            executeDates.add(Date.from(task.getExecuteDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        final DateAxis xAxis = new DateAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChartController<Date,String> chart = new GanttChartController<>(xAxis,yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.DARKRED);
        xAxis.setTickLabelGap(10);
        xAxis.setLowerBound(new GregorianCalendar(2024, Calendar.APRIL, 1).getTime());
        xAxis.setUpperBound(new GregorianCalendar(2024, Calendar.JUNE, 1).getTime());
        xAxis.averageTickGap();
        xAxis.setTickLength(15);
        xAxis.setMaxWidth(1000);
        xAxis.setMinWidth(1000);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelRotation(90);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.GREEN);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.observableList(TaskNames));

        chart.setTitle("test");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        for (int i =0; i<tasksByProject.size(); i++) {
            double length = xAxis.getDisplayPositionDate(beginDates.get(i),executeDates.get(i));
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data(beginDates.get(i), TaskNames.get(i), new GanttChartController.ExtraData( length, "BLACK")));
            chart.getData().add(series);
        }

        //chart.getStylesheets().add(getClass().getResource("GanttChart.css").toExternalForm());

        Scene scene  = new Scene(chart,1200,600);
        //stage = (Stage) GanttChartButton.getScene().getWindow();
        Stage stage = new Stage();
        stage.setX(scene.getX()+50);
        stage.setScene(scene);
        stage.show();


    }

    void initTable(Socket socket) throws IOException {
        this.socket = socket;

        TaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        TaskBeginDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("beginDate"));
        TaskExecuteDate.setCellValueFactory(new PropertyValueFactory<Task, LocalDate>("executeDate"));
        TaskDuration.setCellValueFactory(new PropertyValueFactory<Task, Integer>("duration"));
        TaskStatus.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
        TaskPercent.setCellValueFactory(new PropertyValueFactory<Task, Integer>("percent"));
        TaskDependency.setCellValueFactory(new PropertyValueFactory<Task, Integer>("parentId"));
        TaskUser.setCellValueFactory(new PropertyValueFactory<Task, Integer>("userId"));

        tableview.setEditable(true);

        TaskName.setCellFactory(TextFieldTableCell.forTableColumn());
        TaskDuration.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TaskStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        TaskPercent.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TaskDependency.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TaskUser.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        loadDataForTable();
    }

    void loadDataForTable() throws IOException {
        tableview.getItems().clear();

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYPROJECT);
        req.setProject(project);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByProject = FXCollections.observableArrayList(msg.getTasks());

        tableview.setItems(tasksByProject);
    }

    @FXML
    private void AddTask(ActionEvent event) throws IOException {

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

    @FXML
    void DeleteTask(ActionEvent event) throws IOException {
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
                    task.setBeginDate(temp.getExecuteDate());
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

    public void EditDependency(TableColumn.CellEditEvent<Task, Integer> taskIntegerCellEditEvent) throws IOException {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setParentId(taskIntegerCellEditEvent.getNewValue());

        for (Task temp : tasksByProject) {
            if (temp.getTaskId() == task.getParentId()) {
                task.setBeginDate(temp.getExecuteDate());
                task.setExecuteDate(task.getBeginDate().plusDays(task.getDuration()));
            }
        }

            Sender sender = new Sender(socket);
            Request req = new Request(ClientsAction.UPDATETASKPARENTID, task);
            sender.sendRequest(req);
            msg = sender.getResp();
            Rec(msg.getTasks());

        loadDataForTable();
    }

    public void EditUser(TableColumn.CellEditEvent<Task, Integer> taskIntegerCellEditEvent) {
        Task task = tableview.getSelectionModel().getSelectedItem();
        task.setUserId(taskIntegerCellEditEvent.getNewValue());

        Sender sender = new Sender(socket);
        Request req = new Request(ClientsAction.UPDATETASKUSER, task);
        sender.sendRequest(req);
    }
}
