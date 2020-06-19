package com.CSCI3130.gardenapp;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is the filter popup window.
 * @author Hongwei Zhang (B00780843)
 */
public class FilterPopUp extends Activity {
    Dialog myDialog;
    //ArrayList dateBetween used to save two dates which user selected.
    ArrayList<Integer> dateBetween;

    protected void onCreate(Bundle savedInstanceState) {
        dateBetween = new ArrayList<Integer>(2);
        //to avoid null pointer exception
        dateBetween.add(1);
        dateBetween.add(2);
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
                applyButton.setText(dateBetween.get(0)+" ~ "+dateBetween.get(1));
            }
        });

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
                applyButton.setText("Apply");
                dateBetween.clear();
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
}
