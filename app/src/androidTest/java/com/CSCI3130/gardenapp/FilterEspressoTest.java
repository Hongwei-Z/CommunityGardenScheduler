package com.CSCI3130.gardenapp;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;

public class FilterEspressoTest {

    @Rule
    public ActivityScenarioRule<FilterPopUp> activityActivityScenarioRule = new ActivityScenarioRule<>(FilterPopUp.class);

    @Test
    public void testFilterTextDisplay(){
        onView(withId(R.id.filterTextView)).check(matches(withText("Filters")));
        onView(withId(R.id.dueBetweenTextView)).check(matches(withText("Due Between")));
        onView(withId(R.id.priorityTextView)).check(matches(withText("Priority")));
        onView(withId(R.id.filterTextView)).check(matches(withText("Filters")));
        onView(withId(R.id.startDateButton)).check(matches(withText("Start Date")));
        onView(withId(R.id.endDateButton)).check(matches(withText("End Date")));
        onView(withId(R.id.clearButton)).check(matches(withText("Clear")));
        onView(withId(R.id.applyButton)).check(matches(withText("APPLY")));
        onView(withId(R.id.filterPriorityButton1)).check(matches(withText("1")));
        onView(withId(R.id.filterPriorityButton2)).check(matches(withText("2")));
        onView(withId(R.id.filterPriorityButton3)).check(matches(withText("3")));
        onView(withId(R.id.filterPriorityButton4)).check(matches(withText("4")));
        onView(withId(R.id.filterPriorityButton5)).check(matches(withText("5")));
    }

    @Test
    public void testDateSelectTextDisplay(){
        onView(withId(R.id.clearButton)).perform(click());
        onView(withId(R.id.startDateButton)).check(matches(withText("Start Date")));
        onView(withId(R.id.endDateButton)).check(matches(withText("End Date")));
        onView(withId(R.id.applyButton)).check(matches(withText("APPLY")));
    }

    @Test
    public void testButtonClickable(){
        onView(withId(R.id.filterPriorityButton1)).check(matches(isClickable()));
        onView(withId(R.id.filterPriorityButton2)).check(matches(isClickable()));
        onView(withId(R.id.filterPriorityButton3)).check(matches(isClickable()));
        onView(withId(R.id.filterPriorityButton4)).check(matches(isClickable()));
        onView(withId(R.id.filterPriorityButton5)).check(matches(isClickable()));
        onView(withId(R.id.startDateButton)).check(matches(isClickable()));
        onView(withId(R.id.endDateButton)).check(matches(isClickable()));
        onView(withId(R.id.clearButton)).check(matches(isClickable()));
        onView(withId(R.id.applyButton)).check(matches(isClickable()));
    }
}