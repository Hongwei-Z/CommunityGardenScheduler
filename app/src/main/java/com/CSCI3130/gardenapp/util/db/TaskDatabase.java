package com.CSCI3130.gardenapp.util.db;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.SortCategory;
import com.CSCI3130.gardenapp.SortOrder;
import com.CSCI3130.gardenapp.TaskDetailInfo;
import com.CSCI3130.gardenapp.task_view_list.TaskAdapter;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Database layer for writing tasks to the database. This can easily be mocked in
 * tests so that we can emulate database functions on JUnit tests.
 *
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
        DatabaseReference location = dbWrite.push();
        task.setTaskID(location.getKey());
        return location.setValue(task).isComplete();
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
        // if allTasks do nothing, no need for query
        switch (activeTaskListContext) {
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
        return dbWrite.child(task.getTaskID()).setValue(task).isComplete();
    }

    /**
     * Returns the event listener for the database to retrieve tasks
     *
     * @param recyclerView
     * @param activeTaskListContext to determine the ordering and sorting of the tasks
     * @return ValueEventListener
     */
    public ValueEventListener getTaskData(RecyclerView recyclerView, String activeTaskListContext) {
        ArrayList<Task> allTasks = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTasks.clear();
                for (DataSnapshot dataSnapshotTask : dataSnapshot.getChildren()) {
                    Task task = dataSnapshotTask.getValue(Task.class);
                    allTasks.add(task);
                }

                if (activeTaskListContext.equals("taskHistory")) {
                    Comparator<Task> comparator = Comparator.comparingLong(Task::getDateCompleted);
                    allTasks.sort(comparator);
                    Collections.reverse(allTasks);
                } else {
                    Comparator<Task> comparator = Comparator.comparingLong(Task::getDateDue);
                    allTasks.sort(comparator);

                    //move weather tasks to the end of the queue based on whether they match with the current weather
                    for (int i = 0; i < allTasks.size(); i++) {
                        Task curr = allTasks.get(i);
                        //current task has a weather trigger
                        WeatherCondition trigger = curr.getWeatherTrigger();
                        if (trigger != WeatherCondition.NONE) {
                            //if this task's weather trigger does not match those of the current weather conditions, throw it down to the bottom of the queue
                            ArrayList<WeatherCondition> currList = CurrentWeather.currentWeatherList;
                            if (currList.contains(trigger)) {
                                allTasks.remove(curr);
                                allTasks.add(0, curr);
                            } else {
                                allTasks.remove(curr);
                                allTasks.add(allTasks.size(), curr);
                            }
                        }
                    }

                }

                TaskAdapter taskAdapter = new TaskAdapter(allTasks, activeTaskListContext);
                recyclerView.setAdapter(taskAdapter);
                taskAdapter.setOnItemClickListener(position -> {
                    openTaskDetails(position, allTasks); //position refers to index of task in recyclerview tasklist
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
    }


    /**
     * Returns the event listener for the database to retrieve tasks
     * Specific method overload for sorting by category with order
     * @param recyclerView
     * @param sortCategory - sort category
     * @param sortOrder - sort order
     * @return ValueEventListener
     */
    public ValueEventListener getTaskData(RecyclerView recyclerView, String activeTaskListContext, SortCategory sortCategory, SortOrder sortOrder) {
        ArrayList<Task> allTasks = new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allTasks.clear();
                for (DataSnapshot dataSnapshotTask : dataSnapshot.getChildren()) {
                    Task task = dataSnapshotTask.getValue(Task.class);
                    allTasks.add(task);
                }

                switch (sortCategory){
                    case DUEDATE:
                        Comparator<Task> comparator = Comparator.comparingLong(Task::getDateDue);
                        allTasks.sort(comparator);
                        //sort in descending order if applicable
                        if (sortOrder == SortOrder.DESCENDING){
                            Collections.reverse(allTasks);
                        }
                        break;
                    case PRIORITY:
                        Comparator<Task> comparator1 = Comparator.comparingLong(Task::getPriority);
                        allTasks.sort(comparator1);
                        //sort in descending order if applicable
                        if (sortOrder == SortOrder.DESCENDING){
                            Collections.reverse(allTasks);
                        }
                        break;
                    case AZ:
                        Comparator<Task> comparator2 = Comparator.comparing(Task::getName);
                        allTasks.sort(comparator2);
                        //sort in descending order if applicable
                        if (sortOrder == SortOrder.DESCENDING){
                            Collections.reverse(allTasks);
                        }
                        break;
                    default:
                        break;
                }



                TaskAdapter taskAdapter = new TaskAdapter(allTasks, activeTaskListContext);
                recyclerView.setAdapter(taskAdapter);
                taskAdapter.setOnItemClickListener(position -> {
                    openTaskDetails(position, allTasks); //position refers to index of task in recyclerview tasklist
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        };
    }

    /**
     * Opens the task details page and passes the selected task to the new Task Details activity
     *
     * @param position
     * @param tasks
     */
    public void openTaskDetails(int position, ArrayList<Task> tasks) {
        Task t = tasks.get(position);
        Context taskList = TaskViewList.getContext(); //allows us to start activities inside DatabaseTaskWriter
        Intent taskDetailActivity = new Intent();
        taskDetailActivity.setClass(taskList, TaskDetailInfo.class);
        taskDetailActivity.putExtra(taskList.getString(R.string.task_extra), t);
        taskList.startActivity(taskDetailActivity);
    }
}
