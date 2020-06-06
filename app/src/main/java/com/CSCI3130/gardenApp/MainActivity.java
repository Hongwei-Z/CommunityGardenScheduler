package com.CSCI3130.gardenApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity<task> extends AppCompatActivity {

    private ArrayList<Task> allTasks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter taskAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        taskList.add(new Task("Cover tomatoes", "cover if raining", "4", null, "June 4th, 2020"));
        taskList.add(new Task("Water all of the carrots", "water them a lot", "2", "Bob", "June 2nd, 2020"));
        taskList.add(new Task("Plant cucumber transplants", null, "1", "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some dirt", "pile it", "3",  null, "June 20th, 2020"));
        taskList.add(new Task("Water more carrots", "water them a lot", "5", null, "June 30th, 2020"));
        taskList.add(new Task("Plant tomato transplants", null, "1", "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some more dirt", null, "3", "Bill", "June 4th, 2020"));
        taskList.add(new Task("Water cucumber", "water them a lot", "2", null, "June 16th, 2020"));
        taskList.add(new Task("Add compost", null, "1", "Bill", "June 10th, 2020"));
        return taskList;
    }
}


