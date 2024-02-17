package Server;

import com.example.diploma.DatabaseHandler;
import model.Model;
import model.ModelBuilder;
import request.Request;
import request.Response;
import request.Sender;
import request.ServReaction;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    int port = 3124;
    InetAddress ip = null;
    ExecutorService service = Executors.newFixedThreadPool(10); //ограничиваем кол-во клиентких потоков 4
    ArrayList<ClientHandler> allClients = new ArrayList<>();

    Sender sender;
    Model model = ModelBuilder.build();



    public void  bcast(){
        //allClients.forEach(ClientAtServer::sendInfoToClient);
    }

    public void serverStart(){
        ServerSocket ss;
        try {
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 2, ip);
            System.out.println("Server start\n");

            DatabaseHandler dbHandler = new DatabaseHandler();
            model.init(this, dbHandler);

            while(true)
            {
                Socket cs;
                cs = ss.accept();

                sender = new Sender(cs);
                Request msg = sender.getRequest();
                //String respName = msg.getPlayerName();
                if (tryAddClient(cs, msg)) {
                    System.out.println(" Connected " + msg.getUser().getFirstName());
                } else {
                    cs.close();
                }
            }

        } catch (IOException | SQLException | ClassNotFoundException ignored) {}
    }

    private boolean tryAddClient(Socket sock, Request msg) throws SQLException, ClassNotFoundException {
        ResultSet result=null;

        Response resp = new Response(ServReaction.SUCCESS);

        //проверка регистрация или вход и разные проверки из этого
        switch (msg.getClientsAction()) {
            case SIGNUP: {
                model.getDb().signUpUser(msg.getUser());
                break;
            }
            case SIGNIN: {
                result = model.getDb().getUser(msg.getUser());
                int counter = 0;

                while (result.next()) {
                    counter++;
                }
                if (counter < 1) {
                    resp = new Response(ServReaction.FAIL);
                }
                break;
            }
            default: {
                resp = new Response(ServReaction.FAIL);
                break;
            }
        }

        sender.sendResp(resp);
        if (resp.getServReaction() == ServReaction.SUCCESS) {
            ClientHandler c = new ClientHandler(sock, msg.getUser().getFirstName());

            result = model.getDb().getUser(msg.getUser());
            result.next();
            c.setUserName(result.getString("firstname"));
            c.setUserId(result.getInt("idusers"));
            allClients.add(c);
            service.submit(c);
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        new Server().serverStart();
    }

}