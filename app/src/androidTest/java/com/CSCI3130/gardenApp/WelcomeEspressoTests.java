package com.CSCI3130.gardenApp;

import android.view.KeyEvent;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WelcomeEspressoTests {

    @Rule
    public ActivityTestRule<Welcome> activityScenarioRule
            = new ActivityTestRule<>(Welcome.class);

    //test that log out button switches views
    @Test
    public void log_out_test(){
        onView(withId(R.id.signOutBtn_welcome))
                .perform(click());
        onView(withId(R.id.title_signin))
                .check(matches(isDisplayed()));
    }

    /*
    @Test
    public void testStrongPassword(){
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText("Nihir@123"));
        onView(withId(R.id.button))
                .perform(click());
        onView(withId(R.id.textView))
                .check(matches(withText("Strong password")));
    }


    @Test
    public void testWeakPassword(){
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText("nihir@123"));
        onView(withId(R.id.button))
                .perform(click());
        onView(withId(R.id.textView))
                .check(matches(withText("Weak password, enter a stronger one.")));
    }

*/

}

