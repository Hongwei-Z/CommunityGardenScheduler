package com.CSCI3130.gardenapp;

import com.CSCI3130.gardenapp.CreateTask.CreateTaskActivity;
import com.CSCI3130.gardenapp.CreateTask.CreateTaskError;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

public class CreateTaskActivityTest {
    CreateTaskActivity activity;
    String title = "Test";
    String description = "This is a description!";
    String location = "Carrot Test";
    int priority = 3;
    ArrayList<CreateTaskError> errors;

    @Before
    public void setUp() throws Exception {
        activity = new CreateTaskActivity();
        errors = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        errors.clear();
    }

    @Test
    public void testSuccessfulCreation() {
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                priority,
                location));
    }

    @Test
    public void testMissingTitle(){
        errors.add(CreateTaskError.MISSING_TITLE);
        Assert.assertEquals(errors, activity.verifyTask(
                "",
                description,
                priority,
                location));
    }

    @Test
    public void testMissingDescription(){
        errors.add(CreateTaskError.MISSING_DESCRIPTION);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                "",
                priority,
                location));
    }

    @Test
    public void testMissingPriority() {
        errors.add(CreateTaskError.MISSING_PRIORITY);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                -1,
                location));
    }

    @Test
    public void testMissingLocation() {
        errors.add(CreateTaskError.MISSING_LOCATION);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                priority,
                ""));
    }

    @Test
    public void testMultipleErrors(){
        errors.add(CreateTaskError.MISSING_LOCATION);
        errors.add(CreateTaskError.MISSING_TITLE);
        Assert.assertTrue(activity.verifyTask("", description, priority, "").containsAll(errors));
    }


}