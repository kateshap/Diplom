package Server;

import com.example.diploma.Project;
import com.example.diploma.Task;
import com.example.diploma.User;
import com.google.gson.Gson;
import model.Model;
import model.ModelBuilder;
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
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Gson gson = new Gson();
    private String UserName;
    private int UserId;


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

    Model model = ModelBuilder.build();
    Sender sender;

    public ClientHandler(Socket clientSocket, String userName,  ArrayList<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        sender = new Sender(clientSocket);
        UserName = userName;
    }



    @Override
    public void run() {
        try {
            while(true)
            {
                Request msg = sender.getRequest();

                switch (msg.getClientsAction()) {
                    case CREATEPROJECT: {
                        model.getDb().createProject(msg.getProject(), UserId);
                        break;
                    }
                    case GETPROJECTSBYAUTHOR:{
                        ArrayList<Project> projects;
                        projects=model.getDb().getProjectsByAuthor(UserId);
                        Response resp = new Response(projects);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETPROJECTSBYUSER:{
                        ArrayList<Project> projects;
                        projects=model.getDb().getProjectsByUser(UserId);
                        Response resp = new Response(projects);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETUSERSBYPROJECT:{
                        ArrayList<User> users;
                        users=model.getDb().getUsersByProject(msg.getProject());
                        Response resp = new Response();
                        resp.setUsers(users);
                        sender.sendResp(resp);
                        break;
                    }
                    case GETUSERS:{
                        ArrayList<User> users;
                        users=model.getDb().getAllUsers();
                        Response resp = new Response();
                        resp.setUsers(users);
                        sender.sendResp(resp);
                        break;
                    }
                    case CREATETASK: {
                        model.getDb().createTask(msg.getTask());
                        break;
                    }
                    case ADDPROJECTUSERS: {
                        model.getDb().addProjectUsers(msg.getProjectUsers());
                        break;
                    }
                    case GETTASKSBYUSER:{
                        ArrayList<Task> tasks;
                        tasks=model.getDb().getTasksByUser(UserId);
                        Response resp = new Response();
                        resp.setTasks(tasks);
                        sender.sendResp(resp);
                        break;
                    }

                    case UPDATESTATUS: {
                        model.getDb().updateStatus(msg.getTask());
                        break;
                    }

                    case SENDMESSAGE: {
                        Response resp = new Response();
                        resp.setMessage(msg.getMessage());
                        for (ClientHandler cl : clients) {
                            cl.sender.sendResp(resp);
                        }
                        break;
                    }
                    case GETINFCLIENT:{
                        Response resp = new Response();
                        User user=new User();
                        user.setUserId(UserId);
                        user.setFirstName(UserName);
                        resp.setUser(user);
                        sender.sendResp(resp);
                        break;
                    }

                }

                //обработка других действий клиента, кроме регистрации и входа
            }
        } catch (IOException | SQLException | ClassNotFoundException ignored) {}

    }
}
