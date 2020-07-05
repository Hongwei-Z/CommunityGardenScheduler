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


public class TaskDetailInfo extends AppCompatActivity {

    private Button completeButton;
    private Button backlist;
    private TextView textName;
    private TextView textDate;
    private TextView textPriority;

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

        textName = findViewById(R.id.submitAuth);
        textDate = findViewById(R.id.submitDate);
        textPriority = findViewById(R.id.Taskid);

    }

    public void backtotasklist(){
        Intent intent = new Intent(this, TaskRegisterDummyPage.class);
        startActivity(intent);
    }
}