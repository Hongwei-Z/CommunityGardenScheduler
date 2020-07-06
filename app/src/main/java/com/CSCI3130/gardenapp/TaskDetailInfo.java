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


public class TaskDetailInfo extends AppCompatActivity {

    private Button registerButton;
    private TextView description;
    private TextView dueDate;
    private TextView taskTitle;
    private TextView repeatCon;
    private TextView priority;
    private TextView priorityColor;
    private TaskDatabase db;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);
        db = new TaskDatabase();

        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(view -> {
            User user = DatabaseAuth.getCurrentUser();
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
        TextView location = findViewById(R.id.taskLocation);
        repeatCon = findViewById(R.id.repeatCondition);


        taskTitle.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());
        dueDate.setText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()));
        repeatCon.setText("ENTER REPEAT STATUS HERE");

        priority.setText(Integer.toString(task.getPriority()));
        int color = R.color.colorPriority + task.getPriority();
        priorityColor.setBackgroundResource(color);
    }

    /**
     * Processes the repeat condition into an readable string for user
     *
     * @param repeat
     * @return
     */
    public String processRepeatCondition(String repeat) {
        return "This is a placeholder method for displaying repeat condition";
    }

    public void onEdit(View v) {
        Intent editTask = new Intent(this, CreateTaskActivity.class);
        editTask.putExtra(getString(R.string.editSetting_extra), true);
        editTask.putExtra(getString(R.string.task_extra), task);
        startActivity(editTask);
    }


}