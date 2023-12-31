package com.CSCI3130.gardenapp.util.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User Object
 * <p> This object stores user data and keeps track of the user's assigned tasks </p>
 *
 * @author Logan Sutherland
 */

public class User implements Serializable {
    ArrayList<Task> assignedTasks = new ArrayList<>();
    private String username;
    private String email;
    private String userId;

    /**
     * Constructor
     *
     * @param name (required) Username/display name of the user
     * @param mail Email address of the user
     */
    public User(String name, String mail, String userId) {
        this.username = name;
        this.email = mail;
        this.userId = userId;
    }

    /**
     * adds a Task t to the user's assignedTasks
     *
     * @param t (required) the Task being assigned to the user
     */
    public void addTask(Task t) {
        this.assignedTasks.add(t);
    }

    /**
     * Checks if the Task t is assigned to the user
     *
     * @param t (required)
     * @return false if user is not assigned to t, true if user is assigned to t
     */
    public boolean isAssignedTo(Task t) {
        return this.assignedTasks.contains(t);
    }

    /**
     * returns User's username
     *
     * @return users username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username to the string passed in
     *
     * @param username user's new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * returns User's email
     *
     * @return users email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the user's email to the string passed in
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * returns an arraylist of Task objects that the user is assigned to
     *
     * @return list of assigned tasks for the user
     */
    public ArrayList<Task> getAssignedTasks() {
        return this.assignedTasks;
    }

    /**
     * Sets the list of user's assigned tasks to the arraylist passed in
     *
     * @param assignedTasks
     */
    public void setAssignedTasks(ArrayList<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    /**
     * returns the id of a user
     *
     * @return
     */
    public String getId() {
        return this.userId;
    }

    /**
     * sets the id of a user
     *
     * @param newId user's new id
     */
    public void setId(String newId) {
        this.userId = newId;
    }
}
