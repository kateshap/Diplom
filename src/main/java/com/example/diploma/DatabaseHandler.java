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

    public String getUserRole(User user){
        String res = "";
        ResultSet r = null;

        try {
            PreparedStatement prSt = dbConnection.prepareStatement("select * from users where login=? and password=?");
            prSt.setString(1,user.getLogin());
            prSt.setString(2,user.getPassword());
            r = prSt.executeQuery();
            r.next();

            res=r.getString("role");

        } catch (SQLException ex) { }

        return res;
    }


    public void createProject(Project project, int userId) throws SQLException, ClassNotFoundException {
        String insert = "insert into projects(name,userid,begindate,program) values(?,?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,project.getProjectName());
        prSt.setInt(2, userId);
        prSt.setDate(3, Date.valueOf(project.getBeginDate()));
        prSt.setString(4,project.getProgram());
        prSt.executeUpdate();
    }


    public ArrayList<Project> getProjectsByAuthor(int userId) {
        ArrayList<Project> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement("select * from projects where " + Const.PROJECT_USER_ID+"=?");
            st.setInt(1,userId);
            r = st.executeQuery();


            while(r.next()){
                var project = new Project(r.getString("name"),r.getString("program"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate());
                project.setUserId(r.getInt("userid"));
                project.setProjectId(r.getInt("projectid"));
                res.add(project);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Task> getKeyTasksByDirector(int userId) {
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement("SELECT  t1.fullname as fullname, tasks.*\n" +
                    "\tfrom tasks\n" +
                    "    left join users as t1 on tasks.userid=t1.userid\n" +
                    "\twhere tasks.key=1 and tasks.projectid in (select projectid \n" +
                    "\t\t\t\t\t\t\t\t\t\t\tfrom projects \n" +
                    "                                            where userid=?)");
            st.setInt(1,userId);
            r = st.executeQuery();


            while(r.next()){
                var task = new Task(r.getInt("taskid"),r.getString("name"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),
                        r.getInt("duration"),r.getInt("projectid"),r.getInt("userid"),r.getString("status"),r.getInt("parentid"),r.getInt("percent"),r.getInt("delay"));
                task.setUserName(r.getString("fullname"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Project> getProjectsByUser(int userId) {
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
                var project = new Project(r.getString("name"),r.getString("program"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate());
                project.setUserId(r.getInt("userid"));
                project.setProjectId(r.getInt("projectid"));
                res.add(project);
            }
        } catch (SQLException ex) { }

        return res;
    }


    public ArrayList<Task> getTasksByUser(int userId) {
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" SELECT projects.name as projectName,t.name as parentName,tasks.*\n" +
                                                                    "\tfrom tasks\n" +
                                                                    "    left join projects on tasks.projectid=projects.projectid\n" +
                                                                    "    left join tasks as t on tasks.parentid=t.taskid\n" +
                                                                    "    where tasks.userid=?");
            st.setInt(1,userId);
            r = st.executeQuery();

            while(r.next())
            {
                var task = new Task(r.getInt("taskid"),r.getString("name"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),
                        r.getInt("duration"),r.getInt("projectid"),r.getInt("userid"),r.getString("status"),r.getInt("parentid"),r.getInt("percent"),r.getInt("delay"));
                task.setParentName(r.getString("parentName"));
                task.setProjectName(r.getString("projectName"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Task> getTasksByProject(int projectId) {//получить все проекты участника для вкладки
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" SELECT users.fullname,t.name as parentName,tasks.*\n" +
                                                                    "\tfrom tasks\n" +
                                                                    "    left join users on tasks.userid=users.userid\n" +
                                                                    "    left join tasks as t on tasks.parentid=t.taskid\n" +
                                                                    "    where tasks.projectid=?");
            st.setInt(1,projectId);
            r = st.executeQuery();

            while(r.next())
            {
                var task = new Task(r.getInt("taskid"),r.getString("name"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),
                        r.getInt("duration"),r.getInt("projectid"),r.getInt("userid"),r.getString("status"),r.getInt("parentid"),r.getInt("percent"),r.getInt("delay"));
                task.setParentName(r.getString("parentName"));
                task.setUserName(r.getString("fullname"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<User> getUsersByProject(Project project) {
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
                        r.getString("password"), r.getString("email"), r.getString("gender"),r.getString("fullname"),r.getString("role"));
                user.setUserId(r.getInt("userid"));
                res.add(user);
            }

        } catch (SQLException ex) { }

        return res;
    }

    public void createTask(Task task) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO tasks (name,beginDate,executeDate,duration,delay,projectid,userid,status,parentid) VALUES(?,?,?,?,?,?,?,?,?)";

        PreparedStatement prSt = dbConnection.prepareStatement(insert);
        prSt.setString(1,task.getTaskName());
        prSt.setDate(2, Date.valueOf(task.getBeginDate()));
        prSt.setDate(3, Date.valueOf(task.getExecuteDate()));
        prSt.setInt(4,task.getDuration());
        prSt.setInt(5,task.getDelay());
        prSt.setInt(6,task.getProjectId());
        prSt.setInt(7, task.getUserId());
        prSt.setString(8,task.getStatus());
        prSt.setInt(9, task.getParentId());
        prSt.executeUpdate();
    }

    public void updateTaskName(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set name =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setString(1,task.getTaskName());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public void updateTaskBeginDate(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set begindate =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setDate(1, Date.valueOf(task.getBeginDate()));
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public void updateTaskExecuteDate(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set executedate =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setDate(1, Date.valueOf(task.getExecuteDate()));
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }


    public void updateTaskDuration(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set duration =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, task.getDuration());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public void updateTaskDelay(Task task) throws SQLException {
        String update = "update tasks set delay =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1, task.getDelay());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }


    public void updateTaskStatus(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set status =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setString(1,task.getStatus());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }


    public void updateTaskPercent(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set percent =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1,task.getPercent());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public void updateTaskParentId(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set parentid =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1,task.getParentId());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public void updateTaskUser(Task task) throws SQLException, ClassNotFoundException {
        String update = "update tasks set userid =? where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(update);
        prSt.setInt(1,task.getUserId());
        prSt.setInt(2, task.getTaskId());
        prSt.executeUpdate();
    }

    public ArrayList<Task> getTasksByParentId(int parentId) {
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select * from tasks where parentid=?");
            st.setInt(1,parentId);
            r = st.executeQuery();

            while(r.next())
            {
                var task = new Task(r.getInt("taskid"),r.getString("name"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),
                        r.getInt("duration"),r.getInt("projectid"),r.getInt("userid"),r.getString("status"),r.getInt("parentid"),r.getInt("percent"),r.getInt("delay"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;

    }

    public void deleteTask(Task task) throws SQLException, ClassNotFoundException  {
        String delete = "delete from tasks where taskid =?";

        PreparedStatement prSt = dbConnection.prepareStatement(delete);
        prSt.setInt(1, task.getTaskId());
        prSt.executeUpdate();
    }

    public ArrayList<Queries> getProgramsCountProjects(int iserId) {
        ArrayList<Queries> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" SELECT program, COUNT(program) as count\n" +
                                                                    "\tFROM projects\n" +
                                                                    "    WHERE userid=?\n" +
                                                                    "\tGROUP BY program");
            st.setInt(1,iserId);
            r = st.executeQuery();


            while(r.next()){
                var query = new Queries(r.getString("program"),r.getInt("count"));
                res.add(query);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Queries> getProjectsCompletedTasks(int iserId) {
        ArrayList<Queries> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select t1.name, t2.done, t2.notdone from projects as t1\n" +
                                        "right join (select t.projectid,sum(case when t.status=\"выполнена\" then 1 else 0 end) as done, \n" +
                                        "sum(case when t.status<> \"выполнена\" then 1 else 0 end) as notdone from tasks as t \n" +
                                        "where t.projectid in (select projectid from projects where userid=?) group by t.projectid) as t2\n" +
                                        "on t1.projectid = t2.projectid");
            st.setInt(1,iserId);
            r = st.executeQuery();

            while(r.next()){
                var query = new Queries(r.getString("name"),r.getInt("done"),r.getInt("notdone"));
                res.add(query);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Queries> getUsersCountProjects(int userId) {
        ArrayList<Queries> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select users.fullname, t.countProjects \n" +
                                                                    "\tfrom users\n" +
                                                                    "    right join \n" +
                                                                    "(select tasks.userid, count(tasks.projectid) as countProjects\n" +
                                                                    "from tasks left join users on tasks.userid=users.userid \n" +
                                                                    "and  users.role=\"teamMember\"\n" +
                                                                    "where tasks.projectid in (select projectid \n" +
                                                                    "\t\t\t\t\t\t\tfrom projects \n" +
                                                                    "\t\t\t\t\t\t\twhere userid=?)\n" +
                                                                    "group by tasks.userid) as t on users.userid=t.userid");
            st.setInt(1,userId);
            r = st.executeQuery();

            while(r.next()){
                var query = new Queries();
                query.setProjectsCount(r.getInt("countProjects"));
                query.setUser(r.getString("fullname"));
                res.add(query);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Queries> getUsersCountTasks(int projectId) {
        ArrayList<Queries> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select users.fullname as fullname, count(tasks.taskid) as countTasks\n" +
                    "\tfrom tasks\n" +
                    "\tright join users on tasks.userid=users.userid\n" +
                    "\twhere projectid=?\n" +
                    "    group by fullname");
            st.setInt(1,projectId);
            r = st.executeQuery();

            while(r.next()){
                var query = new Queries();
                query.setTasksCount(r.getInt("countTasks"));
                query.setUser(r.getString("fullname"));
                res.add(query);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public ArrayList<Task> getOutstandingTasks(int projectId) {
        ArrayList<Task> res = new ArrayList<>();
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select name, tasks.begindate as begindate,tasks.executedate as executedate,users.fullname as fullname\n" +
                    "from tasks\n" +
                    "left join users on users.userid=tasks.userid\n" +
                    "where tasks.projectid=? and status<> \"выполнена\"");
            st.setInt(1,projectId);
            r = st.executeQuery();

            while(r.next())
            {
                var task = new Task(r.getString("name"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate());
                task.setUserName(r.getString("fullname"));
                res.add(task);
            }
        } catch (SQLException ex) { }

        return res;
    }

    public Project getCardInfo(int projectId) {
        Project res = null;
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select t2.name as projectName,t2.begindate as begindate,t2.executedate as executedate,t2.program as projectProgram,users.fullname as dirName, t3.fullname as manName\n" +
                    "\tfrom (select projects.name,projects.begindate,executedate, projects.program,projectusers.userid as dir,pu.userid as man\n" +
                    "\t\t\tfrom projects\n" +
                    "\t\t\tleft join (select projectid, max(executedate) as executedate\n" +
                    "\t\t\t\t\t\tfrom tasks\n" +
                    "\t\t\t\t\t\tgroup by projectid) as t on projects.projectid=t.projectid\n" +
                    "\t\t   left join projectusers on projects.projectid=projectusers.projectid and projectusers.role=\"director\"\n" +
                    "\t\t   left join projectusers as pu on projects.projectid=pu.projectid and pu.role=\"manager\"\n" +
                    "\t\t   where projects.projectid=?) as t2\n" +
                    "\t\t\t   left join users on t2.dir=users.userid\n" +
                    "\t\t\t   left join users as t3 on t2.man=t3.userid\n");
            st.setInt(1,projectId);
            r = st.executeQuery();

            r.next();

            res = new Project(r.getString("projectName"),new java.sql.Date(r.getDate("begindate").getTime()).toLocalDate(),new java.sql.Date(r.getDate("executedate").getTime()).toLocalDate(),r.getString("projectProgram"),r.getString("dirName"),r.getString("manName"));
                
        } catch (SQLException ex) { }

        return res;
    }

    public Project getCardTasksCount(int projectId) {
        Project res = null;
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select count(*) as tasksCount\n" +
                                                                        "from tasks\n" +
                                                                        "where projectid=?");
            st.setInt(1,projectId);
            r = st.executeQuery();

            r.next();

            res = new Project();
            res.setTasksCount(r.getInt("tasksCount"));

        } catch (SQLException ex) { }

        return res;
    }

    public Project getCardTeamMembers(int projectId) {
        Project res = null;
        ResultSet r = null;

        try {
            PreparedStatement st = dbConnection.prepareStatement(" select DISTINCT  users.fullname as fullname\n" +
                                                                        "from tasks\n" +
                                                                        "right join users on tasks.userid=users.userid\n" +
                                                                        "where projectid=?");
            st.setInt(1,projectId);
            r = st.executeQuery();
            res = new Project();
            ArrayList<String> teamMembers = new ArrayList<>();
            while(r.next())
            {
                teamMembers.add(r.getString("fullname"));
            }
            res.setTeamMembers(teamMembers);
            
        } catch (SQLException ex) { }

        return res;
    }
}
