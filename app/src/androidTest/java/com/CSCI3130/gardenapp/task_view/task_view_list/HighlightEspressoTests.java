package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.content.Intent;
import android.util.Log;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import java.util.concurrent.TimeUnit;

public class HighlightEspressoTests {
    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);
    public TaskTestDatabase db;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.ALL_TASKS;
        intent.putExtra("activeTaskListContext", activeTaskListContext);
        activityScenarioRule.launchActivity(intent);
        db = new TaskTestDatabase();
        db.setDbRead(activeTaskListContext);
    }

    @Test
    public void testShown_One (){
        long currentDate = System.currentTimeMillis();
        Task task = new Task("Test", "Test", 1, "Test", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        db.uploadTask(task);
        long TestDate = 1594869824409L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(TestDate - currentDate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
            System.out.print("due in one day");
        } else if (diff_date == 2 || diff_date ==3 ){
            Log.i("K", "due in two/three days");
            System.out.print("due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
            System.out.print("Hide symbol");
        }
    }

    @Test
    public void recyclerViewCardsContainImage() {
        onView(AllOf.allOf(ViewMatchers.withId(R.id.task_card), hasDescendant(withId(R.id.task_due_symbol))));
    }

    @Test
    public void testShown_Two (){
        long currentDate = System.currentTimeMillis();
        Task task = new Task("Test", "Test", 2, "Test", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        db.uploadTask(task);
        long TestDate = 1594873362654L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(currentDate - TestDate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
            System.out.print("due in one day");
        } else if (diff_date == 2 || diff_date ==3 ){
            Log.i("K", "due in two/three days");
            System.out.print("due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
            System.out.print("Hide symbol");
        }
    }
    @Test
    public void testShown_Three () {
        long currentDate = System.currentTimeMillis();
        Task task = new Task("Test", "Test", 2, "Test", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        db.uploadTask(task);
        long TestDate = 1594873383633L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(currentDate - TestDate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
            System.out.print("due in one day");
        } else if (diff_date == 2 || diff_date == 3) {
            Log.i("K", "due in two/three days");
            System.out.print("due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
            System.out.print("Hide symbol");
        }
    }
}
