package com.CSCI3130.gardenapp.create_task;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the Create task screen where the user can create a task and upload to the database
 * @author Liam Hebert
 */
public class CreateTaskActivity extends AppCompatActivity {
    private int current_priority;
    private boolean edit;
    TaskDatabase db;

    /**
     * Constructing the activity
     * @param savedInstanceState The default Android activity config variable to load the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        edit = getIntent().getBooleanExtra("edit", false);
        db = new TaskDatabase();
        current_priority = -1;
        if (edit) { // configures the UI to EDIT mode
            loadEditConfiguration((Task) Objects.requireNonNull(getIntent().getSerializableExtra("t")));
        }
        db = new TaskDatabase();
    }

    private void loadEditConfiguration(Task t) {
        TextView activityName = findViewById(R.id.textTitle);
        activityName.setText(getText(R.string.textEditTitle));

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editLocation = findViewById(R.id.editLocation);
        Button buttonConfirmAdd = findViewById(R.id.buttonConfirmAdd);
        buttonConfirmAdd.setText(getText(R.string.confirm_edit_task));

        greyUnselectedButtons(t.getPriority());
        editTitle.setText(t.getName());
        editDescription.setText(t.getDescription());
        editLocation.setText(t.getLocation());
        current_priority = t.getPriority();
    }

    /**
     * Saved the current selected parameter button when clicked, gray out the other buttons
     * @param view contains the id of what pressed the button
     */
    public void onPriorityCheck(View view) {
        clearPriorityError();
        int id = view.getId();
        switch (id) {
            case R.id.buttonPriority1:
                current_priority = 1;
                break;
            case R.id.buttonPriority2:
                current_priority = 2;
                break;
            case R.id.buttonPriority3:
                current_priority = 3;
                break;
            case R.id.buttonPriority4:
                current_priority = 4;
                break;
            case R.id.buttonPriority5:
                current_priority = 5;
                break;
        }
        greyUnselectedButtons(current_priority);
    }

    private void clearPriorityError() {
        TextView errorText = findViewById(R.id.textErrorText);
        errorText.setText("");
    }

    private void greyUnselectedButtons(int selected) {
        Button priority1 = findViewById(R.id.buttonPriority1);
        priority1.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority2 = findViewById(R.id.buttonPriority2);
        priority2.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority3 = findViewById(R.id.buttonPriority3);
        priority3.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority4 = findViewById(R.id.buttonPriority4);
        priority4.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority5 = findViewById(R.id.buttonPriority5);
        priority5.setBackgroundColor(getColor(R.color.colorUnselected));
        switch (selected){
            case 1:
                priority1.setBackgroundColor(getColor(R.color.colorPriority1));
                break;
            case 2:
                priority2.setBackgroundColor(getColor(R.color.colorPriority2));
                break;
            case 3:
                priority3.setBackgroundColor(getColor(R.color.colorPriority3));
                break;
            case 4:
                priority4.setBackgroundColor(getColor(R.color.colorPriority4));
                break;
            case 5:
                priority5.setBackgroundColor(getColor(R.color.colorPriority5));
                break;
        }


    }

    /**
     * When the confirm button is pushed, this gathers all the field information and verifies it
     * @param view Required for android
     */
    public void onConfirm(View view) {
        clearPriorityError();
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editLocation = findViewById(R.id.editLocation);
        String title = editTitle.getText().toString();
        String description = editDescription.getText().toString();
        String location = editLocation.getText().toString();
        ArrayList<CreateTaskError> errors = verifyTask(
                title,
                description,
                current_priority,
                location);
        if (errors.size() == 0) {

            // package into a task object
            boolean result = uploadTask(title, description, current_priority, "", location);
            Intent i = new Intent(this, TaskViewList.class);
            i.putExtra("result", true);
            startActivity(i);
            return;
        }
        for (CreateTaskError error: errors) {
            switch (error) {
                case MISSING_TITLE:
                    editTitle.setError("Missing Title");
                    break;
                case MISSING_DESCRIPTION:
                    editDescription.setError("Missing Description");
                    break;
                case MISSING_LOCATION:
                    editLocation.setError("Missing Location");
                    break;
                case MISSING_PRIORITY:
                    TextView errorText = findViewById(R.id.textErrorText);
                    errorText.setText("Missing Priority");
                    break;
            }
        }
    }

    /**
     * Takes all the information about a task and uploads it to the database.
     * @param title The name of the task
     * @param description The description of the task
     * @param priority The priority value associated with the task
     * @param user The user of which the task is assigned to
     * @param location The location where the task should be performed
     * @return boolean value denoting if the write was successful
     */
    protected boolean uploadTask(String title, String description, int priority, String user, String location){
        if (edit) {
            Task task = (Task) getIntent().getSerializableExtra("t");
            task.setName(title);
            task.setDescription(description);
            task.setLocation(location);
            task.setPriority(priority);
            return db.updateTask(task);
        }
        else {
            Task task = new Task(title, description, priority, user, location, System.currentTimeMillis());
            return db.uploadTask(task);
        }
    }

    /**
     * @param title Name of the task
     * @param description Description of the task
     * @param priority Int value of the priority
     * @param location String of the location
     * @return List of errors using Enums in CreateTaskError
     * @see CreateTaskError
     */
    public ArrayList<CreateTaskError> verifyTask(String title, String description, int priority, String location) {
        ArrayList<CreateTaskError> errors = new ArrayList<>();
        if (title.equals("")) {
            errors.add(CreateTaskError.MISSING_TITLE);
        }
        if (description.equals("")){
            errors.add(CreateTaskError.MISSING_DESCRIPTION);
        }
        if (priority < 1 || priority > 5) {
            errors.add(CreateTaskError.MISSING_PRIORITY);
        }
        if (location.equals("")) {
            errors.add(CreateTaskError.MISSING_LOCATION);
        }
        return errors;
    }
}
