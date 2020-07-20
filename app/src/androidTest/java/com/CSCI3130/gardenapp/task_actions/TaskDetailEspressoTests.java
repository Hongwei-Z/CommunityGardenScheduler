package com.CSCI3130.gardenapp.task_actions;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.task_actions.TaskDetailInfo;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;


public class TaskDetailEspressoTests {

    @Rule
    public IntentsTestRule<TaskDetailInfo> rule = new IntentsTestRule<>(TaskDetailInfo.class, false, false);
    Task task;

    @Before
    public void before() {
        task = TaskGenerator.generateTask(false, System.currentTimeMillis());
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent i = new Intent(targetContext, TaskDetailInfo.class);
        i.putExtra(targetContext.getString(R.string.task_extra), task);
        rule.launchActivity(i);
    }


    // check if there is a edit button
    @Test
    public void checkEdit() {
        onView(withId(R.id.buttonEdit)).check(matches(isDisplayed()));
    }

    @Test
    public void checkUnregister() {
        onView(withId(R.id.buttonUnregister)).check(matches(isDisplayed()));
    }

    @Test
    public void checkProperLoad() throws UiObjectNotFoundException, InterruptedException {
        onView(withId(R.id.taskTitle)).check(matches(withText(task.getName())));
        onView(withId(R.id.taskDescription)).check(matches(withText(task.getDescription())));
        String location = String.format("%.5f", MapFragment.selectedLocation.latitude) + ", " + String.format("%.5f", MapFragment.selectedLocation.longitude);
        assertEquals(task.getLocation(), location);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Location " + task.getLocation()));
        marker.click();
        onView(withId(R.id.taskPriority)).check(matches(withText("" + task.getPriority())));
        onView(withId(R.id.taskDuedate)).check(matches(withText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()))));
    }

    // checks if complete button exists
    @Test
    public void checkComplete() {
        onView(allOf(withText(R.string.Complete), instanceOf(Button.class)))
                .check(matches(isDisplayed()));
    }
}
