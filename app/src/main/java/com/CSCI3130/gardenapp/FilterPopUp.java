package com.CSCI3130.gardenapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.DateFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is the filter popup window.
 * @author Hongwei Zhang, Liam Hebert, Elizabeth Eddy
 */
public class FilterPopUp extends Activity {
    //ArrayList dateBetween used to save two dates which user selected
    ArrayList<Long> dateBetween;
    int priority = -1;

    //current sorting options
    SortCategory sortCat = SortCategory.NONE;
    SortOrder sortOrder = SortOrder.NONE;

    protected void onCreate(Bundle savedInstanceState) {
        dateBetween = new ArrayList<>(2);

        //to avoid null pointer exception
        dateBetween.add(0l);
        dateBetween.add(Long.MAX_VALUE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
    }

    //clearMethod, click the Clear button, perform the clear function
    public void clearMethod(View view){
        Button startDate = findViewById(R.id.startDateButton);
        Button endDate = findViewById(R.id.endDateButton);
        Button apply = findViewById(R.id.applyButton);
        Button p1 = findViewById(R.id.filterPriorityButton1);
        Button p2 = findViewById(R.id.filterPriorityButton2);
        Button p3 = findViewById(R.id.filterPriorityButton3);
        Button p4 = findViewById(R.id.filterPriorityButton4);
        Button p5 = findViewById(R.id.filterPriorityButton5);
        startDate.setText("Start Date");
        endDate.setText("End Date");
        apply.setText("APPLY");
        dateBetween.set(0, Long.MIN_VALUE);
        dateBetween.set(1, Long.MAX_VALUE);

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

        //reset sorting
        sortOrder = SortOrder.NONE;
        sortCat = SortCategory.NONE;
        priority = -1;
        setButtonConditionFromId(true, R.id.sortAscendingBtn);
        setButtonConditionFromId(true, R.id.sortDescendingBtn);
        setButtonConditionFromId(true, R.id.sortDueDateBtn);
        setButtonConditionFromId(true, R.id.sortPriorityBtn);
        setButtonConditionFromId(true, R.id.sortAZBtn);
    }

    /***
     * applyMethod, click the APPLY button, save selected date and priority
     * Haven't connected to the database,
     * the button is temporarily displayed strings to indicate that the button is functional
     */
    public void applyMethod(View view){
        if (sortCat != SortCategory.NONE && sortOrder == SortOrder.NONE){
            Toast.makeText(this, "Please select a sorting order or clear.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sortOrder != SortOrder.NONE && sortCat == SortCategory.NONE){
            Toast.makeText(this, "Please select a sorting category or clear.", Toast.LENGTH_SHORT).show();
            return;
        }
        //pass sort options on to task list activity
        Intent i = new Intent(FilterPopUp.this, TaskViewList.class);
        i.putExtra("sort_category", sortCat);
        i.putExtra("sort_order", sortOrder);
        i.putExtra("startDate", dateBetween.get(0));
        i.putExtra("endDate", dateBetween.get(1));
        i.putExtra("priority", priority);
        startActivity(i);
    }

    //selectCalendar, used to call the DatePicker, select dates

    /**
     * Method opens calendar popup
     * @param view - view of button pressed
     */
    public void selectCalendar(final View view){
        Button startBtn = (Button) findViewById(R.id.startDateButton);
        Button endBtn = (Button) findViewById(R.id.endDateButton);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(FilterPopUp.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month);
                date.set(Calendar.DAY_OF_MONTH, day);

                switch (view.getId()) {
                    case R.id.startDateButton:
                        startBtn.setText(DateFormatUtils.getDateFormatted(date.getTimeInMillis()));
                        dateBetween.set(0, date.getTimeInMillis());
                        break;
                    case R.id.endDateButton:
                        endBtn.setText(DateFormatUtils.getDateFormatted(date.getTimeInMillis()));
                        dateBetween.set(1, date.getTimeInMillis());
                        break;
                }
            }
        }, year, month, day);
        switch (view.getId()) {
            case R.id.startDateButton:
                dp.getDatePicker().setMaxDate(dateBetween.get(1));
                break;
            case R.id.endDateButton:
                dp.getDatePicker().setMinDate(dateBetween.get(0));
                break;
        }
        dp.show();

    }

    /***
     * prioritySelect method, used to select priority button,
     * code copied from @Liam's method onPriorityCheck
     */
    public void prioritySelect(View view) {
        int p = view.getId();
        switch (p) {
            case R.id.filterPriorityButton1:
                priority = 1;
                break;
            case R.id.filterPriorityButton2:
                priority = 2;
                break;
            case R.id.filterPriorityButton3:
                priority = 3;
                break;
            case R.id.filterPriorityButton4:
                priority = 4;
                break;
            case R.id.filterPriorityButton5:
                priority = 5;
                break;
        }
        highlightButton(priority);
    }

    /**
     * Method sets whether a button is enabled/disabled based on its resource ID
     * @param condition - true = enabled, false = disabled
     * @param id - resource id of button
     */
    public void setButtonConditionFromId(boolean condition, int id){
        Button btn = (Button) findViewById(id);
        if (condition)
            btn.setBackgroundResource(R.color.positiveButton);
        else
            btn.setBackgroundResource(R.color.negativeButton);
        btn.setEnabled(condition);
    }


    /**
     * Method sets current sort category and changes UI button conditions accordingly
     * @param view - view param of button which has been clicked
     */
    public void sortCategorySelect(View view){
        int id = view.getId();
        switch (id){
            case R.id.sortPriorityBtn:
                sortCat = SortCategory.PRIORITY;
                setButtonConditionFromId(true, R.id.sortDueDateBtn);
                setButtonConditionFromId(false, R.id.sortPriorityBtn);
                setButtonConditionFromId(true, R.id.sortAZBtn);
                break;
            case R.id.sortDueDateBtn:
                sortCat = SortCategory.DUEDATE;
                setButtonConditionFromId(false, R.id.sortDueDateBtn);
                setButtonConditionFromId(true, R.id.sortPriorityBtn);
                setButtonConditionFromId(true, R.id.sortAZBtn);
                break;
            case R.id.sortAZBtn:
                sortCat = SortCategory.AZ;
                setButtonConditionFromId(true, R.id.sortDueDateBtn);
                setButtonConditionFromId(true, R.id.sortPriorityBtn);
                setButtonConditionFromId(false, R.id.sortAZBtn);
                break;
            default:
                break;
        }
    }

    /**
     * Method sets current sort order and changes UI button conditions accordingly
     * @param view - view param of button which has been clicked
     */
    public void sortOrderSelect(View view){
        int id = view.getId();
        switch (id){
            case R.id.sortAscendingBtn:
                sortOrder = SortOrder.ASCENDING;
                setButtonConditionFromId(false, R.id.sortAscendingBtn);
                setButtonConditionFromId(true, R.id.sortDescendingBtn);
                break;
            case R.id.sortDescendingBtn:
                sortOrder = SortOrder.DESCENDING;
                setButtonConditionFromId(true, R.id.sortAscendingBtn);
                setButtonConditionFromId(false, R.id.sortDescendingBtn);
                break;
            default:
                break;
        }
    }

    /***
     * Make the selected button 1.2 times larger
     * @param priority is the priority number
     */
    public void highlightButton(int priority){
        Button p1 = findViewById(R.id.filterPriorityButton1);
        Button p2 = findViewById(R.id.filterPriorityButton2);
        Button p3 = findViewById(R.id.filterPriorityButton3);
        Button p4 = findViewById(R.id.filterPriorityButton4);
        Button p5 = findViewById(R.id.filterPriorityButton5);
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
        switch (priority) {
            case 1:
                p1.setScaleX((float) 1.2);
                p1.setScaleY((float) 1.2);
                break;
            case 2:
                p2.setScaleX((float) 1.2);
                p2.setScaleY((float) 1.2);
                break;
            case 3:
                p3.setScaleX((float) 1.2);
                p3.setScaleY((float) 1.2);
                break;
            case 4:
                p4.setScaleX((float) 1.2);
                p4.setScaleY((float) 1.2);
                break;
            case 5:
                p5.setScaleX((float) 1.2);
                p5.setScaleY((float) 1.2);
                break;
        }
    }
}