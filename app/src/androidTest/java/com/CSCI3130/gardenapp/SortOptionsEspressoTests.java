package com.CSCI3130.gardenapp;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


/**
 * This class tests the sorting options on the filtering popup
 * @author Arjav Gupta
 */
public class SortOptionsEspressoTests {

    @Rule
    public ActivityTestRule<FilterPopUp> activityScenarioRule
            = new ActivityTestRule<>(FilterPopUp.class);

    //test for selection of category with no sorting order, should display a toast error
    @Test
    public void test_set_category_no_order_error(){
        onView(withId(R.id.sortPriorityBtn))
                .perform(click());
        onView(withId(R.id.applyButton))
                .perform(click());
        onView(withText("Please select a sorting order or clear.")).inRoot(withDecorView(not(is(activityScenarioRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //test for selection of sorting order with no category, should display a toast error
    @Test
    public void test_set_order_no_category_error() throws Exception{
        onView(withId(R.id.sortAscendingBtn))
                .perform(click());
        onView(withId(R.id.applyButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withText("Please select a sorting category or clear.")).inRoot(withDecorView(not(is(activityScenarioRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //test for the category buttons each deactivating the given button and activating the others
    @Test
    public void due_date_activated(){
        onView(withId(R.id.sortDueDateBtn))
                .perform(click());
        onView(withId(R.id.sortDueDateBtn)).check(matches(is(not(isEnabled()))));
        onView(withId(R.id.sortPriorityBtn)).check(matches(is(isEnabled())));
        onView(withId(R.id.sortAZBtn)).check(matches(is(isEnabled())));
    }
    @Test
    public void priority_activated(){
        onView(withId(R.id.sortPriorityBtn))
                .perform(click());
        onView(withId(R.id.sortPriorityBtn)).check(matches(is(not(isEnabled()))));
        onView(withId(R.id.sortDueDateBtn)).check(matches(is(isEnabled())));
        onView(withId(R.id.sortAZBtn)).check(matches(is(isEnabled())));
    }
    @Test
    public void az_activated(){
        onView(withId(R.id.sortAZBtn))
                .perform(click());
        onView(withId(R.id.sortAZBtn)).check(matches(is(not(isEnabled()))));
        onView(withId(R.id.sortPriorityBtn)).check(matches(is(isEnabled())));
        onView(withId(R.id.sortDueDateBtn)).check(matches(is(isEnabled())));
    }

    //tests for both order buttons deactivating the given button and activating the other
    @Test
    public void ascending_activated(){
        onView(withId(R.id.sortAscendingBtn))
                .perform(click());
        onView(withId(R.id.sortAscendingBtn)).check(matches(is(not(isEnabled()))));
        onView(withId(R.id.sortDescendingBtn)).check(matches(is(isEnabled())));
    }
    @Test
    public void descending_activated(){
        onView(withId(R.id.sortAscendingBtn))
                .perform(click());
        onView(withId(R.id.sortAscendingBtn)).check(matches(is(not(isEnabled()))));
        onView(withId(R.id.sortDescendingBtn)).check(matches(is(isEnabled())));
    }


}
