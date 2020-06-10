package com.CSCI3130.gardenapp;


import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpUnitTests {


    //sign in page for testing methods
    SignUp s = new SignUp();

    //example strings for testing
    String b = ""; //blank
    String n = "John"; //valid name
    String i_e = "arjav@arjav@arjav"; //invalid email
    String v_e = "arjav@gmail.com"; //valid email
    String i_p = "fives"; //invalid password
    String v_p = "password"; //valid password
    String v_p2 = "passwrd"; //alternate valid password

    //test for blank first name
    @Test
    public void blank_first_name_test(){
        assertFalse(s.validSignUpInfo(b, n, v_e, v_p, v_p));
    }

    //test for blank last name
    @Test
    public void blank_last_name_teset(){
        assertFalse(s.validSignUpInfo(n, b, v_e, v_p, v_p));
    }
    //test for blank email
    @Test
    public void blank_email_test(){
        assertFalse(s.validSignUpInfo(n, n, b, v_p, v_p));
    }

    //test for invalid email
    @Test
    public void invalid_email_test(){
        assertFalse(s.validSignUpInfo(n, n, i_e, v_p, v_p));
    }

    //test for blank password
    @Test
    public void blank_passwrd_test(){
        assertFalse(s.validSignUpInfo(n, n, v_e, b, v_p));
    }

    //test for invalid (under 6 character) password
    @Test
    public void short_passwrd_test(){
        assertFalse(s.validSignUpInfo(n, n, v_e, i_p, i_p));
    }

    //test that password confirmation catches non-matching passwords
    @Test
    public void pass_confirm_diff_test(){
        assertFalse(s.validSignUpInfo(n, n, v_e, v_p, v_p2));
    }

    //test for valid inputs
    @Test
    public void valid_inputs_test(){
        assertTrue(s.validSignUpInfo(n, n, v_e, v_p, v_p));
    }



}