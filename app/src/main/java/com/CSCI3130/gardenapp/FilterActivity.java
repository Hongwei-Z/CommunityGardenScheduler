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


    }


}
