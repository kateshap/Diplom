package Server;

import com.example.diploma.Project;
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

    public ClientHandler(Socket clientSocket, String userName) {
        this.clientSocket = clientSocket;
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
                    case GETPROJECTS:{
                        ArrayList<Project> projects=new ArrayList<Project>();
                        projects=model.getDb().getAllProjects();
                        Response resp = new Response(projects);
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
                }

                //обработка других действий клиента, кроме регистрации и входа
            }
        } catch (IOException | SQLException | ClassNotFoundException ignored) {}

    }
}
