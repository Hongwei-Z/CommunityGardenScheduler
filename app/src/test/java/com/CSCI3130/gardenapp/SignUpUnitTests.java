package com.CSCI3130.gardenapp;


import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpUnitTests {


    //sign in page for testing methods
    SignUp s = new SignUp();

    //example strings for testing
    String blank = ""; //blank
    String valid_name = "John"; //valid name
    String invalid_email = "arjav@arjav@arjav"; //invalid email
    String valid_email = "arjav@gmail.com"; //valid email
    String invalid_password = "fives"; //invalid password
    String valid_password = "password"; //valid password
    String valid_password2 = "passwrd"; //alternate valid password

    //test for blank first name
    @Test
    public void blank_first_name_test(){
        assertFalse(s.validSignUpInfo(blank, valid_name, valid_email, valid_password, valid_password));
    }

    //test for blank last name
    @Test
    public void blank_last_name_teset(){
        assertFalse(s.validSignUpInfo(valid_name, blank, valid_email, valid_password, valid_password));
    }
    //test for blank email
    @Test
    public void blank_email_test(){
        assertFalse(s.validSignUpInfo(valid_name, valid_name, blank, valid_password, valid_password));
    }

    //test for invalid email
    @Test
    public void invalid_email_test(){
        assertFalse(s.validSignUpInfo(valid_name, valid_name, invalid_email, valid_password, valid_password));
    }

    //test for blank password
    @Test
    public void blank_passwrd_test(){
        assertFalse(s.validSignUpInfo(valid_name, valid_name, valid_email, blank, valid_password));
    }

    //test for invalid (under 6 character) password
    @Test
    public void short_passwrd_test(){
        assertFalse(s.validSignUpInfo(valid_name, valid_name, valid_email, invalid_password, invalid_password));
    }

    //test that password confirmation catches non-matching passwords
    @Test
    public void pass_confirm_diff_test(){
        assertFalse(s.validSignUpInfo(valid_name, valid_name, valid_email, valid_password, valid_password2));
    }

    //test for valid inputs
    @Test
    public void valid_inputs_test(){
        assertTrue(s.validSignUpInfo(valid_name, valid_name, valid_email, valid_password, valid_password));
    }



}