package com.CSCI3130.gardenapp.util;

import java.text.SimpleDateFormat;

public class DateFormatUtils {
    public static String getDateFormatted(long timestamp) {
        return new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
    }
}
