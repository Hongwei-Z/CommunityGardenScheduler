package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class TaskDetailInfo extends MainActivity {

    private Button completeButton;
    private Button backlist;
    private Button buttonedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_info);

        completeButton = (Button)findViewById(R.id.buttonComplete);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        backlist = (Button)findViewById(R.id.backtolistbutton);
        backlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtotasklist();
            }
        });

        buttonedit = (Button)findViewById(R.id.buttonEdit);
        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditTask();
            }
        });
    }

    public void openDialog(){
        CompleteDialog completeDialog = new CompleteDialog();
        completeDialog.show(getSupportFragmentManager(), "complete dialog");
    }

    public void backtotasklist(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openEditTask(){
        Intent intent = new Intent(this, edittaskdetails.class);
        startActivity(intent);
    }
}