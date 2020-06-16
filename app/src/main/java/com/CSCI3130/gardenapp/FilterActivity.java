package com.CSCI3130.gardenapp;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

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
        final TextView result = myDialog.findViewById(R.id.textView0);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                result.setText(afterFilter());
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

    public String afterFilter(){
        String result="";

        //ArrayList<Task> taskList copied from TaskViewList.class
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Cover tomatoes", "cover if raining", 4, "", "June 4th, 2020"));
        taskList.add(new Task("Water all of the carrots", "water them a lot", 2, "Bob", "June 2nd, 2020"));
        taskList.add(new Task("Plant cucumber transplants", "", 1, "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some dirt", "pile it", 3,  "", "June 20th, 2020"));
        taskList.add(new Task("Water more carrots", "water them a lot", 5, "", "June 30th, 2020"));
        taskList.add(new Task("Plant tomato transplants", "", 1, "Bill", "June 10th, 2020"));
        taskList.add(new Task("Dig some more dirt", "", 3, "Bill", "June 4th, 2020"));
        taskList.add(new Task("Water cucumber", "water them a lot", 3, "", "June 16th, 2020"));
        taskList.add(new Task("Add compost", "", 1, "Bill", "June 10th, 2020"));

        ArrayList<Task> taskListAfter = new ArrayList<>();
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i=0; i<taskList.size(); i++){
            String date = taskList.get(i).getDate();
            String[] spiltDate = date.split("\\s");
            String m = spiltDate[0];
            String d = spiltDate[1];
            String y = spiltDate[2];
            int finalDate = 0;
            if (d.contains("th,")){
                d.replace("th,", "");
            }
            for (int j=0; j<month.length; j++){
                if (month[j].equals(m)){
                    finalDate = j*100;
                }
            }
            finalDate+=Integer.parseInt(y)*10000;
            finalDate+=Integer.parseInt(d);
            if (finalDate <= dateBetween.get(1) && finalDate >= dateBetween.get(0)){
                result+=taskList.get(i).getDescription()+"\n";
            }
        }
        return result;
    }
}
