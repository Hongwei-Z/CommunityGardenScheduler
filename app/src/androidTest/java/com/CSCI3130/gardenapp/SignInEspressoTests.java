package com.CSCI3130.gardenapp;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.CSCI3130.gardenapp.util.db.DatabaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SignInEspressoTests {

    @Rule
    public IntentsTestRule<SignIn> activityScenarioRule
            = new IntentsTestRule<>(SignIn.class);

    @BeforeClass
    public static void before() {
        DatabaseAuth.signOut();
    }

    @Before
    public void beforeTest() {
        DatabaseAuth.signOut();
    }

    @After
    public void after() {
        activityScenarioRule.finishActivity();
    }

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
                .check(matches(hasErrorText("Please enter a valid email address")));
    }

    //test sign in with invalid email
    @Test
    public void invalid_email_test() {
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
    public void empty_pass_test() {
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
    public void invalid_pass_test() {
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
    public void valid_test() throws InterruptedException {
        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("test@test.ca"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("test123456"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.emailTxt_signin))
                .check(matches(hasErrorText((String)null)));
        onView(withId(R.id.passwordTxt_signin))
                .check(matches(hasErrorText((String)null)));

    }

    //test that sign up button navigates to sign up page
    @Test
    public void sign_up_test(){
        onView(withId(R.id.signUpBtn_signin)).perform(click());
        onView(withId(R.id.title_signup))
                .check(matches(isDisplayed()));
    }

    //test sign in with valid credentials, but without passing reCAPTCHA
    @Test
    public void test_valid_cred_no_captcha() {

        onView(withId(R.id.emailTxt_signin))
                .perform(typeText("beth@email.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signin))
                .perform(typeText("password"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        onView(withId(R.id.captchaErrorText))
                .check(matches(withText("Please verify your identity with reCAPTCHA")));

    }
}
