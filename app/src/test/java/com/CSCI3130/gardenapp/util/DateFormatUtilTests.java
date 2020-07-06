package com.CSCI3130.gardenapp.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateFormatUtilTests {
    @Test
    public void dateFormat() {
        long currentDate = 1600000000000L;
        assertEquals(DateFormatUtils.getDateFormatted(currentDate), "13-09-2020");
    }
}
