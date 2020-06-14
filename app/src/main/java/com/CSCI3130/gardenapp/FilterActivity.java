package com.CSCI3130.gardenapp;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
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

    public void PopUp(View view) {
        myDialog.setContentView(R.layout.filter);
        myDialog.show();

        /***
         * @Button selectStartDate is the "Start Date" button on filter window
         * @Button selectEndDate is the "End Date" button on filter window
         */
        final Button selectStartDate = myDialog.findViewById(R.id.startDateButton);
        final Button selectEndDate = myDialog.findViewById(R.id.endDateButton);
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
        final Button applyButton = myDialog.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        /***
         * @Button clearButton is the "Clear" button on filter window
         */
        Button clearButton = myDialog.findViewById(R.id.clearButton);
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

    //ArrayList dateBetween used to save two dates which user selected.
    ArrayList<Integer> dateBetween = new ArrayList<>(2);

    /***
     * selectCalendar method, open the date picker
     * @param b is a select date button
     */
    public void selectCalendar(final Button b){
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                b.setText(year+", "+(month+1)+", "+day);
                dateBetween.add(year*10000+(month+1)*100+day);
            }
        },year,month+1,day).show();
    }
}
