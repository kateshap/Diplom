package com.example.diploma.controllers;

import com.example.diploma.Project;
import com.example.diploma.Task;
import com.example.diploma.User;
import request.Request;
import request.Response;
import request.Sender;

import java.net.Socket;
import java.util.ArrayList;

public class ProfileController {
    public Socket socket;
    public String userRole;
    Sender sender;
    ArrayList<String> projectNameByAuthor=new ArrayList<String>();
    ArrayList<String> projectNameByUser=new ArrayList<String>();
    ArrayList<String> taskNameByUser=new ArrayList<String>();
    ArrayList<Project> projectsByAuthor;
    ArrayList<Project> projectsByUser;
    ArrayList<Task> tasksByUser;
    ArrayList<String> userFullName=new ArrayList<String>();
    ArrayList<String> userFullNameOnCreateProject=new ArrayList<String>();
    ArrayList<User> users;
    ArrayList<User> usersOnCreateProject;
    Request req;
    Response msg;
}
