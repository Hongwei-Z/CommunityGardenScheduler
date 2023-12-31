package com.CSCI3130.gardenapp.task_actions;

import com.CSCI3130.gardenapp.db.TaskDatabase;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
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
        activity.uploadTask(title, description, 3, location, testWeatherCondition, TaskRepeatCondition.REPEAT_NONE, System.currentTimeMillis());
        Task testTask = new Task(title, description, 3, "", location, System.currentTimeMillis(), TaskRepeatCondition.REPEAT_NONE);
        testTask.setWeatherTrigger(testWeatherCondition);
        Mockito.verify(db).uploadTask(testTask);
    }

    @Test
    public void testUnsuccessfulCreate() {
        activity.uploadTask(title, "", 3, location, testWeatherCondition, TaskRepeatCondition.REPEAT_NONE, System.currentTimeMillis());
    }

    @Test
    public void testSuccessfulVerify() {
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                description,
                location));
    }

    @Test
    public void testMissingTitle() {
        errors.add(CreateTaskError.MISSING_TITLE);
        Assert.assertEquals(errors, activity.verifyTask(
                "",
                description,
                location));
    }

    @Test
    public void testMissingDescription() {
        errors.add(CreateTaskError.MISSING_DESCRIPTION);
        Assert.assertEquals(errors, activity.verifyTask(
                title,
                "",
                location));
    }

    @Test
    public void testMultipleErrors(){
         errors.add(CreateTaskError.MISSING_TITLE);
        errors.add(CreateTaskError.MISSING_DESCRIPTION);
        Assert.assertTrue(activity.verifyTask("", "", location).containsAll(errors));
    }


}