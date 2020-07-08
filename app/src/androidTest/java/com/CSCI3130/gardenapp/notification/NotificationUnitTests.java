package com.CSCI3130.gardenapp.notification;

import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class NotificationUnitTests {
    Task task;
    NotificationJob job;
    @Before
    public void before() {
        NotificationJob.db = new TaskTestDatabase();
        job = new NotificationJob();

        task = TaskGenerator.generateTask(false);
        CurrentWeather.currentWeatherList.clear();
    }

    @Test
    /*
     * Testing the weather validation function for valid result
     */
    public void testValidWeatherAlert() {
        CurrentWeather.currentWeatherList.add(WeatherCondition.HOT);
        task.setWeatherTrigger(WeatherCondition.HOT);
        Assert.assertTrue(job.checkValidWeatherAlert(task));
    }

    @Test
    /*
     * Testing the weather validation function for invalid result
     */
    public void testInvalidWeatherAlert() {
        CurrentWeather.currentWeatherList.add(WeatherCondition.WINDY);
        task.setWeatherTrigger(WeatherCondition.HOT);
        Assert.assertFalse(job.checkValidWeatherAlert(task));
    }

    @Test
    /*
     * Testing the duedate validation function for valid result
     */
    public void testValidDuedateAlert() {
        Assert.assertTrue(job.checkValidDueDateAlert(task));
    }

    @Test
    /*
     * Testing the duedate validation function for invalid result
     */
    public void testInvalidDuedateAlert() {
        task.setDateDue(1000);
        Assert.assertFalse(job.checkValidDueDateAlert(task));
    }
}
