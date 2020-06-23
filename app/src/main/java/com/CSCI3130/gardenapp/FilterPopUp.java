package com.CSCI3130.gardenapp;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is the filter popup window.
 * @author Hongwei Zhang, Liam Hebert, Elizabeth Eddy
 */
public class FilterPopUp extends Activity {
    //ArrayList dateBetween used to save two dates which user selected.
    ArrayList<Integer> dateBetween;
    int priority = 0;

    protected void onCreate(Bundle savedInstanceState) {
        dateBetween = new ArrayList<>(2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        /**
         * @Button selectStartDate is the "Start Date" button on filter window
         * @Button selectEndDate is the "End Date" button on filter window
         */
        final Button selectStartDate = findViewById(R.id.startDateButton);
        final Button selectEndDate = findViewById(R.id.endDateButton);
        //TODO - Ditto from the below
        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCalendar(selectStartDate);
            }
        });
        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCalendar(selectEndDate);
            }
        });

        /***
         * @Button applyButton is the "Apply" button on filter window
         */
        final Button applyButton = findViewById(R.id.applyButton);
        //TODO - Check for null values. This should also be in an OnClick event function, not in the create function
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyButton.setText(dateBetween.get(0)+", "+dateBetween.get(1)+", "+priority);
            }
        });

        //Priority buttons
        final Button p1 = (Button) findViewById(R.id.priority1Button);
        final Button p2 = (Button) findViewById(R.id.priority2Button);
        final Button p3 = (Button) findViewById(R.id.priority3Button);
        final Button p4 = (Button) findViewById(R.id.priority4Button);
        final Button p5 = (Button) findViewById(R.id.priority5Button);

        /***
         * @Button clearButton is the "Clear" button on filter window
         */
        Button clearButton = findViewById(R.id.clearButton);
        //TODO - Ditto from the top
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStartDate.setText("Start Date");
                selectEndDate.setText("End Date");
                applyButton.setText("APPLY");
                dateBetween.clear();
                p1.setScaleX(1);
                p1.setScaleY(1);
                p2.setScaleX(1);
                p2.setScaleY(1);
                p3.setScaleX(1);
                p3.setScaleY(1);
                p4.setScaleX(1);
                p4.setScaleY(1);
                p5.setScaleX(1);
                p5.setScaleY(1);
            }
        });
    }

    /**
     * selectCalendar method, open the date picker
     * @param b is a select date button
     */
    public void selectCalendar(final Button b){
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(FilterPopUp.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                b.setText(year+", "+(month+1)+", "+day);
                dateBetween.add(year*10000+(month+1)*100+day);
            }
        },year,month+1,day).show();
    }

    /***
     * prioritySelect method, used to select priority button,
     * code copied from @Liam's method onPriorityCheck
     */
    public void prioritySelect(View view) {
        int p = view.getId();
        switch (p) {
            case R.id.priority1Button:
                priority = 1;
                break;
            case R.id.priority2Button:
                priority = 2;
                break;
            case R.id.priority3Button:
                priority = 3;
                break;
            case R.id.priority4Button:
                priority = 4;
                break;
            case R.id.priority5Button:
                priority = 5;
                break;
        }
        highlightButton(priority);
    }

    /***
     * Make the selected button 1.2 times larger
     * @param priority is the priority number
     */
    public void highlightButton(int priority){
        Button p1 = (Button) findViewById(R.id.priority1Button);
        Button p2 = (Button) findViewById(R.id.priority2Button);
        Button p3 = (Button) findViewById(R.id.priority3Button);
        Button p4 = (Button) findViewById(R.id.priority4Button);
        Button p5 = (Button) findViewById(R.id.priority5Button);
        switch (priority){
            case 1:
                p1.setScaleX((float)1.2);
                p1.setScaleY((float)1.2);
                break;
            case 2:
                p2.setScaleX((float)1.2);
                p2.setScaleY((float)1.2);
                break;
            case 3:
                p3.setScaleX((float)1.2);
                p3.setScaleY((float)1.2);
                break;
            case 4:
                p4.setScaleX((float)1.2);
                p4.setScaleY((float)1.2);
                break;
            case 5:
                p5.setScaleX((float)1.2);
                p5.setScaleY((float)1.2);
                break;
        }
    }
}