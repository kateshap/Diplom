package com.example.diploma.controllers;

import com.example.diploma.*;
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

public class TaskController {

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
    private TableColumn<Task, String> TaskProject;


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
        TaskProject.setCellValueFactory(new PropertyValueFactory<Task, String>("projectName"));

        tableview.setEditable(true);
        TaskStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        TaskPercent.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        loadDataForTable();
    }

    void loadDataForTable() throws IOException {
        tableview.getItems().clear();

        sender = new Sender(socket);
        req = new Request(ClientsAction.GETTASKSBYUSER);
//        req.setProject(project);
        sender.sendRequest(req);
        msg = sender.getResp();

        tasksByProject = FXCollections.observableArrayList(msg.getTasks());

        tableview.setItems(tasksByProject);
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

}
