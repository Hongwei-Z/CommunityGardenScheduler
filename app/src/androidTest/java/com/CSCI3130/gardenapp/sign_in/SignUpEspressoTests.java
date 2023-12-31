package com.CSCI3130.gardenapp.sign_in;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.db.DatabaseAuth;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SignUpEspressoTests {

    @Rule
    public ActivityTestRule<SignUp> activityScenarioRule
            = new ActivityTestRule<>(SignUp.class);

    //example strings for testing
    String blank = ""; //blank
    String valid_name = "John"; //valid name
    String invalid_email = "arjav@arjav@arjav"; //invalid email
    String valid_email = "arjav@gmail.com"; //valid email
    String invalid_password = "fives"; //invalid password
    String valid_password = "password"; //valid password
    String valid_password2 = "passwrd"; //alternate valid password

    @Before
    public void before() {
        DatabaseAuth.signOut();
    }

    //test for empty first name error
    @Test
    public void empty_fname_test() {
        onView(ViewMatchers.withId(R.id.firstNameTxt_signup))
                .perform(typeText(blank));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.firstNameTxt_signup))
                .check(matches(hasErrorText("Please enter your first name")));
    }

    //test for empty last name error
    @Test
    public void empty_lname_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(blank));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.lastNameTxt_signup))
                .check(matches(hasErrorText("Please enter your last name")));
    }

    //test for empty email address error
    @Test
    public void empty_email_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(blank));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.emailTxt_signup))
                .check(matches(hasErrorText("Please enter your email address")));
    }

    //test for invalid email address error
    @Test
    public void invalid_email_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(invalid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.emailTxt_signup))
                .check(matches(hasErrorText("Please enter a valid email address")));
    }

    //test for empty password error
    @Test
    public void empty_pass_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(blank));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordTxt_signup))
                .check(matches(hasErrorText("Please enter a password")));
    }

    //test for invalid password error
    @Test
    public void invalid_pass_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(invalid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(invalid_password));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordTxt_signup))
                .check(matches(hasErrorText("Password must be at least 6 characters long")));
    }

    //test for empty password confirmation error
    @Test
    public void empty_pass_conf_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(blank));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordConfirmTxt_signup))
                .check(matches(hasErrorText("Passwords do not match")));
    }

    //test for password mismatch error
    @Test
    public void pass_mismatch_test() {
        onView(withId(R.id.firstNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.lastNameTxt_signup))
                .perform(typeText(valid_name));
        closeSoftKeyboard();
        onView(withId(R.id.emailTxt_signup))
                .perform(typeText(valid_email));
        closeSoftKeyboard();
        onView(withId(R.id.passwordTxt_signup))
                .perform(typeText(valid_password));
        closeSoftKeyboard();
        onView(withId(R.id.passwordConfirmTxt_signup))
                .perform(typeText(valid_password2));
        closeSoftKeyboard();
        onView(withId(R.id.signUpBtn_signup))
                .perform(click());
        onView(withId(R.id.passwordConfirmTxt_signup))
                .check(matches(hasErrorText("Passwords do not match")));
    }

}
