package com.CSCI3130.gardenapp.create_task;

import android.widget.DatePicker;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;


@LargeTest
public class CreateTaskActivityUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;

    @Before
    public void setUp() {
        activityScenarioRule.launchActivity(null);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    private void clickCorrectPriority(int val) {
        int[] ids = {R.id.buttonPriority1, R.id.buttonPriority2, R.id.buttonPriority3, R.id.buttonPriority4, R.id.buttonPriority5};
        onView(withId(ids[val - 1])).perform(click());
    }

    @Test
    public void testCorrectInputs() {
        Task task = TaskGenerator.generateTask(false, System.currentTimeMillis() + 10000);
        //neat inputs
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testMissingTitle() {
        Task task = TaskGenerator.generateTask(true);
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingDescription() {
        Task task = TaskGenerator.generateTask(true);
        //neat inputs
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingLocation() {
        Task task = TaskGenerator.generateTask(true);
        //neat inputs
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testAllError() {
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testWeatherSelected() {
        Task task = TaskGenerator.generateTask(false, WeatherCondition.HOT);
        onView(withId(R.id.weatherTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.weatherSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Hot"))).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testRepeatedSelected() {
        Task task = TaskGenerator.generateTask(false, TaskRepeatCondition.REPEAT_WEEKLY);
        onView(withId(R.id.repeatTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every week"))).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testDueDate() {
        Task task = TaskGenerator.generateTask(false, 1598350371000L);
        onView(withId(R.id.dateTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.dueDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 25));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        testDB.checkForTask(task);
    }

    @Test
    public void testConditionToggle() {
        onView(withId(R.id.dueDate)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.repeatTypeButton)).perform(click());
        onView(withId(R.id.repeatSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.dueDate)).check(matches(not(isDisplayed())));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.weatherTypeButton)).perform(click());
        onView(withId(R.id.weatherSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.dueDate)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.dateTypeButton)).perform(click());
        onView(withId(R.id.dueDate)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));
    }

}
