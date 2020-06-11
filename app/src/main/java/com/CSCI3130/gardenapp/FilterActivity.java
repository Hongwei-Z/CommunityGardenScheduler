package com.CSCI3130.gardenapp;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * This class is used to achieve the function of each button of the filter pop-up window.
 * @author Hongwei Zhang (B00780843)
 */

public class FilterActivity extends AppCompatActivity {
    Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        myDialog = new Dialog(this);
    }

    /**
     * This is the popup window
     * */
    public void PopUp(View view) {
        myDialog.setContentView(R.layout.filter);
        myDialog.show();

        /**
         * Save the two selected dates, used to compare with task's due date
         * */
        final int[] dateBetween = new int[2];

        /**
         * When you click the start date button, you will see the first calendar, you can select a date
         * */
        final Button selectStartDate = myDialog.findViewById(R.id.startDateButton);
        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar startDate = Calendar.getInstance();
                int startYear = startDate.get(Calendar.YEAR);
                int startMonth = startDate.get(Calendar.MONTH);
                int startDay = startDate.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {
                        selectStartDate.setText(startYear+", "+(startMonth+1)+", "+startDay);
                        dateBetween[0] =startYear*10000+(startMonth+1)*100+startDay;
                    }
                },startYear,startMonth+1,startDay).show();

            }
        });

        /**
         * When you click the end date button, you will see the second calendar, you can select a date
         * */
        final Button selectEndDate = myDialog.findViewById(R.id.endDateButton);
        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar endDate = Calendar.getInstance();
                int endYear = endDate.get(Calendar.YEAR);
                int endMonth = endDate.get(Calendar.MONTH);
                int endDay = endDate.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int endYear, int endMonth, int endDay) {
                        selectEndDate.setText(endYear+", "+(endMonth+1)+", "+endDay);
                        dateBetween[1] =endYear*10000+(endMonth+1)*100+endDay;
                    }
                },endYear,endMonth+1,endDay).show();
            }
        });

        /**
         * This is the clear button,
         * I don’t know how to clear the date yet, now it’s just reset the date selection buttons
         * */
        Button clearButton = myDialog.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                selectStartDate.setText("Start Date");
                selectEndDate.setText("End Date");
            }
        });

        /**
         * This is the apply button, I don't have tasks database now, so I can only save the two dates,
         * once the database is finished, I can compare these two dates with tasks due date,
         * to achieve the selection of a specific tasks
         * */
        final Button applyButton = myDialog.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                applyButton.setText(dateBetween[0]+" ~ "+dateBetween[1]);
            }
        });
    }
}
