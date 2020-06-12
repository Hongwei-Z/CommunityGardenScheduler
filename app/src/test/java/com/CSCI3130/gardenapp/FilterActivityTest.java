package com.CSCI3130.gardenapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterActivityTest {
    /***
     * This is the Filter test.
     * Since this popup window has no text input except select a date from calendar,
     * there is nothing to test for now.
     */
    private FilterActivity f;
    @Before
    public void setUp() {
        f = new FilterActivity();
    }

    @After
    public void tearDown() {
        f.dateBetween.clear();
    }

    @Test
    public void dateBetweenTest(){
        assertTrue(f.dateBetween.isEmpty());
    }
}