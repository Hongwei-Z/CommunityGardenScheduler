package com.CSCI3130.gardenapp.create_task;

import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.DatabaseTaskWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class CreateTaskActivityTest {

    String title = "Test";
    String description = "This is a description!";
    String location = "Carrot Test";
    int priority = 3;
    ArrayList<CreateTaskError> errors;

    DatabaseTaskWriter db;

    CreateTaskActivity activity;


    @Before
    public void setUp() {
        activity = new CreateTaskActivity();
        errors = new ArrayList<>();
        db = mock(DatabaseTaskWriter.class); // prevents real data from uploading
        activity.db = db;
    }

    @After
    public void tearDown() {
        errors.clear();
    }

    @Test
    public void testSuccessfulCreate() {
        activity.uploadTask(title, description, 3, "", location);
        Mockito.verify(db).uploadTask(new Task(title, description, 3, "", location, LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    }

    @Test
    public void testUnsuccessfulCreate() {
        activity.uploadTask(title, "", 3, "", location);
    }

    @Test
    public void testSuccessfulVerify() {
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