package com.CSCI3130.gardenapp;

import android.content.Context;
import android.os.Bundle;

import com.CSCI3130.gardenapp.util.db.DatabaseTaskWriter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.util.data.Task;
import java.util.ArrayList;

/**
 * Activity class to process the task data and populate the task list
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    DatabaseTaskWriter db;
    private ArrayList<Task> allTasks = new ArrayList<>();
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    TaskAdapter taskAdapter = new TaskAdapter(allTasks);
    private static Context mContext;

    /**
     * Actions for when activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_list);
        setContext(this);
        db = new DatabaseTaskWriter();
        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDb().addValueEventListener(db.getTaskData(recyclerView));
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


