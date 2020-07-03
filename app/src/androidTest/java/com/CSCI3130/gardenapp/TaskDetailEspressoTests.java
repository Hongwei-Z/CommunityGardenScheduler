package com.CSCI3130.gardenapp;

import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.allOf;


public class TaskDetailEspressoTests {

    @Rule
    public ActivityTestRule<TaskDetailInfo> Rule = new ActivityTestRule(TaskDetailInfo.class);
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
    // test toast message shown when click on complete button
    @Test
    public void ShowComplete() throws Exception {
        onView(allOf(withText(R.string.Complete), instanceOf(Button.class)))
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(1000);
        onView(withText(R.string.CompleteMessage))
                .inRoot(withDecorView(not(Rule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    //test button back
    @Test
    public void BackButton(){
        onView(withId(R.id.backtolistbutton))
                .perform(click());
        onView(withId(R.id.TaskDetail))
                .check(matches(isDisplayed()));
    }

}
