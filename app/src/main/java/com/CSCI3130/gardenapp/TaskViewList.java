package com.CSCI3130.gardenapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import java.util.ArrayList;

/**
 * Activity class to process the task data and populate the task list
 *
 * @author Elizabeth Eddy and Logan Sutherland
 */
public class TaskViewList extends AppCompatActivity {

    TaskDatabase db;
    protected RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static Context mContext;
    public static String activeTaskListContext;

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
        activeTaskListContext = getIntent().getStringExtra("activeTaskListContext");
        TextView toolbarTitle = (TextView) findViewById(R.id.page_name);

        switch (activeTaskListContext) {
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

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        db.getDbRead().addValueEventListener(
                db.getTaskData(recyclerView, activeTaskListContext));

    }

    /**
     * Get the actvity context
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
     * This is the filter popup method
     * @param v show the popup window
     */
    public void PopUp(View v) {
        startActivityForResult(new Intent(this, FilterPopUp.class), 1024);
    }

    /***
     * onActivityResult method used to filter the result what user expected.
     * @param requestCode Indicate the source of the request
     * @param resultCode Indicate which activity the returned data comes from
     * @param data Data pass
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && resultCode == RESULT_OK) {

            long start = data.getLongExtra("start", Long.MIN_VALUE);
            long end = data.getLongExtra("end", Long.MAX_VALUE);
            int priority = data.getIntExtra("priority", -1);

            ArrayList<Task> allTasks = db.getAllTasks();
            ArrayList<Task> newTasks = new ArrayList<>();

            for (Task task : allTasks) {
                if (task.getDateDue() >= start && task.getDateDue() <= end) {
                    if (priority != -1) {
                        if (priority == task.getPriority()) {
                            newTasks.add(task);
                        }
                    }
                    else {
                        newTasks.add(task);
                    }
                }
            }

            TaskAdapter taskAdapter = new TaskAdapter(newTasks);
            recyclerView.setAdapter(taskAdapter);
            taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    User u = new User("Logan", "sutherland@dal.ca"); //dummy user, replace with actual user when firebase is setup
                    db.registerForTask(position, u, allTasks); //position refers to index of task in recyclerview tasklist
                }
            });
        }
    }
}