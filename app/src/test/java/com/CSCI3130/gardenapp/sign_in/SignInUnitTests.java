package com.CSCI3130.gardenapp.sign_in;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SignInUnitTests {

    //sign in page for testing methods
    SignIn s = new SignIn();

    //test for blank email
    @Test
    public void blank_email_test() {
        assertFalse(s.validInputs("", "testing"));
    }

    //test for invalid email
    @Test
    public void invalid_email_test() {
        assertFalse(s.validInputs("arjav@arjav@arjav", "testing"));
    }

    //test for blank password
    @Test
    public void blank_passwrd_test() {
        assertFalse(s.validInputs("arjav@gmail.com", ""));
    }

    //test for invalid (under 6 character) password
    @Test
    public void short_passwrd_test() {
        assertFalse(s.validInputs("arjav@gmail.com", "fives"));
    }

    //test for all invalid inputs (invalid email and short password)
    @Test
    public void invalid_all_test() {
        assertFalse(s.validInputs("arjav@arjav@arjav", "fives"));
    }

    //test for valid inputs
    @Test
    public void valid_inputs_test() {
        assertTrue(s.validInputs("arjav@gmail.com", "password"));
    }


}