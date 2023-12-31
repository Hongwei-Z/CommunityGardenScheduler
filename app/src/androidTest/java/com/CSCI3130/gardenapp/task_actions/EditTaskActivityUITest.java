package com.CSCI3130.gardenapp.task_actions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import androidx.core.content.res.ResourcesCompat;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EditTaskActivityUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;
    private Task testTask;

    // https://stackoverflow.com/questions/28742495/testing-background-color-espresso-android
    private static Matcher<View> matchesBackgroundColor(final int expectedColorID) {
        return new BoundedMatcher<View, View>(Button.class) {
            int actualColor;
            int expectedColor;
            String message;

            @Override
            protected boolean matchesSafely(View item) {
                if (item.getBackground() == null) {
                    message = item.getId() + " does not have a background";
                    return false;
                }
                Resources resources = item.getContext().getResources();
                expectedColor = ResourcesCompat.getColor(resources, expectedColorID, null);

                try {
                    actualColor = ((ColorDrawable) item.getBackground()).getColor();
                } catch (Exception e) {
                    actualColor = ((GradientDrawable) item.getBackground()).getColor().getDefaultColor();
                } finally {
                    if (actualColor == expectedColor) {
                        System.out.println("Success...: Expected Color " + String.format("#%06X", (0xFFFFFF & expectedColor))
                                + " Actual Color " + String.format("#%06X", (0xFFFFFF & actualColor)));
                    }
                }
                return actualColor == expectedColor;
            }

            @Override
            public void describeTo(final Description description) {
                if (actualColor != 0) {
                    message = "Background color did not match: Expected "
                            + String.format("#%06X", (0xFFFFFF & expectedColor))
                            + " was " + String.format("#%06X", (0xFFFFFF & actualColor));
                }
                description.appendText(message);
            }
        };
    }

    @Before
    public void setUp() {
        testTask = TaskGenerator.generateTask(true, WeatherCondition.HOT);
        testTask.setLocation("-30.00000, 40.00000");
        testDB = new TaskTestDatabase();
        testDB.uploadTask(testTask);
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent i = new Intent(targetContext, CreateTaskActivity.class);
        i.putExtra(targetContext.getString(R.string.editSetting_extra), true);
        i.putExtra(targetContext.getString(R.string.task_extra), testTask);

        activityScenarioRule.launchActivity(i);
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    @Test
    public void testCorrectLoad() throws UiObjectNotFoundException {
        onView(withId(R.id.textTitle)).check(matches(withText(R.string.textEditTitle)));
        onView(withId(R.id.editTitle)).check(matches(withText(testTask.getName())));
        onView(withId(R.id.editDescription)).check(matches(withText(testTask.getDescription())));
        String location = String.format("%.5f", activity.mapFragment.getSelectedLocation().latitude) + ", " + String.format("%.5f", activity.mapFragment.getSelectedLocation().longitude);
        assertEquals(testTask.getLocation(), location);
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Location " + testTask.getLocation()));
        marker.click();
        onView(withId(R.id.buttonConfirmAdd)).check(matches(withText(R.string.confirm_edit_task)));
        onView(withId(R.id.weatherSpinner)).check(matches(withSpinnerText("Hot")));
    }

    @Test
    public void testUpdateTask() {
        testTask.setName("Success");
        onView(withId(R.id.editTitle)).perform(clearText());
        onView(withId(R.id.editTitle)).perform(typeText(testTask.getName()), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        testDB.checkForTask(testTask);
    }

    private void checkPrioritySelected(int val) {
        int[] ids = {R.id.buttonPriority1, R.id.buttonPriority2, R.id.buttonPriority3, R.id.buttonPriority4, R.id.buttonPriority5};
        int[] colors = {R.color.colorPriority1, R.color.colorPriority2, R.color.colorPriority3, R.color.colorPriority4, R.color.colorPriority5};
        for (int i = 0; i < 5; i++) {
            if (val != i + 1) {
                onView(withId(ids[i])).check(matches(matchesBackgroundColor(R.color.colorUnselected)));
            } else {
                onView(withId(ids[i])).check(matches(matchesBackgroundColor(colors[i])));
            }
        }
    }
}


