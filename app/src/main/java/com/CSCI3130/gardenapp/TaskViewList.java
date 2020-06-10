package com.CSCI3130.gardenapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskViewList<task> extends AppCompatActivity {

    private ArrayList<Task> allTasks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter taskAdapter;
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
}


