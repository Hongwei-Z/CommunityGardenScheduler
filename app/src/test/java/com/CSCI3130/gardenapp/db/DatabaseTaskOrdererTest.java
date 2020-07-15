package com.CSCI3130.gardenapp.db;

import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.task_view.filter_sorting.FilterConfigModel;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortCategory;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortOrder;
import com.CSCI3130.gardenapp.task_view.filter_sorting.SortingConfigModel;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseTaskOrdererTest {
    FilterConfigModel filterConfigModel;
    SortingConfigModel sortingConfigModel;

    @Before
    public void setup() {
        filterConfigModel = new FilterConfigModel();
        sortingConfigModel = new SortingConfigModel();
    }

    /**
     * Test dates:
     * 01-02-2020: 1580562001000
     * 08-08-2020: 1599590247976
     */
    @Test
    public void testConstrainedDates() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Task task = TaskGenerator.generateTask(false);
            task.setDateDue(1599590247976L);
            tasks.add(task);
        }
        tasks.get(3).setDateDue(1580562001000L);
        tasks.get(3).setName("THIS SHOULD NOT BE HERE!!!");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 8, 7);
        filterConfigModel.setStartDate(calendar.getTimeInMillis());
        ArrayList<Task> result = DatabaseTaskOrderer.filterSortTasks(
                tasks,
                filterConfigModel,
                sortingConfigModel,
                ActiveTaskListContext.ALL_TASKS);
        Assert.assertNotEquals(result, tasks);
        tasks.remove(3);
        Assert.assertEquals(tasks, result);
    }

    @Test
    public void testConstrainedPriorityValue() {
        ArrayList<Task> tasks = new ArrayList<>();
        int k = 2;
        for (int i = 0; i < 4; i++) {
            Task task = TaskGenerator.generateTask(false);
            task.setName("THIS SHOULD NOT BE HERE!!!");
            task.setPriority(k + i);
            tasks.add(task);
        }
        tasks.get(3).setPriority(1);
        tasks.get(3).setName("This is good!");
        filterConfigModel.setPriorityFilter(1);
        ArrayList<Task> result = DatabaseTaskOrderer.filterSortTasks(
                tasks,
                filterConfigModel,
                sortingConfigModel,
                ActiveTaskListContext.ALL_TASKS);
        Assert.assertNotEquals(result, tasks);
        Task good = tasks.get(3);
        tasks.clear();
        tasks.add(good);
        Assert.assertEquals(tasks, result);
    }

    @Test
    public void testSortedConstrainedPriorityDates() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = TaskGenerator.generateTask(false);
            task.setName("THIS SHOULD NOT BE HERE!!!");
            task.setDateDue(1580562001000L);
            tasks.add(task);
        }
        tasks.get(0).setPriority(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 8, 7);
        tasks.get(3).setDateDue(calendar.getTimeInMillis());
        tasks.get(3).setName("A This is good :)");
        tasks.get(3).setPriority(1);

        tasks.get(2).setDateDue(calendar.getTimeInMillis());
        tasks.get(2).setName("Z This is good :)");
        tasks.get(2).setPriority(1);

        tasks.get(4).setDateDue(calendar.getTimeInMillis());
        tasks.get(4).setPriority(5);
        tasks.get(4).setName("This is NOT good :)");

        calendar.set(2020, 8, 7);
        filterConfigModel.setStartDate(calendar.getTimeInMillis());
        calendar.set(2020, 8, 9);
        filterConfigModel.setEndDate(calendar.getTimeInMillis());
        filterConfigModel.setPriorityFilter(1);
        sortingConfigModel.setSortOrder(SortOrder.ASCENDING);
        sortingConfigModel.setSortCat(SortCategory.AZ);
        ArrayList<Task> result = DatabaseTaskOrderer.filterSortTasks(
                tasks,
                filterConfigModel,
                sortingConfigModel,
                ActiveTaskListContext.ALL_TASKS);

        Assert.assertNotEquals(result, tasks);
        ArrayList<Task> realResults = new ArrayList<>();
        realResults.add(tasks.get(3));
        realResults.add(tasks.get(2));
        Assert.assertEquals(realResults, result);
    }

    @Test
    public void testWeatherOrder() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Task task = TaskGenerator.generateTask(false);
            task.setDateDue(1599590247976L);
            tasks.add(task);
        }
        tasks.get(3).setDateDue(1580562001000L);
        tasks.get(3).setName("THIS SHOULD NOT BE HERE!!!");
        Task weatherTask = TaskGenerator.generateTask(false, WeatherCondition.WINDY);
        CurrentWeather.currentWeatherList.clear();
        CurrentWeather.currentWeatherList.add(WeatherCondition.RAIN);
        Task weatherTask2 = TaskGenerator.generateTask(false, WeatherCondition.RAIN);
        tasks.add(weatherTask2);
        tasks.add(0, weatherTask);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 8, 7);
        filterConfigModel.setStartDate(calendar.getTimeInMillis());


        ArrayList<Task> result = DatabaseTaskOrderer.filterSortTasks(
                tasks,
                filterConfigModel,
                sortingConfigModel,
                ActiveTaskListContext.ALL_TASKS);

        Assert.assertNotEquals(result, tasks);
        tasks.remove(4);
        Assert.assertNotEquals(result, tasks);
        tasks.remove(weatherTask);
        tasks.remove(weatherTask2);
        tasks.add(weatherTask);
        tasks.add(0, weatherTask2);
        Assert.assertEquals(tasks, result);
    }
}
