package com.CSCI3130.gardenapp;

public class Task {
    private String name;
    private String description;
    private int priority;
    private String user;
    private String date;

    Task(String name, String description, int priority, String user, String date) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.date = date;
    }

    public boolean isOpen() { //returns true if task is not assigned to anyone and is open
        if (user.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
