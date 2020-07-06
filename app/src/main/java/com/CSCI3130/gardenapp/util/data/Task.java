package com.CSCI3130.gardenapp.util.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Task.java - a class used to create Task objects to be added to the database and displayed in
 * the list of tasks
 *
 * @author Elizabeth Eddy
 */
public class Task implements Serializable {
    private String name;
    private String description;
    private int priority;
    private String user;
    private long dateDue = -1;
    private long dateCompleted = -1;
    private String location;
    private String repeated;
    private boolean open;
    private String taskID;
    private WeatherCondition weatherTrigger;

    /**
     * Constructor for the Task object
     *
     * @param name        name of the task
     * @param description description of the task
     * @param priority    indicator of how high the task priority is
     * @param user        name of user assigned to task
     * @param location    location where the task should be performed
     * @param dateDue     due date of the task
     */
    public Task(String name, String description, int priority, String user, String location, long dateDue, String repeated) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.dateDue = dateDue;
        this.location = location;
        this.repeated = repeated;
        this.open = false;

        //sets weather trigger to "none" by default - can be set explicitly using set method
        this.weatherTrigger = WeatherCondition.NONE;
    }

    /**
     * Constructor for the Task object
     *
     * @param name           name of the task
     * @param description    description of the task
     * @param priority       indicator of how high the task priority is
     * @param user           name of user assigned to task
     * @param location       location where the task should be performed
     * @param weatherTrigger weather condition for task
     * @param dateDue        due date of the task
     */
    public Task(String name, String description, int priority, String user, String location, WeatherCondition weatherTrigger, long dateDue) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.user = user;
        this.location = location;
        this.open = false;
        this.dateDue = dateDue;
        this.weatherTrigger = weatherTrigger;
    }

    public Task() {
    }

    /**
     * Gets the location of the task
     *
     * @return location of the task
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the location of the task
     *
     * @param location the location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * gets whether the value is open or not
     *
     * @return open status of the task
     */
    public boolean getOpen() {
        return open;
    }

    /**
     * sets whether or not the task is open or not
     *
     * @param open true = open, false = taken
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Gets the task name
     *
     * @return name of the task
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the task name
     *
     * @param name of the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the task description
     *
     * @return description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task description
     *
     * @param description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the task priority
     *
     * @return priority of the task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the task priority
     *
     * @param priority of the task
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the task user
     *
     * @return user of the task
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the task user
     *
     * @param user of the task
     */
    public void setUser(String user) {
        this.user = user;
    }


    /**
     * Gets the task dateDue timestamp
     *
     * @return dateDue of the task in timestamp format
     */
    public long getDateDue() {
        return dateDue;
    }

    /**
     * Sets the task dateDue
     *
     * @param dateDue of the task
     */
    public void setDateDue(long dateDue) {
        this.dateDue = dateDue;
    }

    /**
     * Gets the task dateCompleted timestamp
     *
     * @return dateCompleted of the task in timestamp format
     */
    public long getDateCompleted() {
        return dateCompleted;
    }

    /**
     * Sets the task dateCompleted
     *
     * @param dateCompleted of the task
     */
    public void setDateCompleted(long dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    /**
     * Gets weather trigger of task
     *
     * @return weather condition trigger of task
     */
    public WeatherCondition getWeatherTrigger() {
        return weatherTrigger;
    }

    /**
     * Sets weather trigger of task
     *
     * @param weatherCondition weather condition to be set as weather trigger of task
     */
    public void setWeatherTrigger(WeatherCondition weatherCondition) {
        this.weatherTrigger = weatherCondition;
    }

    /**
     * Getter for the unique location in the database where this task is stored
     *
     * @return Unique taskID of this task
     */
    public String getTaskID() {
        return taskID;
    }

    /**
     * Sets the unique task ID for this task to point to where it is stored in the database
     *
     * @param taskID of the Task to be stored
     * @see com.CSCI3130.gardenapp.util.db.TaskDatabase
     */
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    /**
     * Gets the repeat status of the task
     * @return
     */
    public String getRepeated() {
        return this.repeated;
    }

    public void setRepeated(String repeated) {
        this.repeated = repeated;
    }

    /**
     * Equals override to check fields of the task
     *
     * @param o object to compare to
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
                Objects.equals(location, task.location) &&
                weatherTrigger == task.weatherTrigger;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", user='" + user + '\'' +
                ", dateDue=" + dateDue +
                ", dateCompleted=" + dateCompleted +
                ", location='" + location + '\'' +
                ", open=" + open +
                ", weatherTrigger=" + weatherTrigger +
                '}';
    }
}
