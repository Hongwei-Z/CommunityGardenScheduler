package com.CSCI3130.gardenapp.create_task;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.CSCI3130.gardenapp.R;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateTaskActivityUITest extends TestCase {
    @Rule
    public ActivityScenarioRule<CreateTaskActivity> activityScenarioRule = new ActivityScenarioRule<CreateTaskActivity>(CreateTaskActivity.class);

    @Test
    public void testCorrectInputs() {
        //neat inputs
        onView(ViewMatchers.withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
    }

    @Test
    public void testMissingTitle() {
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
    }

    @Test
    public void testMissingDescription() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
    }

    @Test
    public void testMissingLocation() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
    }

    @Test
    public void testMissingPriority() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
    }

    @Test
    public void testAllError() {
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
    }
}
