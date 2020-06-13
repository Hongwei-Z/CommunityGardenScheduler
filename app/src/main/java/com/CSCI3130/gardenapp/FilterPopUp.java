package com.CSCI3130.gardenapp;
import android.app.Activity;
import android.os.Bundle;

/**
 * This class is the filter popup window.
 * @author Hongwei Zhang (B00780843)
 */
public class FilterPopUp extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
    }
}
