package com.CSCI3130.gardenapp;


import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SignInEspressoTests {

    @Rule
    public ActivityTestRule<SignIn> activityScenarioRule
            = new ActivityTestRule<>(SignIn.class);

    //test sign in with empty email
    @Test
    public void empty_email_test() {
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.emailTxt_signin))
                .check(matches(hasErrorText("Please enter your email address")));
    }

    //test sign in with invalid email
    @Test
    public void invalid_email_test(){
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("arjav@arjav@arjav"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.emailTxt_signin))
                .check(matches(hasErrorText("Please enter a valid email address")));
    }

    //test sign in with empty password
    @Test
    public void empty_pass_test(){
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("arjav@arjav.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.passwordTxt_signin))
                .check(matches(hasErrorText("Please enter your password")));
    }

    //test sign in with invalid password
    @Test
    public void invalid_pass_test(){
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("arjav@arjav.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("fives"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.passwordTxt_signin))
                .check(matches(hasErrorText("Invalid Password")));
    }

    //test sign in with valid credentials
    @Test
    public void valid_test(){
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("arjav@arjav.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withText("Sign In Error. Please Try Again.")).inRoot(new ToastMatcher())
                .check(matches(withText("Sign In Error. Please Try Again.")));
    }

    //test that sign up button navigates to sign up page
    @Test
    public void sign_up_test(){
        onView(withId(R.id.signUpBtn_signin))
                .perform(click());
        onView(withId(R.id.title_signup))
                .check(matches(isDisplayed()));
    }


}
