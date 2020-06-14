package com.CSCI3130.gardenapp;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FilterEspressoTest {
    @Rule
    public ActivityScenarioRule<FilterActivity> activityActivityScenarioRule = new ActivityScenarioRule<FilterActivity>(FilterActivity.class);

    /***
     * Didn't find an effective way to test the calendar yet
     */
    @Test
    public void testButtons(){
        onView(withId(R.id.filterButton2)).perform(click());
        onView(withId(R.id.filterTextView)).check(matches(withText("Filters")));
        onView(withId(R.id.dueBetweenTextView)).check(matches(withText("Due Between")));
        onView(withId(R.id.priorityTextView)).check(matches(withText("Priority")));
        onView(withId(R.id.filterTextView)).check(matches(withText("Filters")));
        onView(withId(R.id.startDateButton)).check(matches(withText("Start Date")));
        onView(withId(R.id.endDateButton)).check(matches(withText("End Date")));
        onView(withId(R.id.clearButton)).check(matches(withText("Clear")));
        onView(withId(R.id.applyButton)).check(matches(withText("Apply")));
        onView(withId(R.id.priority1Button)).check(matches(withText("1")));
        onView(withId(R.id.priority2Button)).check(matches(withText("2")));
        onView(withId(R.id.priority3Button)).check(matches(withText("3")));
        onView(withId(R.id.priority4Button)).check(matches(withText("4")));
        onView(withId(R.id.priority5Button)).check(matches(withText("5")));
    }

    /***
     * onView(withId(R.id.filterButton2)).perform(click());
     * onView(withId(R.id.startDateButton)).perform(click()).perform(PickerActions.setDate(2020,7,1));
     * onView(withId(android.R.id.button1)).perform(click());
     * onView(withId(R.id.startDateButton)).check(matches(withText("2020, 7,1")));
     */
}
