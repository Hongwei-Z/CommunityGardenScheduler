package com.CSCI3130.gardenapp;


import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.checkerframework.framework.qual.AnnotatedFor;
import org.junit.*;

import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;


import java.net.UnknownHostException;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class SignInEspressoTests {

    @Rule
    public IntentsTestRule<SignIn> activityScenarioRule
            = new IntentsTestRule<>(SignIn.class);

    @BeforeClass
    public static void before() throws InterruptedException {
        FirebaseAuth.getInstance().signOut();
    }

    @Before
    public void beforeTest() throws InterruptedException {
        FirebaseAuth.getInstance().signOut();
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
                .perform(typeText("test123"));
        closeSoftKeyboard();
        onView(withId(R.id.signInBtn_signin))
                .perform(click());
        Thread.sleep(500);
        intended(hasComponent(hasShortClassName(".Welcome")));
    }

    //test that sign up button navigates to sign up page
    @Test
    public void sign_up_test() {
        onView(withId(R.id.signUpBtn_signup)).perform(click());
        onView(withId(R.id.title_signup))
                .check(matches(isDisplayed()));
    }


}
