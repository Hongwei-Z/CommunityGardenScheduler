package com.CSCI3130.gardenApp;

public class Task {
    private String name;
    private String description;
    private String priority;
    private String user;
    private String date;

    public Task() {
    }

    public Task(String name, String description, String priority, String user, String date) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.date = date;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
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
