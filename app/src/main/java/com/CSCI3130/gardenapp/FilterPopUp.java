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
    //ArrayList dateBetween used to save two dates which user selected
    ArrayList<Integer> dateBetween;
    int priority = 0;

    protected void onCreate(Bundle savedInstanceState) {
        dateBetween = new ArrayList<>(2);

        //to avoid null pointer exception
        dateBetween.add(1);
        dateBetween.add(2);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
    }

    //clearMethod, click the Clear button, perform the clear function
    public void clearMethod(View view){
        Button startDate = findViewById(R.id.startDateButton);
        Button endDate = findViewById(R.id.endDateButton);
        Button apply = findViewById(R.id.applyButton);
        Button p1 = findViewById(R.id.priority1Button);
        Button p2 = findViewById(R.id.priority2Button);
        Button p3 = findViewById(R.id.priority3Button);
        Button p4 = findViewById(R.id.priority4Button);
        Button p5 = findViewById(R.id.priority5Button);
        startDate.setText("Start Date");
        endDate.setText("End Date");
        apply.setText("APPLY");
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

    /***
     * applyMethod, click the APPLY button, save selected date and priority
     * Haven't connected to the database,
     * the button is temporarily displayed strings to indicate that the button is functional
     */
    public void applyMethod(View view){
        Button apply = findViewById(R.id.applyButton);
        apply.setText(dateBetween.get(0)+", "+dateBetween.get(1)+", "+priority);
    }

    //selectCalendar, used to call the DatePicker, select dates
    public void selectCalendar(final View view){
        final Button startDate = findViewById(R.id.startDateButton);
        final Button endDate = findViewById(R.id.endDateButton);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(FilterPopUp.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                switch (view.getId()){
                    case R.id.startDateButton:
                        startDate.setText(year+", "+(month+1)+", "+day);
                        dateBetween.set(0, year*10000+(month+1)*100+day);
                        break;
                    case R.id.endDateButton:
                        endDate.setText(year+", "+(month+1)+", "+day);
                        dateBetween.set(1, year*10000+(month+1)*100+day);
                        break;
                }
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
        Button p1 = findViewById(R.id.priority1Button);
        Button p2 = findViewById(R.id.priority2Button);
        Button p3 = findViewById(R.id.priority3Button);
        Button p4 = findViewById(R.id.priority4Button);
        Button p5 = findViewById(R.id.priority5Button);
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