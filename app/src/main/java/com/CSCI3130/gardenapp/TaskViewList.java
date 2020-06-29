package com.CSCI3130.gardenapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.CSCI3130.gardenapp.util.db.DatabaseTaskWriter;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Activity class to process the task data and populate the task list
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    DatabaseTaskWriter db;
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
        toolbarTitle.setText(setting.equals("allTasks") ? "All Tasks" : "My Tasks");

        db = new DatabaseTaskWriter();
        if (setting.equals("myTasks")){
            db.setDbQuery(db.getDb().orderByChild("user").equalTo(FirebaseAuth.getInstance().getUid()));
        }

        recyclerView = findViewById(R.id.recycleview_tasks);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDbQuery().addValueEventListener(db.getTaskData(recyclerView));
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


