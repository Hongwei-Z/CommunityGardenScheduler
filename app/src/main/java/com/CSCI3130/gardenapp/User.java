package com.CSCI3130.gardenapp;

import java.util.ArrayList;

public class User {
    String username;
    String email;

    ArrayList<Task> assignedTasks = new ArrayList<>();

    public User(String name, String mail) {
        this.username = name;
        this.email = mail;
    }

    public void addTask(Task t) {
        assignedTasks.add(t);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(ArrayList<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
