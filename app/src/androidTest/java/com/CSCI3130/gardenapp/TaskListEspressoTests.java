package com.CSCI3130.gardenapp;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import com.CSCI3130.gardenapp.util.data.Task;

import java.text.SimpleDateFormat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasBackground;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class TaskListEspressoTests {

    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    @Before
    public void setUp(){
        Intent intent = new Intent();
        String setting = "allTasks";
        intent.putExtra("setting", setting);
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, setting));
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
        onView(withId(R.id.page_name)).check(matches(withText("All Tasks")));
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
        Task task = new Task("Test Task", "This is a Test", 2, "Beth", "Location", System.currentTimeMillis());
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Test Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Due: "+ new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).perform(click());
            Espresso.pressBack();
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(doesNotExist());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        task = new Task("Test Task 2", "This is a Test", 2, "Beth", "Location", System.currentTimeMillis());
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Test Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Due: "+ new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).perform(click());
            Espresso.pressBack();
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Test Task 2"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1))
                    .check(matches(hasDescendant(withText("Due: "+ new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis())))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).perform(click());
            Espresso.pressBack();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
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
        for (int i = 1; i <= 20; i++){
            String taskName = "Task " + i;
            Task task = new Task(taskName, "This is a Test", 2, "Beth", "Location", System.currentTimeMillis());
            testDB.uploadTask(task);
        }
        try {
            Thread.sleep(1000);
            onView(ViewMatchers.withId(R.id.recycleview_tasks)).perform(ViewActions.swipeUp());
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(19))
                    .check(matches(hasDescendant(withText("Task 20"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(19)).perform(click());
            Espresso.pressBack();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}

