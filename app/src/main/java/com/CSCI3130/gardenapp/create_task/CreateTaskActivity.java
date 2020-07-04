package com.CSCI3130.gardenapp.create_task;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.WeatherCondition;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * This is the Create task screen where the user can create a task and upload to the database
 * @author Liam Hebert
 */
public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int current_priority;
    TaskDatabase db;

    Spinner weatherSpinner;
    String[] weatherConditions = { "None", "Rain", "Dry", "Cold", "Hot", "Windy" };
    WeatherCondition currWeatherCondition;

    /**
     * Constructing the activity
     * @param savedInstanceState The default Android activity config varible to load the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        db = new TaskDatabase();
        current_priority = -1;

        //set up weather spinner
        weatherSpinner = (Spinner) findViewById(R.id.weatherSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weatherConditions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(adapter);
        weatherSpinner.setOnItemSelectedListener(this);

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
        TextView errorText = (TextView) findViewById(R.id.textErrorText);
        errorText.setText("");
    }

    private void greyUnselectedButtons(int selected) {
        Button priority1 = (Button) findViewById(R.id.buttonPriority1);
        priority1.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority2 = (Button) findViewById(R.id.buttonPriority2);
        priority2.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority3 = (Button) findViewById(R.id.buttonPriority3);
        priority3.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority4 = (Button) findViewById(R.id.buttonPriority4);
        priority4.setBackgroundColor(getColor(R.color.colorUnselected));
        Button priority5 = (Button) findViewById(R.id.buttonPriority5);
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
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        EditText editLocation = (EditText) findViewById(R.id.editLocation);
        String title = editTitle.getText().toString();
        String description = editDescription.getText().toString();
        String location = editLocation.getText().toString();
        ArrayList<CreateTaskError> errors = verifyTask(
                title,
                description,
                current_priority,
                location);
        if (errors.size() == 0) {
            Snackbar.make(view, "Success!", Snackbar.LENGTH_SHORT).show();
            // package into a task object
            boolean result = uploadTask(title, description, current_priority, "", location, currWeatherCondition);
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
                    TextView errorText = (TextView) findViewById(R.id.textErrorText);
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
     * @param weatherTrigger The weather trigger of the task
     * @return boolean value denoting if the write was successful
     */
    protected boolean uploadTask(String title, String description, int priority, String user, String location) {
        Task task = new Task(title, description, priority, user, location, System.currentTimeMillis());
    }
    protected boolean uploadTask(String title, String description, int priority, String user, String location, WeatherCondition weatherTrigger){
        Task task = new Task(title, description, priority, user, location, System.currentTimeMillis());
        task.setWeatherTrigger(weatherTrigger);
        return db.uploadTask(task);
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

    /**
     * Method handles selection of items from weather condition spinner
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String condition = weatherConditions[position];
        switch(condition){
            case "None":
                currWeatherCondition = WeatherCondition.NONE;
                break;
            case "Rain":
                currWeatherCondition = WeatherCondition.RAIN;
                break;
            case "Dry":
                currWeatherCondition = WeatherCondition.DRY;
                break;
            case "Cold":
                currWeatherCondition = WeatherCondition.COLD;
                break;
            case "Hot":
                currWeatherCondition = WeatherCondition.HOT;
                break;
            case "Windy":
                currWeatherCondition = WeatherCondition.WINDY;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
