package com.CSCI3130.gardenapp.task_view.filter_sorting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.DateFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class is the filter popup window.
 *
 * @author Hongwei Zhang, Liam Hebert, Elizabeth Eddy
 */
public class FilterPopUp extends Activity {
    //ArrayList dateBetween used to save two dates which user selected
    ArrayList<Long> dateBetween;
    int priority = -1;
    ArrayList<Button> priorityButtons;

    //current sorting options
    SortCategory sortCat = SortCategory.NONE;
    SortOrder sortOrder = SortOrder.NONE;

    protected void onCreate(Bundle savedInstanceState) {
        dateBetween = new ArrayList<>(2);
        priorityButtons = new ArrayList<>();
        priorityButtons.add(findViewById(R.id.filterPriorityButton1));
        priorityButtons.add(findViewById(R.id.filterPriorityButton2));
        priorityButtons.add(findViewById(R.id.filterPriorityButton3));
        priorityButtons.add(findViewById(R.id.filterPriorityButton4));
        priorityButtons.add(findViewById(R.id.filterPriorityButton4));

        //to avoid null pointer exception
        dateBetween.add(0L);
        dateBetween.add(Long.MAX_VALUE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
    }

    //clearMethod, click the Clear button, perform the clear function
    public void clearMethod(View view) {
        Button startDate = findViewById(R.id.startDateButton);
        Button endDate = findViewById(R.id.endDateButton);
        Button apply = findViewById(R.id.applyButton);
        resetScaling();

        startDate.setText("Start Date");
        endDate.setText("End Date");
        apply.setText("APPLY");
        dateBetween.set(0, Long.MIN_VALUE);
        dateBetween.set(1, Long.MAX_VALUE);

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
    public void applyMethod(View view) {
        if (sortCat != SortCategory.NONE && sortOrder == SortOrder.NONE) {
            Toast.makeText(this, "Please select a sorting order or clear.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sortOrder != SortOrder.NONE && sortCat == SortCategory.NONE) {
            Toast.makeText(this, "Please select a sorting category or clear.", Toast.LENGTH_SHORT).show();
            return;
        }
        //pass sort options on to task list activity
        Intent i = new Intent(FilterPopUp.this, TaskViewList.class);
        SortingConfigModel sortingConfigModel = new SortingConfigModel(
                sortCat,
                sortOrder);
        FilterConfigModel filterConfigModel = new FilterConfigModel(
                priority,
                dateBetween.get(0),
                dateBetween.get(1));
        i.putExtra(getString(R.string.sortingConfig), sortingConfigModel);
        i.putExtra(getString(R.string.filteringConfig), filterConfigModel);
        startActivity(i);
    }

    /**
     * Method opens calendar popup
     *
     * @param view - view of button pressed
     */
    public void selectCalendar(final View view) {
        Button startBtn = findViewById(R.id.startDateButton);
        Button endBtn = findViewById(R.id.endDateButton);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dp = new DatePickerDialog(FilterPopUp.this, (datePicker, year1, month1, day1) -> {
            date.set(Calendar.YEAR, year1);
            date.set(Calendar.MONTH, month1);
            date.set(Calendar.DAY_OF_MONTH, day1);

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
     *
     * @param condition - true = enabled, false = disabled
     * @param id        - resource id of button
     */
    public void setButtonConditionFromId(boolean condition, int id) {
        Button btn = findViewById(id);
        if (condition)
            btn.setBackgroundResource(R.color.positiveButton);
        else
            btn.setBackgroundResource(R.color.negativeButton);
        btn.setEnabled(condition);
    }


    /**
     * Method sets current sort category and changes UI button conditions accordingly
     *
     * @param view - view param of button which has been clicked
     */
    public void sortCategorySelect(View view) {
        int id = view.getId();
        switch (id) {
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
     *
     * @param view - view param of button which has been clicked
     */
    public void sortOrderSelect(View view) {
        int id = view.getId();
        switch (id) {
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

    private void highlightButton(int priority) {
        resetScaling();
        Button target = priorityButtons.get(priority);
        target.setScaleY((float) 1.2);
        target.setScaleX((float) 1.2);
    }

    private void resetScaling() {
        for (Button button : priorityButtons) {
            button.setScaleX(1);
            button.setScaleY(1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, TaskViewList.class);
        startActivity(i);
    }
}