package com.CSCI3130.gardenapp.util.db;

import com.CSCI3130.gardenapp.util.data.Task;
import com.google.firebase.database.*;

/**
 * Database layer for writing tasks to the database. This can easily be mocked in
 * tests so that we can emulate database functions on JUnit tests.
 * @author Liam Hebert
 */
public class DatabaseTaskWriter {
    /**
     * Database reference
     */
    protected DatabaseReference db;

    /**
     * Constructor that injects a database starting point. Useful for testing
     * @param db Reference node to write all tasks under
     */
    public DatabaseTaskWriter(DatabaseReference db){
        this.db = db;
    }

    /**
     * Default constructor that writes tests under the root Tasks node
     */
    public DatabaseTaskWriter() {
        this.db = FirebaseDatabase.getInstance().getReference().child("Tasks");
    }

    /**
     * Uploads a filled class to the firebase database
     * @param task Task to be uploaded to the database
     * @return Boolean if the upload was successful
     */
    public boolean uploadTask(Task task) {
        return db.push().setValue(task).isComplete();
    }

    /**
     * Fetches the current database root node. Useful for constructing more DB layers
     * @return Current database reference
     */
    public DatabaseReference getDb(){
        return db;
    }

    /**
     * Replaces the task in the database with new one
     * @param task Task to replace
     * @return Boolean value whether the task was successfully replaced
     */
    public boolean updateTask(Task task) {
        return true;
    }


}
