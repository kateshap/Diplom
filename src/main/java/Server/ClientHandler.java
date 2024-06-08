package Server;

import com.example.diploma.*;
import com.google.gson.Gson;
import request.Request;
import request.Response;
import request.Sender;
import request.ServReaction;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ArrayList<ClientHandler> clients;
    private Gson gson = new Gson();
    private String UserName;
    private int UserId;
    DatabaseHandler dbHandler;


    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    Sender sender;

    public ClientHandler(Socket clientSocket, String userName, ArrayList<ClientHandler> clients, DatabaseHandler dbHandler) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.sender = new Sender(clientSocket);
        this.UserName = userName;
        this.dbHandler=dbHandler;
    }

    @Override
    public void run() {
        try {
            while(true)
            {
                Request msg = sender.getRequest();

                switch (msg.getClientsAction()) {
                    case CREATEPROJECT: {
                        dbHandler.createProject(msg.getProject(), UserId);
                        break;
                    }
                    case GETPROJECTSBYAUTHOR:{
                        ArrayList<Project> projects;
                        projects=dbHandler.getProjectsByAuthor(UserId);
                        Response resp = new Response(projects);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETPROJECTSBYUSER:{
                        ArrayList<Project> projects;
                        projects=dbHandler.getProjectsByUser(UserId);
                        Response resp = new Response(projects);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETUSERSBYPROJECT:{
                        ArrayList<User> users;
                        users=dbHandler.getUsersByProject(msg.getProject());
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setUsers(users);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETUSERS:{
                        ArrayList<User> users;
                        users=dbHandler.getAllUsers();
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setUsers(users);
                        sender.sendResp(resp);
                        break;
                    }
                    case CREATETASK: {
                        dbHandler.createTask(msg.getTask());
                        break;
                    }
                    case ADDPROJECTUSERS: {
                        dbHandler.addProjectUsers(msg.getProjectUsers());
                        break;
                    }
                    case GETTASKSBYUSER:{
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getTasksByUser(UserId);
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }

                    case DELETETASK:{
                        ArrayList<Task> tasks;
                        Response resp = new Response(ServReaction.SUCCESS);
                        tasks=dbHandler.getTasksByParentId(msg.getTask().getTaskId());
                        if(tasks.isEmpty()){
                            dbHandler.deleteTask(msg.getTask());
                            resp.setServReaction(ServReaction.SUCCESS);
                        }
                        else{
                            resp.setServReaction(ServReaction.FAIL);
                        }
                        sender.sendResp(resp);
                        break;
                    }

                    case UPDATETASKNAME: {
                        dbHandler.updateTaskName(msg.getTask());
                        break;
                    }
                    case UPDATETASKDURATION: {
                        dbHandler.updateTaskDuration(msg.getTask());
                        dbHandler.updateTaskExecuteDate(msg.getTask());
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getTasksByParentId(msg.getTask().getTaskId());
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }
                    case UPDATETASKDELAY: {
                        dbHandler.updateTaskDelay(msg.getTask());
                        dbHandler.updateTaskBeginDate(msg.getTask());
                        dbHandler.updateTaskExecuteDate(msg.getTask());
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getTasksByParentId(msg.getTask().getTaskId());
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                    }

                    case UPDATETASKSTATUS: {
                        dbHandler.updateTaskStatus(msg.getTask());
                        break;
                    }
                    case UPDATETASKPERCENT: {
                        dbHandler.updateTaskPercent(msg.getTask());
                        break;
                    }
                    case UPDATETASKPARENTID: {
                        dbHandler.updateTaskParentId(msg.getTask());
                        dbHandler.updateTaskBeginDate(msg.getTask());
                        dbHandler.updateTaskExecuteDate(msg.getTask());
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getTasksByParentId(msg.getTask().getTaskId());
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }

                    case UPDATETASKUSER: {
                        dbHandler.updateTaskUser(msg.getTask());
                        break;
                    }

                    case SENDMESSAGE: {
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setMessage(msg.getMessage());
                        for (ClientHandler cl : clients) {
                            cl.sender.sendResp(resp);
                        }
                        break;
                    }
                    case GETINFCLIENT:{
                        Response resp = new Response(ServReaction.SUCCESS);
                        User user=new User();
                        user.setUserId(UserId);
                        user.setFirstName(UserName);
                        resp.setUser(user);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETTASKSBYPROJECT:{
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getTasksByProject(msg.getProject().getProjectId());
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }

                    case GETKEYTASKSBYDIRECTOR:{
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getKeyTasksByDirector(UserId);
                        Response resp = new Response(ServReaction.SUCCESS);
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETPROGRAMSCOUNTPROJECTS :{
                        ArrayList<Queries> queries;
                        queries=dbHandler.getProgramsCountProjects(UserId);
                        Response resp = new Response();
                        resp.setQueries(queries);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETPROJECTSCOMPLETEDTASKS :{
                        ArrayList<Queries> queries;
                        queries=dbHandler.getProjectsCompletedTasks(UserId);
                        Response resp = new Response();
                        resp.setQueries(queries);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETUSERSCOUNTPROJECTS :{
                        ArrayList<Queries> queries;
                        queries=dbHandler.getUsersCountProjects(UserId);
                        Response resp = new Response();
                        resp.setQueries(queries);
                        sender.sendResp(resp);
                        break;
                    }

                    case GETUSERSCOUNTTASKS :{
                        ArrayList<Queries> queries;
                        queries=dbHandler.getUsersCountTasks(msg.getProject().getProjectId());
                        Response resp = new Response();
                        resp.setQueries(queries);
                        sender.sendResp(resp);
                        break;
                    }

                    case GETOUSTANDINGTASKS :{
                        ArrayList<Task> tasks;
                        tasks=dbHandler.getOutstandingTasks(msg.getProject().getProjectId());
                        Response resp = new Response();
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETCARDINFO:{
                        Project project;
                        project=dbHandler.getCardInfo(msg.getProject().getProjectId());
                        Response resp = new Response(project);
                        sender.sendResp(resp);
                        break;
                    }

                    case GETCARDTASKSCOUNT:{
                        Project project;
                        project=dbHandler.getCardTasksCount(msg.getProject().getProjectId());
                        Response resp = new Response(project);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETCARDTEAMMEMBERS:{
                        Project project;
                        project=dbHandler.getCardTeamMembers(msg.getProject().getProjectId());
                        Response resp = new Response(project);
                        sender.sendResp(resp);
                        break;
                    }

                }
            }
        } catch (IOException | SQLException | ClassNotFoundException ignored) {}

    }
}
