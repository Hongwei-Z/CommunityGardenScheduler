package com.CSCI3130.gardenapp;

import java.io.Serializable;

/**
 * Task.java - a class used to create Task objects to be added to the database and displayed in
 * the list of tasks
 * @author  Elizabeth Eddy
 */
public class Task implements Serializable {
    private String name;
    private String description;
    private int priority;
    private String user;
    private String date;

    /**
     * Constructor for the Task object
     * @param name  name of the task
     * @param description description of the task
     * @param priority indicator of how high the task priority is
     * @param user name of user assigned to task
     * @param date due date of the task
     */
    public Task(String name, String description, int priority, String user, String date) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.date = date;
    }

    /**
     * Checks if any user is assigned to the Task
     * @return true if no user is assigned, false if user is assigned
     */
    public boolean isOpen() {
        if (user.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets the task name
     * @return name of the task
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the task name
     * @param name of the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the task description
     * @return description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task description
     * @param description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the task priority
     * @return priority of the task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the task priority
     * @param priority of the task
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the task user
     * @return user of the task
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the task user
     * @param user of the task
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the task date
     * @return date of the task
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the task date
     * @param date of the task
     */
    public void setDate(String date) {
        this.date = date;
    }
}
