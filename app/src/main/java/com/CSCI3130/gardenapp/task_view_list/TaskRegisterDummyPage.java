package com.CSCI3130.gardenapp.task_view_list;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.User;
import com.CSCI3130.gardenapp.util.db.DatabaseAuth;
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
        setContentView(R.layout.activity_task_register_dummy_page);
        task = (Task) getIntent().getSerializableExtra("t");
        TextView titleText = findViewById(R.id.tempTaskTitle);
        titleText.setText(task.getName());
        pos = getIntent().getIntExtra("p", 1);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> {
            User user = DatabaseAuth.getCurrentUser();
            task.setUser(user.getUsername());
            db.updateTask(task);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("p", pos);
            returnIntent.putExtra("t", task);
            finish();
        });
    }

    public void onEdit(View v) {
        Intent editTask = new Intent(this, CreateTaskActivity.class);
        editTask.putExtra("edit", true);
        editTask.putExtra("t", task);
        startActivity(editTask);
    }


}
