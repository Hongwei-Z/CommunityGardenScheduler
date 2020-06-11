package com.CSCI3130.gardenapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User Object
 * <p> This object stores user data and keeps track of the user's assigned tasks </p>
 */

public class User implements Serializable {
    private String username;
    private String email;

    ArrayList<Task> assignedTasks = new ArrayList<>();

    /**
     * Constructor
     * @param name (required) Username/display name of the user
     * @param mail Email address of the user
     */
    public User(String name, String mail) {
        this.username = name;
        this.email = mail;
    }

    /**
     * adds a Task t to the user's assignedTasks
     * @param t (required) the Task being assigned to the user
     */
    public void addTask(Task t) {
        this.assignedTasks.add(t);
    }

    /**
     * Checks if the Task t is assigned to the user
     * @param t (required)
     * @return false if user is not assigned to t, true if user is assigned to t
     */
    public boolean isAssignedTo(Task t) {
        if (this.assignedTasks.contains(t)) {
            return true;
        }
        else {
            return false;
        }
    }

    /** returns User's username */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username to the string passed in
     * @param username user's new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** returns User's email */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the user's email to the string passed in
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** returns an arraylist of Task objects that the user is assigned to */
    public ArrayList<Task> getAssignedTasks() {
        return this.assignedTasks;
    }

    /**
     * Sets the list of user's assigned tasks to the arraylist passed in
     * @param assignedTasks
     */
    public void setAssignedTasks(ArrayList<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
