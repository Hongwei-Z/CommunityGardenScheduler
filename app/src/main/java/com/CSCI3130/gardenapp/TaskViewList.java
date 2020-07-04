package com.CSCI3130.gardenapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.CSCI3130.gardenapp.util.db.TaskDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Activity class to process the task data and populate the task list
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    TaskDatabase db;
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static Context mContext;
    private String setting;

    /**
     * Actions for when activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_list);
        setContext(this);
        setting = getIntent().getStringExtra("setting");
        TextView toolbarTitle = (TextView) findViewById(R.id.page_name);

        switch(setting) {
            case "myTasks":
                toolbarTitle.setText("My Tasks");
                break;
            case "openTasks":
                toolbarTitle.setText("Open Tasks");
                break;
            default:
                toolbarTitle.setText("All Tasks");
                break;
        }

        db = new TaskDatabase();
        db.setDbRead(setting);

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDbRead().addValueEventListener(db.getTaskData(recyclerView));
    }

    /**
     * Get the actvity context
     * @return context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * Set the activity context
     * @param mContext context
     */
    public void setContext(Context mContext) {
        TaskViewList.mContext = mContext;
    }
}


