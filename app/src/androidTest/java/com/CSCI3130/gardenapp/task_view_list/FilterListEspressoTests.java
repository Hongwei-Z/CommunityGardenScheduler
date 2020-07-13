package com.CSCI3130.gardenapp.task_view_list;

import android.content.Intent;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FilterListEspressoTests {

    private final long currentDate = System.currentTimeMillis();

    @Rule
    public IntentsTestRule<TaskViewList> activityScenarioRule = new IntentsTestRule<>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    @Before
    public void setUp(){
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.ALL_TASKS;
        intent.putExtra("activeTaskListContext", activeTaskListContext);
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, activeTaskListContext));

        //for the sake of testing, the current weather condition is "dry"
        CurrentWeather.windSpeed = 0.5;
        CurrentWeather.humidity = 40;
        CurrentWeather.temperature = 25;
        CurrentWeather.description = "there's no water falling from the sky and it's sunny";
        CurrentWeather.city = "Halifax";
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    /**
     * Test dates:
     * 01-02-2020: 1580562001000
     * 08-08-2020: 1596891601000
     * 25-12-2020: 1607778001000
     */

    //test the display of tasks within a certain range being present
    @Test
    public void test_display_dates_withinRange(){
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.startDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 7));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.endDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 9));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.applyButton)).perform(click());

        try {
            testDB = new TaskTestDatabase();
            activity = activityScenarioRule.getActivity();
            activity.db = testDB;
            testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, ActiveTaskListContext.ALL_TASKS,
                    activityScenarioRule.getActivity().getSortCategory(),
                    activityScenarioRule.getActivity().getSortOrder(),
                    activityScenarioRule.getActivity().getPriorityFilter(),
                    activityScenarioRule.getActivity().getStartDate(),
                    activityScenarioRule.getActivity().getEndDate()));

            Task task1 = new Task("Task in range 1/3", "This task is within the date range", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Task in range 2/3", "This task is within the date range", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Task in range 3/3", "This task is within teh date range", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task4 = new Task("Task out of range 1/1", "This task is not within the date range", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task1);
            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task4);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Task in range 1/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Task in range 2/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("Task in range 3/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(3)).check(doesNotExist());

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //test the display of only one priority being present
    @Test
    public void test_display_priorities_filter(){
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.filterPriorityButton3)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());

        try {
            testDB = new TaskTestDatabase();
            activity = activityScenarioRule.getActivity();
            activity.db = testDB;
            testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, ActiveTaskListContext.ALL_TASKS,
                    activityScenarioRule.getActivity().getSortCategory(),
                    activityScenarioRule.getActivity().getSortOrder(),
                    activityScenarioRule.getActivity().getPriorityFilter(),
                    activityScenarioRule.getActivity().getStartDate(),
                    activityScenarioRule.getActivity().getEndDate()));

            Task task1 = new Task("Task in range 1/3", "This task has the correct priority", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Task in range 2/3", "This task has the correct priority", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Task in range 3/3", "This task has the correct priority", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task4 = new Task("Task out of range 1/2", "This task does not have the correct priority", 5, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task5 = new Task("Task out of range 2/2", "This task does not have the correct priority", 1, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task1);
            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task4);
            testDB.uploadTask(task5);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Task in range 1/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Task in range 2/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("Task in range 3/3"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(3)).check(doesNotExist());

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //test the display of tasks within a certain date range with a specific priority
    @Test
    public void test_display_dates_and_priority(){
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.startDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 7));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.endDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 9));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.filterPriorityButton3)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());

        try {
            testDB = new TaskTestDatabase();
            activity = activityScenarioRule.getActivity();
            activity.db = testDB;
            testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, ActiveTaskListContext.ALL_TASKS,
                    activityScenarioRule.getActivity().getSortCategory(),
                    activityScenarioRule.getActivity().getSortOrder(),
                    activityScenarioRule.getActivity().getPriorityFilter(),
                    activityScenarioRule.getActivity().getStartDate(),
                    activityScenarioRule.getActivity().getEndDate()));

            Task task1 = new Task("Task out of range 1/3", "This task has the correct priority", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Task in range 1/2", "This task has the correct priority", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Task in range 2/2", "This task has the correct priority", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task4 = new Task("Task out of range 2/3", "This task does not have the correct priority", 5, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task5 = new Task("Task out of range 2/3", "This task does not have the correct priority", 1, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task1);
            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task4);
            testDB.uploadTask(task5);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Task in range 1/2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Task in range 2/2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2)).check(doesNotExist());

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //test the display of tasks within a certain date range, with a specific priority, sorted by some category
    @Test
    public void test_display_dates_priority_sort(){
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.startDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 7));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.endDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 9));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.filterPriorityButton3)).perform(click());
        onView(withId(R.id.sortAZBtn)).perform(click());
        onView(withId(R.id.sortDescendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());

        try {
            testDB = new TaskTestDatabase();
            activity = activityScenarioRule.getActivity();
            activity.db = testDB;
            testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, ActiveTaskListContext.ALL_TASKS,
                    activityScenarioRule.getActivity().getSortCategory(),
                    activityScenarioRule.getActivity().getSortOrder(),
                    activityScenarioRule.getActivity().getPriorityFilter(),
                    activityScenarioRule.getActivity().getStartDate(),
                    activityScenarioRule.getActivity().getEndDate()));

            Task task1 = new Task("Task out of range 1/3", "This task has the correct priority", 3, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("A Task in range 1/2", "This task has the correct priority", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Z Task in range 2/2", "This task has the correct priority", 3, "Arjav", "Location", 1596891601000l, TaskRepeatCondition.REPEAT_NONE);
            Task task4 = new Task("Task out of range 2/3", "This task does not have the correct priority", 5, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);
            Task task5 = new Task("Task out of range 2/3", "This task does not have the correct priority", 1, "Arjav", "Location", 1580562001000l, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task1);
            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task4);
            testDB.uploadTask(task5);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Z Task in range 2/2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("A Task in range 1/2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2)).check(doesNotExist());

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
