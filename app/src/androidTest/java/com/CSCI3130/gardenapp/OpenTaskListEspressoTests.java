package com.CSCI3130.gardenapp;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class OpenTaskListEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("setting", "openTasks");
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        testDB.setDbRead("openTasks");
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView));
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
    public void testOpenFilter() {

        Task task = new Task("Not Open Task", "This is a Test", 2, "Some User ID", "Location", "June 14th, 2020");
        task.setOpen(false);
        testDB.uploadTask(task);
        try {
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        task = new Task("Open Task", "This is a Test", 2, "", "Location", "June 14th, 2020");
        task.setOpen(true);
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(doesNotExist());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(matches(hasDescendant(withText("Open Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("June 14th, 2020"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withId(R.id.task_user_profile))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).perform(click());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
            Espresso.pressBack();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
