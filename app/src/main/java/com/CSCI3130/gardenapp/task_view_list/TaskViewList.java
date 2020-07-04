package com.CSCI3130.gardenapp.task_view_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import android.view.View;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;

import com.CSCI3130.gardenapp.util.db.TaskDatabase;


/**
 * Activity class to process the task data and populate the task list
 *
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    TaskDatabase db;
    private String lastContext = "allTasks"; //default to all tasks
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static Context mContext;

    /**
     * Actions for when activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_list);
        setContext(this);
        if (getIntent().getBooleanExtra("result", false)) {
            Snackbar.make(findViewById(R.id.task_view_list), "Success!", Snackbar.LENGTH_SHORT).show();
        }

        String activeTaskListContext = getIntent().getStringExtra("activeTaskListContext");
        if (activeTaskListContext == null) {
            activeTaskListContext = lastContext;
        }
        TextView toolbarTitle = (TextView) findViewById(R.id.page_name);
        lastContext = activeTaskListContext;

        switch(activeTaskListContext) {
            case "myTasks":
                toolbarTitle.setText("My Tasks");
                break;
            case "openTasks":
                toolbarTitle.setText("Open Tasks");
                break;
            case "taskHistory":
                toolbarTitle.setText("Task History");
                break;
            default:
                toolbarTitle.setText("All Tasks");
                break;
        }

        db = new TaskDatabase();
        db.setDbRead(activeTaskListContext);

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDbRead().addValueEventListener(
                db.getTaskData(recyclerView, activeTaskListContext));

    }


    /**
     * Get the activity context
     *
     * @return context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * Set the activity context
     *
     * @param mContext context
     */
    public void setContext(Context mContext) {
        TaskViewList.mContext = mContext;
    }

    /**
     * On Press method for the hovering + button to link to the CreateTask screen.
     * @param v View belonging to the + button
     */
    public void onButtonPress(View v) {
        Intent i = new Intent(this, CreateTaskActivity.class);
        i.putExtra("edit", false);
        startActivity(i);
    }

}


