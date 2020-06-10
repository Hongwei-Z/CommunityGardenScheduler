package com.CSCI3130.gardenapp;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignUpEspressoTests {

    @Rule
    public ActivityTestRule<SignUp> activityScenarioRule
            = new ActivityTestRule<>(SignUp.class);

    //example strings for testing
    String b = ""; //blank
    String n = "John"; //valid name
    String i_e = "arjav@arjav@arjav"; //invalid email
    String v_e = "arjav@gmail.com"; //valid email
    String i_p = "fives"; //invalid password
    String v_p = "password"; //valid password
    String v_p2 = "passwrd"; //alternate valid password

    //test for empty first name error
    @Test
    public void empty_fname_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(b));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.firstNameTxt_signup))
                .check(matches(hasErrorText("Please enter your first name")));
    }

    //test for empty last name error
    @Test
    public void empty_lname_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(b));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.lastNameTxt_signup))
                .check(matches(hasErrorText("Please enter your last name")));
    }

    //test for empty email address error
    @Test
    public void empty_email_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(b));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.emailTxt_signup))
                .check(matches(hasErrorText("Please enter your email address")));
    }

    //test for invalid email address error
    @Test
    public void invalid_email_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(i_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.emailTxt_signup))
                .check(matches(hasErrorText("Please enter a valid email address")));
    }

    //test for empty password error
    @Test
    public void empty_pass_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(b));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordTxt_signup))
                .check(matches(hasErrorText("Please enter a password")));
    }

    //test for invalid password error
    @Test
    public void invalid_pass_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(i_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(i_p));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordTxt_signup))
                .check(matches(hasErrorText("Password must be at least 6 characters long")));
    }

    //test for empty password confirmation error
    @Test
    public void empty_pass_conf_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(b));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordConfirmTxt_signup))
                .check(matches(hasErrorText("Passwords do not match")));
    }

    //test for password mismatch error
    @Test
    public void pass_mismatch_test(){
        onView(withId(R.id.firstNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(click())
                .perform(typeText(n));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(click())
                .perform(typeText(v_e));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(click())
                .perform(typeText(v_p));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(click())
                .perform(typeText(v_p2));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordConfirmTxt_signup))
                .check(matches(hasErrorText("Passwords do not match")));
    }

}
