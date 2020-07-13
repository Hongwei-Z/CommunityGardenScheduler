package com.CSCI3130.gardenapp.task_view_list;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
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

public class MyTaskListEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.MY_TASKS;
        intent.putExtra("activeTaskListContext", activeTaskListContext);
        activityScenarioRule.launchActivity(intent);
        testDB = new TaskTestDatabase();
        testDB.setDbRead(activeTaskListContext);
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, activeTaskListContext));

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
        onView(ViewMatchers.withId(R.id.page_name)).check(matches(withText("My Tasks")));
    }

    @Test
    public void recyclerViewItemContainsExpectedText() {
        long currentDate = System.currentTimeMillis();
        Task task = new Task("My Task", "This is a Test", 2, "not current UUID", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
        try {
            Thread.sleep(3000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        task = new Task("My Task", "This is a Test", 2, FirebaseAuth.getInstance().getUid(), "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(doesNotExist());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(matches(hasDescendant(withText("My Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Due: " + DateFormatUtils.getDateFormatted(currentDate)))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withId(R.id.task_user_profile))));
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
