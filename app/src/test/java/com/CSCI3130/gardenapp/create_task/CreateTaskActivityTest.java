package com.CSCI3130.gardenapp.create_task;

import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class CreateTaskActivityTest {

    String title = "Test";
    String description = "This is a description!";
    String location = "Carrot Test";
    WeatherCondition testWeatherCondition = WeatherCondition.NONE;
    int priority = 3;
    ArrayList<CreateTaskError> errors;

    TaskDatabase db;

    CreateTaskActivity activity;


    @Before
    public void setUp() {
        activity = new CreateTaskActivity();
        errors = new ArrayList<>();
        db = mock(TaskDatabase.class); // prevents real data from uploading
        activity.db = db;
    }

    @After
    public void tearDown() {
        errors.clear();
    }

    @Test
    public void testSuccessfulCreate() {
        activity.uploadTask(title, description, 3, location, testWeatherCondition, "repeat-none", System.currentTimeMillis());
        Task testTask = new Task(title, description, 3, "", location, System.currentTimeMillis(), "repeat-none");
        testTask.setWeatherTrigger(testWeatherCondition);
        Mockito.verify(db).uploadTask(testTask);
    }

    @Test
    public void testUnsuccessfulCreate() {
        activity.uploadTask(title, "", 3, location, testWeatherCondition,"repeat-none", System.currentTimeMillis());
    }

    @Test
    public void testSuccessfulVerify() {
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                location));
    }

    @Test
    public void testMissingTitle(){
        errors.add(CreateTaskError.MISSING_TITLE);
        Assert.assertEquals(errors, activity.verifyTask(
                "",
                description,
                location));
    }

    @Test
    public void testMissingDescription(){
        errors.add(CreateTaskError.MISSING_DESCRIPTION);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                "",
                location));
    }

    @Test
    public void testMissingLocation() {
        errors.add(CreateTaskError.MISSING_LOCATION);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                ""));
    }

    @Test
    public void testMultipleErrors(){
        errors.add(CreateTaskError.MISSING_LOCATION);
        errors.add(CreateTaskError.MISSING_TITLE);
        Assert.assertTrue(activity.verifyTask("", description, "").containsAll(errors));
    }


}