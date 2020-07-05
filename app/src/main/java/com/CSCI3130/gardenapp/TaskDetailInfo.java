package com.CSCI3130.gardenapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;


public class TaskDetailInfo extends AppCompatActivity {

    private Button completeButton;
    private TextView description,
            dueDate,
            taskTitle,
            location,
            repeatCon,
            priority,
            priorityColor;

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);

        completeButton = (Button)findViewById(R.id.buttonComplete);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toast = getString(R.string.CompleteMessage);
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            }
        });

        task = (Task) getIntent().getSerializableExtra("t");

        taskTitle = (TextView) findViewById(R.id.taskTitle);

        priority = (TextView) findViewById(R.id.taskPriority);
        priorityColor = (TextView) findViewById(R.id.taskColor);
        description = (TextView) findViewById(R.id.taskDescription);
        dueDate = (TextView) findViewById(R.id.Duedate);
        location = (TextView) findViewById(R.id.location);
        repeatCon = (TextView) findViewById(R.id.repeatCondition);


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
     * @param repeat
     * @return
     */
    public String processRepeatCondition(String repeat) {
        return "This is a placeholder method for displaying repeat condition";
    }
}