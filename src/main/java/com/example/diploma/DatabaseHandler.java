package com.example.diploma;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler extends Configs{
    Connection dbConnection;

    public DatabaseHandler()  {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Connected to the MySQL successfully");

    }

    public void signUpUser(User user) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO "+ Const.USER_TABLE+"("+Const.USER_FIRSTNAME+","+Const.USER_LASTNAME+","+Const.USER_LOGIN + ","+Const.USER_PASSWORD+","+Const.USER_EMAIL+","+ Const.USER_GENDER+","+Const.USER_FULLNAME+")"+
                "VALUES(?,?,?,?,?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,user.getFirstName());
        prSt.setString(2,user.getLastName());
        prSt.setString(3,user.getLogin());
        prSt.setString(4,user.getPassword());
        prSt.setString(5,user.getEmail());
        prSt.setString(6,user.getGender());
        prSt.setString(7,user.getFullname());

        prSt.executeUpdate();
    }

    public ResultSet getUser(User user) throws SQLException, ClassNotFoundException {
        ResultSet resSet = null;

        String select = "SELECT * FROM "+Const.USER_TABLE + " WHERE " +
                Const.USER_LOGIN + "=? AND " + Const.USER_PASSWORD + "=?";

        PreparedStatement prSt = dbConnection.prepareStatement(select);
        prSt.setString(1,user.getLogin());
        prSt.setString(2,user.getPassword());

        resSet = prSt.executeQuery();

        return resSet;
    }

    public void createProject(Project project, int userId) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO "+ Const.PROJECT_TABLE+"("+Const.PROJECT_NAME+","+Const.PROJECT_USER_ID+")"+
                "VALUES(?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,project.getProjectName());
        prSt.setInt(2, userId);
        prSt.executeUpdate();
    }


    public ArrayList<Project> getProjectsByAuthor(int iserId) {//получить все проекты для выпадающего списка по руководителю проекта
        ArrayList<Project> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement("select * from projects where " + Const.PROJECT_USER_ID+"=?");
            st.setInt(1,iserId);
            r = st.executeQuery();


            while(r.next()){
                var project = new Project(r.getString("name"));
                project.setUserId(r.getInt("userid"));
                project.setProjectId(r.getInt("projectid"));
                res.add(project);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Project> getProjectsByUser(int userId) {//получить все проекты участника для вкладки
        ArrayList<Project> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select p.* from projectusers as pu left join projects as p on pu.projectid=p.projectid where pu.userid=? union \n" +
                    "select * from projects where userid=?");
            st.setInt(1,userId);
            st.setInt(2,userId);
            r = st.executeQuery();

            while(r.next())
            {
                var project = new Project(r.getString("name"));
                project.setUserId(r.getInt("userid"));
                project.setProjectId(r.getInt("projectid"));
                res.add(project);
            }
        } catch (SQLException ex) { }

        return res;
    }


    public ArrayList<Task> getTasksByUser(int userId) {//получить все проекты участника для вкладки
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select * from tasks where userid=?");
            st.setInt(1,userId);
            r = st.executeQuery();

            while(r.next())
            {
                var task = new Task(r.getInt("taskid"),r.getString("name"),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),
                        r.getInt("projectid"),r.getInt("userid"),r.getString("status"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;
    }



    public ArrayList<User> getUsersByProject(Project project) {//получить всех участников выбранного проекта для вкладки
        ArrayList<User> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select u.* from projectusers as pu left join users as u on pu.userid=u.userid where pu.projectid=?");
            st.setInt(1,project.getProjectId());
            r = st.executeQuery();

            while(r.next())
            {
                var user = new User();
                user.setFullname(r.getString("fullname"));
                user.setUserId(r.getInt("userid"));
                res.add(user);
            }
        } catch (SQLException ex) { }

        return res;
    }
//    public ArrayList<Project> getAllProjects() {//получить все проекты
//        ArrayList<Project> res = new ArrayList<>();
//
//        try {
//            Statement st = dbConnection.createStatement();
//            ResultSet r = st.executeQuery("select * from projects ORDER BY name");
//
//            while(r.next())
//            {
//                var project = new Project(r.getString("name"));
//                project.setUserId(r.getInt("userid"));
//                project.setProjectId(r.getInt("projectid"));
//                res.add(project);
//            }
//        } catch (SQLException ex) { }
//
//        return res;
//    }

    public void addProjectUsers(ProjectUsers projectUsers) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO "+ Const.PROJECTUSER_TABLE+"("+Const.PROJECTUSER_ID+","+Const.PROJECTUSER_USER_ID+","+Const.PROJECTUSER_PROJECT_ID+")"+
                "VALUES(?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setInt(1,projectUsers.getProjectusersid());
        prSt.setInt(2,projectUsers.getUserid());
        prSt.setInt(3,projectUsers.getProjectid());

        prSt.executeUpdate();
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> res = new ArrayList<>();

        try {
            Statement st = dbConnection.createStatement();
            ResultSet r = st.executeQuery("select * from users ORDER BY lastname");

            while(r.next())
            {
                var user = new User(r.getString("firstname"),r.getString("lastname"),r.getString("login"),
                        r.getString("password"), r.getString("email"), r.getString("gender"),r.getString("fullname"));
                user.setUserId(r.getInt("userid"));
                res.add(user);
            }

        } catch (SQLException ex) { }

        return res;
    }

    public void createTask(Task task) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO "+ Const.TASK_TABLE+"("+Const.TASK_NAME+","+Const.TASK_EXECUTE_DATE+","+Const.TASK_PROJECT_ID+","
                +Const.TASK_USER_ID+","+Const.TASK_STATUS + ")"+ "VALUES(?,?,?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,task.getTaskName());
        prSt.setDate(2, Date.valueOf(task.getExecuteDate()));
        prSt.setInt(3,task.getProjectId());
        prSt.setInt(4, task.getUserId());
        prSt.setString(5,task.getStatus());
        prSt.executeUpdate();
    }

    public void updateStatus(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set status =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setString(1,task.getStatus());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

}
