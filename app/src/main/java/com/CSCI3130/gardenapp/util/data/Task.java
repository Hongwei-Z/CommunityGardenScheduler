package com.CSCI3130.gardenapp.util.data;

import java.io.Serializable;
import java.util.Objects;

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
    private String location;
    private boolean open;
    private int taskId;

    /**
     * Constructor for the Task object
     * @param name  name of the task
     * @param description description of the task
     * @param priority indicator of how high the task priority is
     * @param user name of user assigned to task
     * @param location location where the task should be performed
     * @param date due date of the task
     */
    public Task(String name, String description, int priority, String user, String location, String date) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.date = date;
        this.location = location;
        this.open = false;
    }

    public Task(){}

    /**
     * Gets the location of the task
     * @return location of the task
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the location of the task
     * @param location the location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * sets whether or not the task is open or not
     * @param open true = open, false = taken
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * gets whether the value is open or not
     * @return open status of the task
     */
    public boolean getOpen() {
        return open;
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

    /**
     * Equals override to check fields of the task
     * @param o object to comapre to
     * @return whether or not the objects are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return priority == task.priority &&
                name.equals(task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(user, task.user) &&
                date.equals(task.date) &&
                Objects.equals(location, task.location);
    }

    public int getId() {
        return this.taskId;
    }

    public void setId(int newId) {
        this.taskId = newId;
    }
}
