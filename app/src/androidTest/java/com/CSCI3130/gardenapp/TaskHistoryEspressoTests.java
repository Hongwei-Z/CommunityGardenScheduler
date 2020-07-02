package com.CSCI3130.gardenapp;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class TaskHistoryEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("setting", "taskHistory");
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        testDB.setDbRead("taskHistory");
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
//        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView));
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void toolbarHasExpectedText() {
        onView(withId(R.id.page_name)).check(matches(withText("Task History")));
    }

    @Test
    public void taskHistoryContainsExpectedTasks() {
        Task task = new Task("Task1", "This is a Test", 2, "", "Location", System.currentTimeMillis());
        long timePast = System.currentTimeMillis() - 1000;
        long timeFurtherPast = System.currentTimeMillis() - 100000;
        long timeFuture = System.currentTimeMillis() + 100000;
        //completed prior to current time
        task.setDateCompleted(timePast);
        testDB.uploadTask(task);
        //completed prior to task1 (should show up lower in list)
        task.setName("Task2");
        task.setDateCompleted(timeFurtherPast);
        //completed after current time (sanity check that is does not appear in list)
        task.setName("Task3");
        task.setDateCompleted(timeFuture);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(2)).check(doesNotExist());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(matches(hasDescendant(withText("Task1"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Completed: " + new SimpleDateFormat("dd-MM-yyyy").format(timePast)))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).perform(click());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
            Espresso.pressBack();
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(matches(hasDescendant(withText("Task2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Completed: " + new SimpleDateFormat("dd-MM-yyyy").format(timeFurtherPast)))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).perform(click());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(doesNotExist());
            Espresso.pressBack();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void incompleteTasksNotShownInHistory() {
        Task task = new Task("Task1", "This is a Test", 2, "", "Location", System.currentTimeMillis());
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}