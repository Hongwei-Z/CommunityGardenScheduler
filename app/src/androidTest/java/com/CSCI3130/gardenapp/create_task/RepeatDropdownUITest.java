package com.CSCI3130.gardenapp.create_task;

import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.StringContains.containsString;


public class RepeatDropdownUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;

    @Before
    public void setUp() {
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
        onView(withId(R.id.repeatTypeButton)).perform(click());
        //click first item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every 2 days"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every 2 days"))));


        //click second item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every week"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every week"))));

        //click third  item
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every month"))).perform(click());

        //check if item was successfully selected
        onView(withId(R.id.repeatSpinner))
                .check(matches(withSpinnerText(containsString("Repeat every month"))));
    }
}
