package com.CSCI3130.gardenapp;

import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

//activity allows user to sign in
public class SignIn extends AppCompatActivity {

    //UI element declarations
    EditText emailTxt, passwordTxt;
    Button logInBtn, signUpBtn;

    //firebase authentication object
    FirebaseAuth mFirebaseAuth;
    //state listener
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onStart() {
        super.onStart();

        //sets up authorization listener
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //UI element assignments
        emailTxt = (EditText) findViewById(R.id.emailTxt_signin);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt_signin);
        logInBtn = (Button) findViewById(R.id.signInBtn_signin);
        signUpBtn = (Button) findViewById(R.id.signUpBtn_signin);

        //get instance for firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        //check if anyone is logged in
        checkLoginState();
    }



    //check if anyone is logged in through this method
    public void checkLoginState(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                //if logged in, go to welcome screen
                if( mFirebaseUser != null ){
                    Toast.makeText(SignIn.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignIn.this, Welcome.class);
                    startActivity(i);
                }

            }
        };
    }

    //onclick function for log in button (wrapper)
    public void LogIn_onclick (View v){
        LogInFirebase();
    }

    //onclick function for sign up button
    public void SignUp_onclick (View v){
        //starts sign up activity
        Intent i = new Intent(SignIn.this, SignUp.class);
        startActivity(i);
    }

    //function checks that inputs are valid
    public boolean validInputs(String email, String pass){

        //email must not be empty and must have valid pattern
        if (email.isEmpty()){
            return false;
        }
        if (!email.isEmpty()){
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                return false;
            }
        }

        //check that password isn't empty
        if(pass.isEmpty()){
            return false;
        }
        //check that password is at least 6 characters long
        if (pass.length() < 6){
            return false;
        }

        //if both fields valid
        return true;
    }


    //displays errors based on enum
    public void errorDisplays(String email, String pass){

        //email must not be empty and must have valid pattern
        if (email.isEmpty()){
            emailTxt.setError("Please enter your email address");
            emailTxt.requestFocus();
        }
        if (!email.isEmpty()) {
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTxt.setError("Please enter a valid email address");
                emailTxt.requestFocus();
            }
        }

        //check that password isn't empty and is at least 6 characters long
        if(pass.isEmpty()){
            passwordTxt.setError("Please enter your password");
            passwordTxt.requestFocus();
        } else {
            if (pass.length() < 6){
                passwordTxt.setError("Invalid Password");
                passwordTxt.requestFocus();
            }
        }


    }

    //function logs into firebase using given credentials
    public void LogInFirebase(){

        //get text from email/password fields
        String email = emailTxt.getText().toString();
        String pass = passwordTxt.getText().toString();

        //if inputs are valid, try to sign in
        if (validInputs(email, pass)){

            //run signin method for firebase
            mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //if login is not successful, return error
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "Sign In Error. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }

                    //if login is successful, go to login page
                    else {
                        Toast.makeText(SignIn.this, "Signed in. Welcome!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignIn.this, Welcome.class);
                        startActivity(i);
                    }
                }
            });

        } else{
            errorDisplays(email, pass);
        }

    }




}
