package com.CSCI3130.gardenapp;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.DatabaseTaskTestWriter;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MyTaskListEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public DatabaseTaskTestWriter testDB;
    private TaskViewList activity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("setting", "myTasks");
        activityScenarioRule.launchActivity(intent);
        testDB = new DatabaseTaskTestWriter();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        testDB.getDb().addValueEventListener(testDB.getTaskData(activity.recyclerView));
    }

    @After
    public void tearDown() {
        DatabaseTaskTestWriter db = (DatabaseTaskTestWriter) activity.db;
        db.clearDatabase();
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void toolbarHasExpectedText() {
        onView(withId(R.id.page_name)).check(matches(withText("My Tasks")));
    }

    @Test
    public void recyclerViewItemContainsExpectedText() {
        Task task = new Task("My Task", "This is a Test", 2, FirebaseAuth.getInstance().getUid(), "Location", "June 14th, 2020");
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("My Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("June 14th, 2020"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withId(R.id.task_user_profile))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).perform(click());
            Espresso.pressBack();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
