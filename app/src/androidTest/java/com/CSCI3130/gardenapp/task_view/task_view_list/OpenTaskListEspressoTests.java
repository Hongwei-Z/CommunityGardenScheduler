package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.content.Intent;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class OpenTaskListEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase testDB;
    private TaskViewList activity;

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.OPEN_TASKS;
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
        db.clearDatabase(); //clears test database
    }

    @Test
    public void testOpenFilter() {
        long currentDate = System.currentTimeMillis();
        //upload non-open task to test database
        Task task = new Task("Not Open Task", "This is a Test", 2, "Some User ID", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
        //check if task appears in filtered recyclerview, fail if it is there
        try {
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(doesNotExist());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        //upload open task to database
        task = new Task("Open Task", "This is a Test", 2, "", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
        //check if task appears in filtered recyclerview, fail if it is NOT there
        try {
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(1)).check(doesNotExist());
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0)).check(matches(hasDescendant(withText("Open Task"))));
            onView(withRecyclerView(R.id.recycleview_tasks).atPosition(0))
                    .check(matches(hasDescendant(withText("Due: " + DateFormatUtils.getDateFormatted(currentDate)))));
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
