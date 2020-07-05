package com.CSCI3130.gardenapp.create_task;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.junit.*;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;


@LargeTest
public class CreateTaskActivityUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;

    @Before
    public void setUp(){
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
        int[] ids = {R.id.priorityButtons, R.id.buttonPriority2, R.id.buttonPriority3, R.id.buttonPriority4, R.id.buttonPriority5};
        onView(withId(ids[val - 1])).perform(click());
    }

    @Test
    public void testCorrectInputs() {
        Task task = TaskGenerator.generateTask(true);
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
    public void testMissingPriority() {
        Task task = TaskGenerator.generateTask(true);
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText(task.getLocation()), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testAllError() {
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        testDB.ensureNoDatabaseActivity();
    }
}
