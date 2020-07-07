package com.CSCI3130.gardenapp.create_task;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

/**
 * This is the Create task screen where the user can create a task and upload to the database
 *
 * @author Liam Hebert
 */
public class CreateTaskActivity extends AppCompatActivity {
    TaskDatabase db;
    Spinner weatherSpinner;
    ArrayAdapter<CharSequence> adapter;
    private int current_priority;
    private long dueDateSelected = System.currentTimeMillis();
    private boolean edit;
    private Spinner repeatSpinner;
    private MaterialButtonToggleGroup conditionsToggle ;
    private Button dateConditions;
    private Spinner repeatConditions;
    private Spinner weatherConditions;
    private DatePickerDialog datePickerDialog;
    private Calendar date;

    /**
     * Constructing the activity
     *
     * @param savedInstanceState The default Android activity config variable to load the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        edit = getIntent().getBooleanExtra(getString(R.string.editSetting_extra), false);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        ArrayAdapter<CharSequence> repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_choice_array, android.R.layout.simple_spinner_dropdown_item);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatAdapter);
        db = new TaskDatabase();
        //set up weather spinner
        weatherSpinner = findViewById(R.id.weatherSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrentWeather.spinnerWeatherConditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(adapter);
        Button dueDate = findViewById(R.id.dueDate);
        dueDate.setText("Due: " + DateFormatUtils.getDateFormatted(System.currentTimeMillis()));
        MaterialButton priority1 = findViewById(R.id.buttonPriority1);
        priority1.setBackgroundColor(getColor(R.color.colorPriority1));
        current_priority = 1;
        conditionsToggle = findViewById(R.id.taskTypesToggleGroup);
        dateConditions = findViewById(R.id.dueDate);
        repeatConditions = findViewById(R.id.repeatSpinner);
        weatherConditions = findViewById(R.id.weatherSpinner);

        date = Calendar.getInstance();
        Button dueDateButton = findViewById(R.id.dueDate);
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dueDateSelected = date.getTimeInMillis();
                dueDateButton.setText("Due: " + DateFormatUtils.getDateFormatted(dueDateSelected));
            }
        }, year, month, day);

        if (edit) { // configures the UI to EDIT mode
            loadEditConfiguration((Task) Objects.requireNonNull(getIntent().getSerializableExtra(getString(R.string.task_extra))));
        }
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

        boolean repeatedFalse = t.getRepeated().equals("repeat-none");
        boolean weatherFalse = t.getWeatherTrigger().equals(WeatherCondition.NONE);
        boolean dateFalse = t.getDateDue() == -1;

        if (repeatedFalse && weatherFalse){
            conditionsToggle.check(R.id.dateTypeButton);
            dateConditions.setVisibility(View.VISIBLE);
            repeatConditions.setVisibility(View.INVISIBLE);
            weatherConditions.setVisibility(View.INVISIBLE);
            Button dueDateButton = findViewById(R.id.dueDate);
            dueDateButton.setText("Due: " + DateFormatUtils.getDateFormatted(t.getDateDue()));
            date.setTimeInMillis(t.getDateDue());
           datePickerDialog.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        } else if (weatherFalse && dateFalse) {
            conditionsToggle.check(R.id.repeatTypeButton);
            dateConditions.setVisibility(View.INVISIBLE);
            repeatConditions.setVisibility(View.VISIBLE);
            weatherConditions.setVisibility(View.INVISIBLE);
            switch (t.getRepeated()) {
                case "repeat-weekly":
                    repeatSpinner.setSelection(1);
                    break;
                case "repeat-monthly":
                    repeatSpinner.setSelection(2);
                    break;
                default:
                    repeatSpinner.setSelection(0);
                    break;
            }
        } else if (dateFalse && repeatedFalse) {
            conditionsToggle.check(R.id.weatherTypeButton);
            dateConditions.setVisibility(View.INVISIBLE);
            repeatConditions.setVisibility(View.INVISIBLE);
            weatherConditions.setVisibility(View.VISIBLE);
            weatherSpinner.setSelection(Arrays.asList(WeatherCondition.values()).indexOf(t.getWeatherTrigger()));
        }
    }


    public void ontoggleCondition(View view) {
        int id = view.getId();
        Button dateConditions = findViewById(R.id.dueDate);
        Spinner repeatConditions = findViewById(R.id.repeatSpinner);
        Spinner weatherConditions = findViewById(R.id.weatherSpinner);
        switch (id) {
            case R.id.dateTypeButton:
                //reset repeat and weather
                dateConditions.setVisibility(View.VISIBLE);
                repeatConditions.setVisibility(View.INVISIBLE);
                weatherConditions.setVisibility(View.INVISIBLE);
                break;
            case R.id.repeatTypeButton:
                dateConditions.setVisibility(View.INVISIBLE);
                repeatConditions.setVisibility(View.VISIBLE);
                weatherConditions.setVisibility(View.INVISIBLE);
                break;
            case R.id.weatherTypeButton:
                dateConditions.setVisibility(View.INVISIBLE);
                repeatConditions.setVisibility(View.INVISIBLE);
                weatherConditions.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Saved the current selected parameter button when clicked, gray out the other buttons
     *
     * @param view contains the id of what pressed the button
     */
    public void onPriorityCheck(View view) {
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

    private void greyUnselectedButtons(int selected) {
        MaterialButton priority1 = findViewById(R.id.buttonPriority1);
        priority1.setBackgroundColor(Color.TRANSPARENT);
        MaterialButton priority2 = findViewById(R.id.buttonPriority2);
        priority2.setBackgroundColor(Color.TRANSPARENT);
        MaterialButton priority3 = findViewById(R.id.buttonPriority3);
        priority3.setBackgroundColor(Color.TRANSPARENT);
        MaterialButton priority4 = findViewById(R.id.buttonPriority4);
        priority4.setBackgroundColor(Color.TRANSPARENT);
        MaterialButton priority5 = findViewById(R.id.buttonPriority5);
        priority5.setBackgroundColor(Color.TRANSPARENT);
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
     *
     * @param view Required for android
     */
    public void onConfirm(View view) {
        EditText editTitle = findViewById(R.id.editTitle);
        EditText editDescription = findViewById(R.id.editDescription);
        EditText editLocation = findViewById(R.id.editLocation);
        String title = editTitle.getText().toString();
        String description = editDescription.getText().toString();
        String location = editLocation.getText().toString();
        WeatherCondition weatherCondition = WeatherCondition.NONE;
        String repeated = "repeat-none";
        long dateDue = -1;

        switch (conditionsToggle.getCheckedButtonId()) {
            case R.id.dateTypeButton:
                dateDue = dueDateSelected;
                break;
            case R.id.repeatTypeButton:
                //get repeat condition of task
                switch (repeatSpinner.getSelectedItemPosition()) {
                    case 1:
                        repeated = "repeat-weekly";
                        break;
                    case 2:
                        repeated = "repeat-monthly";
                        break;
                    default:
                        repeated = "repeat-2day";
                        break;
                }
                break;
            case R.id.weatherTypeButton:
                weatherCondition = WeatherCondition.values()[weatherSpinner.getSelectedItemPosition()];
                break;

        }

        ArrayList<CreateTaskError> errors = verifyTask(
                title,
                description,
                location);
        if (errors.size() == 0) {

            // package into a task object
            uploadTask(title, description, current_priority, location, weatherCondition, repeated, dateDue);
            Intent i = new Intent(this, TaskViewList.class);
            i.putExtra("result", true);
            startActivity(i);
            return;
        }
        for (CreateTaskError error : errors) {
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
            }
        }
    }

    /**
     * Takes all the information about a task and uploads it to the database.
     *
     * @param title          The name of the task
     * @param description    The description of the task
     * @param priority       The priority value associated with the task
     * @param location       The location where the task should be performed
     * @param weatherTrigger The weather trigger of the task
     * @param repeated       The repeat condition of the task
     * @param dateDue        The date that the task is due
     */
    protected void uploadTask(String title, String description, int priority, String location, WeatherCondition weatherTrigger, String repeated, long dateDue) {
        if (edit) {
            Task task = (Task) getIntent().getSerializableExtra("t");
            task.setName(title);
            task.setDescription(description);
            task.setLocation(location);
            task.setWeatherTrigger(weatherTrigger);
            task.setPriority(priority);
            task.setRepeated(repeated);
            task.setDateDue(dateDue);
            db.updateTask(task);
        } else {
            Task task = new Task(title, description, priority, "", location, weatherTrigger, dateDue, repeated);
            db.uploadTask(task);
        }
    }

    /**
     * @param title       Name of the task
     * @param description Description of the task
     * @param location    String of the location
     * @return List of errors using Enums in CreateTaskError
     * @see CreateTaskError
     */
    public ArrayList<CreateTaskError> verifyTask(String title, String description, String location) {
        ArrayList<CreateTaskError> errors = new ArrayList<>();
        if (title.equals("")) {
            errors.add(CreateTaskError.MISSING_TITLE);
        }
        if (description.equals("")) {
            errors.add(CreateTaskError.MISSING_DESCRIPTION);
        }
        if (location.equals("")) {
            errors.add(CreateTaskError.MISSING_LOCATION);
        }
        return errors;
    }

    /**
     * Used to open the Date Picker Dialog
     *
     * @param view
     */
    public void openCalendar(View view) {
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
