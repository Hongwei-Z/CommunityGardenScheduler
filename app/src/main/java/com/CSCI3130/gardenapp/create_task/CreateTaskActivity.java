package com.CSCI3130.gardenapp.create_task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This is the Create task screen where the user can create a task and upload to the database
 *
 * @author Liam Hebert
 */
public class CreateTaskActivity extends AppCompatActivity {
    TaskDatabase db;
    private Spinner weatherSpinner, repeatSpinner;
    ArrayAdapter<CharSequence> repeatAdapter, weatherAdapter;
    private int current_priority;
    private long dueDateSelected = System.currentTimeMillis();
    private boolean edit;
    private MaterialButtonToggleGroup conditionsToggle;
    private Button dateConditions;
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
        db = new TaskDatabase();

        MaterialButton priority1 = findViewById(R.id.buttonPriority1);
        priority1.setBackgroundColor(getColor(R.color.colorPriority1));
        current_priority = 1;

        //set up repeat spinner
        repeatSpinner = findViewById(R.id.repeatSpinner);
        repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_choice_array, android.R.layout.simple_spinner_dropdown_item);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(repeatAdapter);

        //set up weather spinner
        weatherSpinner = findViewById(R.id.weatherSpinner);
        weatherAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrentWeather.spinnerWeatherConditions);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weatherAdapter);

        conditionsToggle = findViewById(R.id.taskTypesToggleGroup);
        dateConditions = findViewById(R.id.dueDate);
        dateConditions.setText("Due: " + DateFormatUtils.getDateFormatted(System.currentTimeMillis()));

        date = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(CreateTaskActivity.this, (view, year, month, dayOfMonth) -> {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dueDateSelected = date.getTimeInMillis();
            dateConditions.setText("Due: " + DateFormatUtils.getDateFormatted(dueDateSelected));
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

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

        //determine the task condition
        boolean dateConditionTask = t.getDateDue() != -1;
        boolean repeatConditionTask = !t.getRepeated().equals("repeat-none");
        boolean weatherConditionTask = !t.getWeatherTrigger().equals(WeatherCondition.NONE);
        if (repeatConditionTask) {
            setToggleVisibility("repeat");
            switch (t.getRepeated()) {
                case REPEAT_WEEKLY:
                    repeatSpinner.setSelection(1);
                    break;
                case REPEAT_MONTHLY:
                    repeatSpinner.setSelection(2);
                    break;
                default:
                    repeatSpinner.setSelection(0);
                    break;
            }
        } else if (weatherConditionTask) {
            setToggleVisibility("weather");
            weatherSpinner.setSelection(Arrays.asList(WeatherCondition.values()).indexOf(t.getWeatherTrigger()));
        } else if (dateConditionTask) {
            setToggleVisibility("date");
            Button dueDateButton = findViewById(R.id.dueDate);
            dueDateButton.setText("Due: " + DateFormatUtils.getDateFormatted(t.getDateDue()));
            date.setTimeInMillis(t.getDateDue());
            datePickerDialog.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Display task condition selectors based on toggled button
     *
     * @param view contains the id of what pressed the button
     */
    public void onToggleCondition(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.dateTypeButton:
                setToggleVisibility("date");
                break;
            case R.id.repeatTypeButton:
                setToggleVisibility("repeat");
                break;
            case R.id.weatherTypeButton:
                setToggleVisibility("weather");
                break;
        }
    }

    private void setToggleVisibility(String condition) {
        if (condition.equals("date")) {
            conditionsToggle.check(R.id.dateTypeButton);
            dateConditions.setVisibility(View.VISIBLE);
            repeatSpinner.setVisibility(View.INVISIBLE);
            weatherSpinner.setVisibility(View.INVISIBLE);
        } else if (condition.equals("repeat")) {
            conditionsToggle.check(R.id.repeatTypeButton);
            dateConditions.setVisibility(View.INVISIBLE);
            repeatSpinner.setVisibility(View.VISIBLE);
            weatherSpinner.setVisibility(View.INVISIBLE);
        } else if (condition.equals("weather")) {
            conditionsToggle.check(R.id.weatherTypeButton);
            dateConditions.setVisibility(View.INVISIBLE);
            repeatSpinner.setVisibility(View.INVISIBLE);
            weatherSpinner.setVisibility(View.VISIBLE);
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
        switch (selected) {
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
        //initialize repeat conditions
        WeatherCondition weatherCondition = WeatherCondition.NONE;
        TaskRepeatCondition repeated = TaskRepeatCondition.REPEAT_NONE;
        long dateDue = -1;

        switch (conditionsToggle.getCheckedButtonId()) {
            case R.id.dateTypeButton:
                dateDue = dueDateSelected;
                break;
            case R.id.repeatTypeButton:
                //get repeat condition of task
                switch (repeatSpinner.getSelectedItemPosition()) {
                    case 1:
                        repeated = TaskRepeatCondition.REPEAT_WEEKLY;
                        break;
                    case 2:
                        repeated = TaskRepeatCondition.REPEAT_MONTHLY;
                        break;
                    default:
                        repeated = TaskRepeatCondition.REPEAT_2DAY;
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
    protected void uploadTask(String title, String description, int priority, String location, WeatherCondition weatherTrigger, TaskRepeatCondition repeated, long dateDue) {
        if (edit) {
            Task task = (Task) getIntent().getSerializableExtra("t");
            task.setName(title);
            task.setDescription(description);
            task.setLocation(location);
            task.setWeatherTrigger(weatherTrigger);
            task.setPriority(priority);
            task.setRepeated(repeated);
            switch (repeated) {
                case REPEAT_2DAY:
                    task.setDateDue(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(2));
                    break;
                case REPEAT_WEEKLY:
                    task.setDateDue(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(7));
                    break;
                case REPEAT_MONTHLY:
                    task.setDateDue(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(30));
                    break;
                default:
                    task.setDateDue(dateDue);
            }
            db.updateTask(task);
        } else {
            Task task;
            switch (repeated) {
                case REPEAT_2DAY:
                    task = new Task(title, description, priority, "", location, weatherTrigger, System.currentTimeMillis()+TimeUnit.DAYS.toMillis(2), repeated);
                    break;
                case REPEAT_WEEKLY:
                    task = new Task(title, description, priority, "", location, weatherTrigger, System.currentTimeMillis()+TimeUnit.DAYS.toMillis(7), repeated);
                    break;
                case REPEAT_MONTHLY:
                    task = new Task(title, description, priority, "", location, weatherTrigger, System.currentTimeMillis()+TimeUnit.DAYS.toMillis(30), repeated);
                    break;
                default:
                    task = new Task(title, description, priority, "", location, weatherTrigger, dateDue, repeated);
            }
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
     * @param view contains the id of what pressed the button
     */
    public void openCalendar(View view) {
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
