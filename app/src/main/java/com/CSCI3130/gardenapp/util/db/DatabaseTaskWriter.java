package com.CSCI3130.gardenapp.util.db;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.TaskAdapter;
import com.CSCI3130.gardenapp.TaskRegisterDummyPage;
import com.CSCI3130.gardenapp.TaskViewList;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;
import com.google.firebase.database.*;

import java.util.ArrayList;

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
     *
     * @param db Reference node to write all tasks under
     */
    public DatabaseTaskWriter(DatabaseReference db) {
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
     *
     * @param task Task to be uploaded to the database
     * @return Boolean if the upload was successful
     */
    public boolean uploadTask(Task task) {
        return db.push().setValue(task).isComplete();
    }

    /**
     * Fetches the current database root node. Useful for constructing more DB layers
     *
     * @return Current database reference
     */
    public DatabaseReference getDb() {
        return db;
    }

    /**
     * Replaces the task in the database with new one
     *
     * @param task Task to replace
     * @return Boolean value whether the task was successfully replaced
     */
    public boolean updateTask(Task task) {
        return true;
    }

    /**
     * Returns the event listener for the database to retrieve tasks
     *
     * @param recyclerView
     * @return ValueEventListener
     */
    public ValueEventListener getTaskData(RecyclerView recyclerView) {
        ArrayList<Task> allTasks = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTasks.clear();
                for (DataSnapshot dataSnapshotTask : dataSnapshot.getChildren()) {
                    Task task = dataSnapshotTask.getValue(Task.class);
                    allTasks.add(task);
                }
                TaskAdapter taskAdapter = new TaskAdapter(allTasks);
                recyclerView.setAdapter(taskAdapter);
                taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        User u = new User("Logan", "sutherland@dal.ca"); //dummy user, replace with actual user when firebase is setup
                        registerForTask(position, u, allTasks); //position refers to index of task in recyclerview tasklist

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
    }

    /**
     * Creates a new activity that allows a user to register for a task
     * 
     * @param position index of task
     * @param user     user of the app
     */
    public void registerForTask(int position, User user, ArrayList<Task> tasks) {
        Task t = tasks.get(position); //get task from recyclerview
        Context taskList = TaskViewList.getContext();
        Intent registerTaskActivity = new Intent();
        registerTaskActivity.setClass(taskList, TaskRegisterDummyPage.class);
        registerTaskActivity.putExtra(taskList.getString(R.string.task_extra), t);
        registerTaskActivity.putExtra(taskList.getString(R.string.user_extra), user);
        registerTaskActivity.putExtra(taskList.getString(R.string.position_extra), position); //we need to send the position over in order to preserve it and use it to update the task in recyclerview when the activity returns
        taskList.startActivity(registerTaskActivity);
    }
}
