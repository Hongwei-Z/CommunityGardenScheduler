package com.CSCI3130.gardenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskViewList<task> extends AppCompatActivity {

    private ArrayList<Task> allTasks = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_list);

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        allTasks = populateTasks();
        taskAdapter = new TaskAdapter(allTasks);
        recyclerView.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                User u = new User("Logan", "sutherland@dal.ca"); //dummy user, replace with actual user when firebase is setup
                registerForTask(position, u); //position refers to index of task in recyclerview tasklist

            }
        });
    }

    //hardcoded until firebase is set up
    public static ArrayList<Task> populateTasks(){
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Cover tomatoes", "cover if raining", 4, "", "June 4th, 2020"));
        taskList.add(new Task("Water all of the carrots", "water them a lot", 2, "Bob", "June 2nd, 2020"));
        taskList.add(new Task("Plant cucumber transplants", "", 1, "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some dirt", "pile it", 3,  "", "June 20th, 2020"));
        taskList.add(new Task("Water more carrots", "water them a lot", 5, "", "June 30th, 2020"));
        taskList.add(new Task("Plant tomato transplants", "", 1, "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some more dirt", "", 3, "Bill", "June 4th, 2020"));
        taskList.add(new Task("Water cucumber", "water them a lot", 3, "", "June 16th, 2020"));
        taskList.add(new Task("Add compost", "", 1, "Bill", "June 10th, 2020"));
        return taskList;
    }

    public void registerForTask(int position, User user) {
        Task t = allTasks.get(position); //get task from recyclerview
        int LAUNCH_REGISTER_ACTIVITY = 1; //return code is used to differentiate returns from different activites in onActivityResult
        Intent registerTaskActivity = new Intent(TaskViewList.this, TaskRegisterDummyPage.class);
        registerTaskActivity.putExtra(getString(R.string.task_extra), t);
        registerTaskActivity.putExtra(getString(R.string.user_extra), user);
        registerTaskActivity.putExtra(getString(R.string.position_extra), position); //we need to send the position over in order to preserve it and use it to update the task in recyclerview when the activity returns
        startActivityForResult(registerTaskActivity, LAUNCH_REGISTER_ACTIVITY); //By starting a new activity using this function, the function onActivityResult will be called when the activity finishes

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//called when an activity started with startActivityForResult finishes
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { //if activity finished was TaskRegisterDummyPage
            if (resultCode == Activity.RESULT_OK) { //if data has been sent over properly
                int pos = data.getIntExtra(getString(R.string.position_extra), 1); //get task position
                Task t = (Task) data.getSerializableExtra(getString(R.string.task_extra)); //get task from activity
                allTasks.set(pos, t); //replace old task with the updated task
                taskAdapter.notifyItemChanged(pos); //update recyclerview display
            }
        }
    }
}


