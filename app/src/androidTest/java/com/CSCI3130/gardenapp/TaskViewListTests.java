package com.CSCI3130.gardenapp;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.util.data.Task;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasBackground;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskViewListTests {

    @Rule
    public ActivityTestRule<TaskViewList> taskViewListTestRule
            = new ActivityTestRule<>(TaskViewList.class);

    @Before
    public void start() {
        taskViewListTestRule.launchActivity(null);
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void recyclerViewCardsContainTasksInfo() {
        onView(allOf(withId(R.id.task_card), hasDescendant(withId(R.id.task_name))));
        onView(allOf(withId(R.id.task_card), hasDescendant(withId(R.id.task_date))));
        onView(allOf(withId(R.id.task_card), hasDescendant(withId(R.id.task_priority))));
        onView(allOf(withId(R.id.task_card), hasDescendant(withId(R.id.task_user_profile))));
    }

    @Test
    public void recyclerViewItemContainsExpectedText() {
        onView(withRecyclerView(R.id.recycleview_tasks).atPosition(3))
                .check(matches(hasDescendant(withText("Dig some dirt"))));
        onView(withRecyclerView(R.id.recycleview_tasks).atPosition(3))
                .check(matches(hasDescendant(withText("04-01-2020"))));
        onView(withRecyclerView(R.id.recycleview_tasks).atPosition(3)).perform(click());
    }

    @Test
    public void recyclerViewItemDisplaysPriority() {
        onView(allOf(withText("1"), hasBackground(R.color.colorPriority1)));
        onView(allOf(withText("2"), hasBackground(R.color.colorPriority2)));
        onView(allOf(withText("3"), hasBackground(R.color.colorPriority3)));
        onView(allOf(withText("4"), hasBackground(R.color.colorPriority4)));
        onView(allOf(withText("5"), hasBackground(R.color.colorPriority5)));
    }

    @Test
    public void scrollToItemBelowFold() {
        onView(ViewMatchers.withId(R.id.recycleview_tasks)).perform(ViewActions.swipeUp());
        onView(withRecyclerView(R.id.recycleview_tasks).atPosition(8))
                .check(matches(hasDescendant(withText("Add compost"))));
        onView(withRecyclerView(R.id.recycleview_tasks).atPosition(8)).perform(click());
    }
}