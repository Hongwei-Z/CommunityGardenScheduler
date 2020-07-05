package com.CSCI3130.gardenapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;

import org.w3c.dom.Text;

import java.text.DateFormat;


public class TaskDetailInfo extends AppCompatActivity {

    private Button completeButton;
    private Button backlist;
    private TextView textPriority;
    private TextView description;
    private TextView dueDate;
    private TextView taskTitle;
    private TextView location;
    private TextView repeatCon;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);

        backlist = (Button)findViewById(R.id.backtolistbutton);
        backlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtotasklist();
            }
        });

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

        textPriority = (TextView) findViewById(R.id.Taskid);
        description = (TextView) findViewById(R.id.taskDescription);
        dueDate = (TextView) findViewById(R.id.Duedate);
        location = (TextView) findViewById(R.id.location);
        repeatCon = (TextView) findViewById(R.id.repeatCondition);


        taskTitle.setText(task.getName());
        description.setText(task.getDescription());
        location.setText(task.getLocation());
        dueDate.setText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()));
        repeatCon.setText("ENTER REPEAT STATUS HERE");

    }

    public void backtotasklist(){
        Intent intent = new Intent(this, TaskViewList.class);
        startActivity(intent);
    }

    /**
     * Processes the repeat condition into an readable string for user
     * @param repeat
     * @return
     */
    public String processRepeatCondition(String repeat) {
        return "h";
    }
}