package com.CSCI3130.gardenapp.task_view.task_view_list;

import android.content.Intent;
import android.util.Log;
import androidx.drawerlayout.widget.DrawerLayout;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.CSCI3130.gardenapp.task_view.task_view_list.SortListEspressoTests.withRecyclerView;
import static com.CSCI3130.gardenapp.task_view.task_view_list.DrawableMatcher.withDrawable;
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
    public TaskTestDatabase testDB;
    public TaskViewList activity;

    @After
    public void after() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ActiveTaskListContext activeTaskListContext = ActiveTaskListContext.ALL_TASKS;
        intent.putExtra("activeTaskListContext", activeTaskListContext);
        activityScenarioRule.launchActivity(intent);
        activity = activityScenarioRule.getActivity();
        testDB = new TaskTestDatabase();
        activity.db = testDB;
        testDB.getDbRead().addValueEventListener(testDB.getTaskData(activity.recyclerView, activeTaskListContext));
    }

    @Test
    public void DueTime_RedAlert1DayLeft () throws InterruptedException {
        long currentDate = System.currentTimeMillis();
        long testDate = currentDate + TimeUnit.DAYS.toMillis(1);
        Task task = TaskGenerator.generateTask(false, testDate);
        testDB.uploadTask(task);
        Thread.sleep(4000);

        onView(withRecyclerView(R.id.recycleview_tasks).atPositionOnView(0, R.id.task_due_symbol))
                .check(matches(withDrawable(R.drawable.red)));
    }

    @Test
    public void DueTime_YellowAlert2DaysLeft () throws InterruptedException {
        long currentDate = System.currentTimeMillis();
        long testDate = currentDate + TimeUnit.DAYS.toMillis(2);
        Task task = TaskGenerator.generateTask(false, testDate);
        testDB.uploadTask(task);
        Thread.sleep(4000);

        onView(withRecyclerView(R.id.recycleview_tasks).atPositionOnView(0, R.id.task_due_symbol))
                .check(matches(withDrawable(R.drawable.yellow)));
    }

    @Test
    public void DueTime_YellowAlert3DaysLeft () throws InterruptedException {
        long currentDate = System.currentTimeMillis();
        long testDate = currentDate + TimeUnit.DAYS.toMillis(3);
        Task task = TaskGenerator.generateTask(false, testDate);
        testDB.uploadTask(task);
        Thread.sleep(4000);

        onView(withRecyclerView(R.id.recycleview_tasks).atPositionOnView(0, R.id.task_due_symbol))
                .check(matches(withDrawable(R.drawable.yellow)));
    }

    @Test
    public void recyclerViewCardsContainImage() {
        onView(AllOf.allOf(ViewMatchers.withId(R.id.task_card), hasDescendant(withId(R.id.task_due_symbol))));
    }

    @Test
    public void testShownLogRecords_RealDataOne (){
        long currentDate = System.currentTimeMillis();
        Task task = new Task("Test", "Test", 2, "Test", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
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
    public void testShownLogRecords_RealDataTwo () {
        long currentDate = System.currentTimeMillis();
        Task task = new Task("Test", "Test", 3, "Test", "Location", currentDate, TaskRepeatCondition.REPEAT_NONE);
        testDB.uploadTask(task);
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
