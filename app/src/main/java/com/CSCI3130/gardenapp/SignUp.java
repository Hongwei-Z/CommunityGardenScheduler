package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    //UI element declarations
    EditText firstNameTxt, lastNameTxt, emailTxt, passwordTxt, passwordConfirmTxt;

    //firebase authentication object
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize firebase authentication object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //UI element assignments
        firstNameTxt = (EditText) findViewById(R.id.firstNameTxt_signup);
        lastNameTxt = (EditText) findViewById(R.id.lastNameTxt_signup);
        emailTxt = (EditText) findViewById(R.id.emailTxt_signup);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt_signup);
        passwordConfirmTxt = (EditText) findViewById(R.id.passwordConfirmTxt_signup);

    }

    //on click function for sign up button
    public void signUp_input_onclick(View v){
        //get strings from all text fields
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String passwordConf = passwordConfirmTxt.getText().toString();

        //attempt to sign up
        signUp(firstName, lastName, email, password, passwordConf);
    }

    //function checks if user inputs are valid
    public boolean validSignUpInfo(String firstName, String lastName, String email, String password, String passwordConf){
        //first and last names must not be empty
        if (firstName.isEmpty()){
            return false;
        }
        if (lastName.isEmpty()){
            return false;
        }

        //email must not be empty and must have valid pattern
        if (email.isEmpty()){
            return false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }

        //password must not be empty and must be at least 6 characters (for Firebase)
        if (password.isEmpty()){
            return false;
        }
        if (password.length() < 6){
            return false;
        }

        //password confirmation must not be empty and must match password
        if (passwordConf.isEmpty()){
            return false;
        }
        if (!(password.equals(passwordConf))){
            return false;
        }
        return true;
    }

    //displays errors where required
    public void errorDisplays(String firstName, String lastName, String email, String password, String passwordConf){

        //first and last names must not be empty
        if (firstName.isEmpty()){
            firstNameTxt.setError("Please enter your first name");
            firstNameTxt.requestFocus();
        }
        if (lastName.isEmpty()){
            lastNameTxt.setError("Please enter your last name");
            lastNameTxt.requestFocus();
        }

        //email must not be empty and must have valid pattern
        if (email.isEmpty()){
            emailTxt.setError("Please enter your email address");
            emailTxt.requestFocus();
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please enter a valid email address");
            emailTxt.requestFocus();
        }

        //password must not be empty and must be at least 6 characters (for Firebase)
        if (password.isEmpty()){
            passwordTxt.setError("Please enter a password");
            passwordTxt.requestFocus();
        } else if (password.length() < 6){
            passwordTxt.setError("Password must be at least 6 characters long");
            passwordTxt.requestFocus();
        }

        //password confirmation must not be empty and must match password
        if (passwordConf.isEmpty()){
            passwordConfirmTxt.setError("Please confirm password");
            passwordConfirmTxt.requestFocus();
        }
        if (!(password.equals(passwordConf))){
            passwordConfirmTxt.setError("Passwords do not match");
            passwordConfirmTxt.requestFocus();
        }
    }

    //function signs a user up based on given credentials
    public void signUp(String firstName, String lastName, String email, String password, String passwordConf){

        //validate sign up information
        if (validSignUpInfo(firstName, lastName, email, password, passwordConf)){

            //final variables for user profile updating
            final String name = (firstName + " " + lastName);

            //run create user function for firebase with given email and password
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //if sign up is unsuccessful, display error message
                    if(!task.isSuccessful()){
                        Toast.makeText(SignUp.this,"Sign up unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                    }

                    //if sign up is successful, add first/last names and go to welcome screen
                    else {

                        //get user and attempt to update profile with first/last name
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //if name addition is unsuccessful, give toast and retry
                                        if (!(task.isSuccessful())){
                                            Toast.makeText(SignUp.this, "Profile first/last name update error. Please try again.", Toast.LENGTH_SHORT).show();

                                            //if successful, all items in profile have been added, move on to welcome page
                                        } else {
                                            Intent i = new Intent(SignUp.this, Welcome.class);
                                            startActivity(i);
                                        }
                                    }
                                });
                    }
                }
            });

        //if there are input problems
        } else {
            errorDisplays(firstName, lastName, email, password, passwordConf);
        }


    }

}
