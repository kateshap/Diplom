package com.example.diploma;

import java.sql.*;
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

//    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
//        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
//
//        Class.forName("com.mysql.jdbc.Driver");
//
//        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
//
//        return dbConnection;
//    }

    public void signUpUser(User user) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO "+ Const.USER_TABLE+"("+Const.USER_FIRSTNAME+","+Const.USER_LASTNAME+","+Const.USER_LOGIN + ","+Const.USER_PASSWORD+","+Const.USER_EMAIL+","+ Const.USER_GENDER+")"+
                "VALUES(?,?,?,?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,user.getFirstName());
        prSt.setString(2,user.getLastName());
        prSt.setString(3,user.getLogin());
        prSt.setString(4,user.getPassword());
        prSt.setString(5,user.getEmail());
        prSt.setString(6,user.getGender());

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
        String insert = "INSERT INTO "+ Const.PROJECT_TABLE+"("+Const.PROJECT_NAME+","+Const.PROJECT_ID_USER+")"+
                "VALUES(?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,project.getProjectName());
        prSt.setString(2, String.valueOf(userId));
        prSt.executeUpdate();
    }


    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> res = new ArrayList<>();

        try {
            Statement st = dbConnection.createStatement();
            ResultSet r = st.executeQuery("select * from projects ORDER BY name");

            while(r.next())
            {
                var project = new Project(r.getString("name"));
                project.setIdUser(r.getInt("iduser"));
                project.setIdProject(r.getInt("idproject"));
                res.add(project);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> res = new ArrayList<>();

        try {
            Statement st = dbConnection.createStatement();
            ResultSet r = st.executeQuery("select * from users ORDER BY lastname");

            while(r.next())
            {
                res.add(r.getString("fullname"));
            }

        } catch (SQLException ex) { }

        return res;
    }
}
