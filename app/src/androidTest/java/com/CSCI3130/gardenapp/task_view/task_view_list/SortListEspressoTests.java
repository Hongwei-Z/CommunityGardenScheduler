package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.content.Intent;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class SortListEspressoTests {
    private final long currentDate = System.currentTimeMillis();

    @Rule
    public IntentsTestRule<TaskViewList> activityScenarioRule = new IntentsTestRule<>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.ALL_TASKS;
        intent.putExtra("activeTaskListContext", activeTaskListContext);
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

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

    //tests for sorting tasks based on due date, in ascending order
    @Test
    public void test_sortDueDateAscending() {

        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortDueDateBtn)).perform(click());
        onView(withId(R.id.sortAscendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            Thread.sleep(2000);
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );

            Task task1 = new Task("First task", "This task is due first", 1, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(2), TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Second task", "This task is due second", 3, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(4), TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Third task", "This task is due third", 5, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(8), TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("First task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Second task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("Third task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //tests for sorting tasks based on due date, in descending order
    @Test
    public void test_sortDueDateDescending() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortDueDateBtn)).perform(click());
        onView(withId(R.id.sortDescendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );

            Task task1 = new Task("First task", "This task is due first", 3, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(2), TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Second task", "This task is due second", 3, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(4), TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Third task", "This task is due third", 3, "Arjav", "44.64541, -63.57661", currentDate + TimeUnit.DAYS.toMillis(8), TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Third task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Second task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("First task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //tests for sorting tasks based on priority, in ascending order
    @Test
    public void test_sortPriorityAscending() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortPriorityBtn)).perform(click());
        onView(withId(R.id.sortAscendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );
            Task task1 = new Task("First task", "This task has the first priority", 1, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Second task", "This task has the second priority", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Third task", "This task has the third priority", 5, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("First task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Second task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("Third task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //tests for sorting tasks based on priority, in descending order
    @Test
    public void test_sortPriorityDescending() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortPriorityBtn)).perform(click());
        onView(withId(R.id.sortDescendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );

            Task task1 = new Task("First task", "This task has the first priority", 1, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("Second task", "This task has the second priority", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Third task", "This task has the third priority", 5, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Third task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Second task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("First task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //tests for sorting tasks alphabetically, in ascending order
    @Test
    public void test_sortAZAscending() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortAZBtn)).perform(click());
        onView(withId(R.id.sortAscendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );

            Task task1 = new Task("A task", "First task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("M task", "Second task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Z task", "Third task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("A task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("M task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("Z task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    //tests for sorting tasks alphabetically, in descending order
    @Test
    public void test_sortAZDescending() {
        onView(withId(R.id.filterButton)).perform(click());
        onView(withId(R.id.sortAZBtn)).perform(click());
        onView(withId(R.id.sortDescendingBtn)).perform(click());
        onView(withId(R.id.applyButton)).perform(click());
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;

        try {
            testDB.getDbRead().addValueEventListener(
                    testDB.getTaskData(
                            activity.recyclerView,
                            ActiveTaskListContext.ALL_TASKS,
                            activityScenarioRule.getActivity().getSortingConfigModel(),
                            activityScenarioRule.getActivity().getFilterConfigModel()
                    )
            );

            Task task1 = new Task("A task", "First task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task2 = new Task("M task", "Second task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);
            Task task3 = new Task("Z task", "Third task alphabetically", 3, "Arjav", "44.64541, -63.57661", currentDate, TaskRepeatCondition.REPEAT_NONE);

            testDB.uploadTask(task2);
            testDB.uploadTask(task3);
            testDB.uploadTask(task1);

            Thread.sleep(4000);

            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Z task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("M task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2))
                    .check(matches(hasDescendant(withText("A task"))));

        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

}
