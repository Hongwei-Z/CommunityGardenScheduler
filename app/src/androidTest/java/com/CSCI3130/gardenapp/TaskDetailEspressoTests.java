package com.CSCI3130.gardenapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


public class TaskDetailEspressoTests {

    @Rule
    public IntentsTestRule<TaskDetailInfo> rule = new IntentsTestRule<>(TaskDetailInfo.class, false, false);
    Task task;

    @Before
    public void before() {
        task = TaskGenerator.generateTask(false);
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent i = new Intent(targetContext, TaskDetailInfo.class);
        i.putExtra(targetContext.getString(R.string.task_extra), task);
        rule.launchActivity(i);
    }

    // check if there is a complete button
    @Test
    public void clickButton() {
        onView(withId(R.id.buttonComplete)).check(matches(isDisplayed()));
    }

    // check if there is a edit button
    @Test
    public void clickEdit() {
        onView(withId(R.id.buttonEdit)).check(matches(isDisplayed()));
    }

    @Test
    public void checkProperLoad() {
        onView(withId(R.id.taskTitle)).check(matches(withText(task.getName())));
        onView(withId(R.id.taskDescription)).check(matches(withText(task.getDescription())));
        onView(withId(R.id.taskLocation)).check(matches(withText(task.getLocation())));
        onView(withId(R.id.taskPriority)).check(matches(withText("" + task.getPriority())));
        onView(withId(R.id.taskDuedate)).check(matches(withText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()))));
    }

    // test toast message shown when click on complete button
    @Test
    public void ShowComplete() {
        onView(allOf(withText(R.string.Complete), instanceOf(Button.class)))
                .check(matches(isDisplayed()))
                .perform(click());
    }
}
