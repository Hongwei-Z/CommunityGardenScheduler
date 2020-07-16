package com.CSCI3130.gardenapp.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.Welcome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Activity class which allows users to sign up for the app,
 * saving their information on Firebase as a new user
 *
 * @author Arjav Gupta
 */
public class SignUp extends AppCompatActivity {

    //UI element declarations
    EditText firstNameTxt, lastNameTxt, emailTxt, passwordTxt, passwordConfirmTxt;

    //firebase authentication object
    FirebaseAuth mFirebaseAuth;

    /**
     * Initial onCreate method for activity
     *
     * @param savedInstanceState - save state of activity, autogenerated by android studio
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize firebase authentication object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //UI element assignments
        firstNameTxt = findViewById(R.id.firstNameTxt_signup);
        lastNameTxt = findViewById(R.id.lastNameTxt_signup);
        emailTxt = findViewById(R.id.emailTxt_signup);
        passwordTxt = findViewById(R.id.passwordTxt_signup);
        passwordConfirmTxt = findViewById(R.id.passwordConfirmTxt_signup);

    }

    //on click function for sign up button

    /**
     * onClick method for sign up button
     * Calls signUp function and passes in user inputs
     *
     * @param v - view object of UI element calling onClick method (button)
     */
    public void signUp_input_onclick(View v) {
        //get strings from all text fields
        String firstName = firstNameTxt.getText().toString();
        String lastName = lastNameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String passwordConf = passwordConfirmTxt.getText().toString();

        //attempt to sign up
        signUp(firstName, lastName, email, password, passwordConf);
    }

    /**
     * Method checks if user inputs are valid
     *
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param email        - user's email address
     * @param password     - user's password
     * @param passwordConf - repetition of password, for confirmation
     * @return boolean - true/false based on whether all inputs are valid
     */
    public boolean validSignUpInfo(String firstName, String lastName, String email, String password, String passwordConf) {
        //first and last names must not be empty
        if (firstName.isEmpty()) {
            return false;
        }
        if (lastName.isEmpty()) {
            return false;
        }

        //email must not be empty and must have valid pattern
        if (email.isEmpty()) {
            return false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }

        //password must not be empty and must be at least 6 characters (for Firebase)
        if (password.isEmpty()) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }

        //password confirmation must not be empty and must match password
        if (passwordConf.isEmpty()) {
            return false;
        }
        return password.equals(passwordConf);
    }

    /**
     * Method checks whether user inputs are valid and displays error messages accordingly
     *
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param email        - user's email address
     * @param password     - user's password
     * @param passwordConf - repetition of password, for confirmation
     */
    public void errorDisplays(String firstName, String lastName, String email, String password, String passwordConf) {

        //first and last names must not be empty
        if (firstName.isEmpty()) {
            firstNameTxt.setError("Please enter your first name");
            firstNameTxt.requestFocus();
        }
        if (lastName.isEmpty()) {
            lastNameTxt.setError("Please enter your last name");
            lastNameTxt.requestFocus();
        }

        //email must not be empty and must have valid pattern
        if (email.isEmpty()) {
            emailTxt.setError("Please enter your email address");
            emailTxt.requestFocus();
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Please enter a valid email address");
            emailTxt.requestFocus();
        }

        //password must not be empty and must be at least 6 characters (for Firebase)
        if (password.isEmpty()) {
            passwordTxt.setError("Please enter a password");
            passwordTxt.requestFocus();
        } else if (password.length() < 6) {
            passwordTxt.setError("Password must be at least 6 characters long");
            passwordTxt.requestFocus();
        }

        //password confirmation must not be empty and must match password
        if (passwordConf.isEmpty()) {
            passwordConfirmTxt.setError("Please confirm password");
            passwordConfirmTxt.requestFocus();
        }
        if (!(password.equals(passwordConf))) {
            passwordConfirmTxt.setError("Passwords do not match");
            passwordConfirmTxt.requestFocus();
        }
    }

    /**
     * Method attempts to sign up for a new Firebase account using given credentials,
     * changes pages and logs user in if successful
     *
     * @param firstName    - user's first name
     * @param lastName     - user's last name
     * @param email        - user's email address
     * @param password     - user's password
     * @param passwordConf - repetition of password, for confirmation
     */
    public void signUp(String firstName, String lastName, String email, String password, String passwordConf) {

        //validate sign up information
        if (validSignUpInfo(firstName, lastName, email, password, passwordConf)) {

            //final variables for user profile updating
            final String name = (firstName + " " + lastName);

            //run create user function for firebase with given email and password
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, task -> {

                //if sign up is unsuccessful, display error message
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Sign up unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                }

                //if sign up is successful, add first/last names and go to welcome screen
                else {

                    //get user and attempt to update profile with first/last name
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task1 -> {

                                //if name addition is unsuccessful, give toast and retry
                                if (!(task1.isSuccessful())) {
                                    Toast.makeText(SignUp.this, "Profile first/last name update error. Please try again.", Toast.LENGTH_SHORT).show();

                                    //if successful, all items in profile have been added, move on to welcome page
                                } else {
                                    Intent i = new Intent(SignUp.this, Welcome.class);
                                    startActivity(i);
                                }
                            });
                }
            });

            //if there are input problems
        } else {
            errorDisplays(firstName, lastName, email, password, passwordConf);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, SignIn.class);
        startActivity(i);
    }

}