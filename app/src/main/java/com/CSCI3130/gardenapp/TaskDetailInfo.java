package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;
import com.CSCI3130.gardenapp.util.db.DatabaseAuth;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;


public class TaskDetailInfo extends AppCompatActivity {

    private Button registerButton,
                    completeButton,
                    editButton,
                    unregisterButton;
    private TextView description;
    private TextView dueDate;
    private TextView taskTitle;
    private TextView repeatCon;
    private TextView priority;
    private TextView priorityColor;
    private TextView location;
    private TaskDatabase db;

    private Task task;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);
        db = new TaskDatabase();
        user = DatabaseAuth.getCurrentUser();

        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(view -> {
            task.setUser(user.getId());
            db.updateTask(task);
            Intent returnIntent = new Intent(this, TaskViewList.class);
            returnIntent.putExtra(getString(R.string.task_extra), task);
            startActivity(returnIntent);
        });

        task = (Task) getIntent().getSerializableExtra(getString(R.string.task_extra));

        taskTitle = findViewById(R.id.taskTitle);

        priority = findViewById(R.id.taskPriority);
        priorityColor = findViewById(R.id.taskColor);
        description = findViewById(R.id.taskDescription);
        dueDate = findViewById(R.id.taskDuedate);
        location = findViewById(R.id.taskLocation);
        repeatCon = findViewById(R.id.repeatCondition);
        completeButton = findViewById(R.id.buttonComplete);
        editButton = findViewById(R.id.buttonEdit);
        registerButton = findViewById(R.id.buttonRegister);
        unregisterButton = findViewById(R.id.buttonUnregister);



        taskTitle.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());
        dueDate.setText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()));
        if (task.getRepeated() == null) {//if no repeat set (Legacy tasks)
            repeatCon.setText("");
            repeatCon.setVisibility(View.INVISIBLE);
        }
        else {
            repeatCon.setText(processRepeatCondition(task.getRepeated()));
        }

        priority.setText(Integer.toString(task.getPriority()));
        int color = R.color.colorPriority + task.getPriority();
        priorityColor.setBackgroundResource(color);

        if (!task.getUser().equals("")) {//if task is assigned to a user
            registerButton.setVisibility(View.GONE);
        }

        if (!task.getUser().equals(user.getId()) && !task.getUser().equals("")) {//if task is not assigned to current user, and task is not open
            completeButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }
    }

    /**
     * Processes the repeat condition into an readable string for user
     *
     * @param repeat
     * @return
     */
    public String processRepeatCondition(String repeat) {
        switch (repeat) {
            case "repeat-none":
                return "Do not repeat";
            case "repeat-2day":
                return "Repeat every 2 days";
            case "repeat-weekly":
                return "Repeat every week";
            case "repeat-monthly":
                return "Repeat every month";
            default:
                return "Do not repeat";
        }
    }

    /**
     * Passes task to edit page to edit
     * @param v
     */
    public void onEdit(View v) {
        Intent editTask = new Intent(this, CreateTaskActivity.class);
        editTask.putExtra(getString(R.string.editSetting_extra), true);
        editTask.putExtra(getString(R.string.task_extra), task);
        startActivity(editTask);
    }


    /**
     * Completes task and processes a repeated task
     */
    public void onComplete(View v) {
        task.setDateCompleted(System.currentTimeMillis());
        db.updateTask(task);
        if (!task.getRepeated().equals("repeat-none")) {//if task is repeated
            Task repeatedTask;
            switch (task.getRepeated()) {//get type of repeat and set new task
                case "repeat-weekly":
                    repeatedTask = new Task(task.getName(), task.getDescription(), task.getPriority(), "", task.getLocation(), System.currentTimeMillis()+TimeUnit.DAYS.toMillis(7), task.getRepeated());
                    break;
                case "repeat-monthly":
                    repeatedTask = new Task(task.getName(), task.getDescription(), task.getPriority(), "", task.getLocation(), System.currentTimeMillis()+TimeUnit.DAYS.toMillis(30), task.getRepeated());
                    break;
                default:
                    repeatedTask = new Task(task.getName(), task.getDescription(), task.getPriority(), "", task.getLocation(), System.currentTimeMillis()+TimeUnit.DAYS.toMillis(2), task.getRepeated());
            }
            db.uploadTask(repeatedTask);
        }
        Intent returnIntent = new Intent(this, TaskViewList.class);
        returnIntent.putExtra(getString(R.string.task_extra), task);
        startActivity(returnIntent);
    }


}