package com.CSCI3130.gardenapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TaskDetailInfo extends AppCompatActivity {

    private Button completeButton;
    private Button backlist;
    private TextView textName;
    private TextView textDateSatrt;
    private TextView textPriority;
    private TextView description;
    private TextView taskname;
    private TextView textDateDue;
    private TextView Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);

        Task ttask = (Task) getIntent().getSerializableExtra("Iname");

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
                String toast =  getString(R.string.CompleteMessage);
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            }
        });

        long milliSecond = ttask.getDateDue();
        Date date = new Date();
        date.setTime(milliSecond);

        long milliSecond1 = ttask.getDateCompleted();
        Date date1 = new Date();
        date.setTime(milliSecond1);


        //passing due date
        textDateDue = findViewById(R.id.Duedate);
        textDateDue.setText("Due Time: "+new SimpleDateFormat().format(date1));
        //passing priority level
        textPriority = findViewById(R.id.Taskid);
        textPriority.setText(ttask.getId()+"");
        //passing task description

        description = findViewById(R.id.TaskDescription);
        description.setText(ttask.getDescription());
        //passing task name
        taskname = findViewById(R.id.TaskNum);
        taskname.setText(ttask.getName());
        //passing task start time
        textDateSatrt = findViewById(R.id.submitDate);
        textDateSatrt.setText("Start Time: "+new SimpleDateFormat().format(date));
        //passing task location
        Location = findViewById(R.id.location);
        Location.setText("Location: "+ttask.getLocation());
        //passing auth name
        textName = findViewById(R.id.submitAuth);
        textName.setText("Assignee: "+ttask.getUser());

    }

    public void backtotasklist(){
        Intent intent = new Intent(this, TaskRegisterDummyPage.class);
        startActivity(intent);
    }
}