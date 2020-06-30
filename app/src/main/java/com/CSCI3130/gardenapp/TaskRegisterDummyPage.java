package com.CSCI3130.gardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;

public class TaskRegisterDummyPage extends AppCompatActivity {

    Button registerButton;
    Task task;
    User user;
    int pos;
    TaskDatabase db = new TaskDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hardcoded for now
        db.getDbWrite().child("Task1").child("user").setValue("Beth");
        setContentView(R.layout.activity_task_register_dummy_page);
        task = (Task) getIntent().getSerializableExtra("t");
        user = (User) getIntent().getSerializableExtra("u");
        pos = getIntent().getIntExtra("p", 1);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {

            task.setUser(user.getUsername());
            Intent returnIntent = new Intent();
            returnIntent.putExtra("p", pos);
            returnIntent.putExtra("t", task);
            finish();
        });


    }
}
