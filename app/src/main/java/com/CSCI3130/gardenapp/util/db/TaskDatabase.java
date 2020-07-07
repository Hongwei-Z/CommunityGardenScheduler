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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Database layer for writing tasks to the database. This can easily be mocked in
 * tests so that we can emulate database functions on JUnit tests.
 * @author Liam Hebert
 */
public class TaskDatabase {
    /**
     * Database reference
     */
    protected DatabaseReference dbWrite;

    /**
     * Query on the database reference
     */
    protected Query dbRead;

    /***
     * ArrayList, save all tasks
     */
    private ArrayList<Task> allTasks = new ArrayList<>();

    /**
     * Constructor that injects a database starting point. Useful for testing
     *
     * @param dbWrite Reference node to write all tasks under
     */
    public TaskDatabase(DatabaseReference dbWrite) {
        this.dbWrite = dbWrite;
        this.dbRead = dbWrite;
    }

    /**
     * Default constructor that writes tests under the root Tasks node
     */
    public TaskDatabase() {
        this.dbWrite = FirebaseDatabase.getInstance().getReference().child("Tasks");
        this.dbRead = FirebaseDatabase.getInstance().getReference().child("Tasks");
    }

    /**
     * Uploads a filled class to the firebase database
     *
     * @param task Task to be uploaded to the database
     * @return Boolean if the upload was successful
     */
    public boolean uploadTask(Task task) {
        return dbWrite.push().setValue(task).isComplete();
    }

    /**
     * Fetches the current database root node. Useful for constructing more DB layers
     *
     * @return Current database reference
     */
    public DatabaseReference getDbWrite() {
        return dbWrite;
    }

    /**
     * Fetches the current database query. Useful for filtering data
     *
     * @return Current database query
     */
    public Query getDbRead() {
        return dbRead;
    }

    /**
     * Sets the database reference used in an activity to query on specific fields
     *
     * @param activeTaskListContext query activeTaskListContext for tasklist
     */
    public void setDbRead(String activeTaskListContext) {
        /** if allTasks do nothing, no need for query **/
        switch(activeTaskListContext) {
            case "myTasks":
                this.dbRead = dbRead.orderByChild("user").equalTo(FirebaseAuth.getInstance().getUid()); // returns tasks assigned to current user
                break;
            case "openTasks":
                this.dbRead = dbRead.orderByChild("user").equalTo(""); //returns tasks with no user assigned
                break;
            case "taskHistory":
                this.dbRead = dbRead.orderByChild("dateCompleted").startAt(0).endAt(System.currentTimeMillis());
                break;
        }
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
     * @param activeTaskListContext to determine the ordering and sorting of the tasks
     * @return ValueEventListener
     */
    public ValueEventListener getTaskData(RecyclerView recyclerView, String activeTaskListContext) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTasks.clear();
                for (DataSnapshot dataSnapshotTask : dataSnapshot.getChildren()) {
                    Task task = dataSnapshotTask.getValue(Task.class);
                    allTasks.add(task);
                }

                if (activeTaskListContext.equals("taskHistory")) {
                    Comparator<Task> comparator = (Task taskA, Task taskB) ->
                            new Long(taskA.getDateCompleted()).compareTo(new Long(taskB.getDateCompleted()));
                    Collections.sort(allTasks, comparator);
                    Collections.reverse(allTasks);
                } else {
                    Comparator<Task> comparator = (Task taskA, Task taskB) ->
                            new Long(taskA.getDateDue()).compareTo(new Long(taskB.getDateDue()));
                    Collections.sort(allTasks, comparator);
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

    public ArrayList<Task> getAllTasks() {
        return allTasks;
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
