package com.CSCI3130.gardenapp.create_task;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.junit.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.fail;



import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.Is.*;


public class RepeatDropdownUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;
    @Before
    public void setUp(){
        activityScenarioRule.launchActivity(null);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    @Test
    public void testDropdown() {
        //click first item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Do not repeat"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Do not repeat"))));

        //click second item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every 2 days"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every 2 days"))));


        //click third item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every week"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every week"))));

        //click fourth item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every month"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every month"))));




    }
}
