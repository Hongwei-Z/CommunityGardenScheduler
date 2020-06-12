package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity class which welcomes the user after they have signed in/signed up for the app,
 * also allowowing them to log out of the app
 * @author Arjav Gupta
 */
public class Welcome extends AppCompatActivity {

    //UI element declarations
    TextView welcomeTxt;

    //firebase authentication object and state listener
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * onCreate method for initial activity setup
     * @param savedInstanceState - saved instance of app, autogenerated by android studio
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //retrieve current instance of firebase authentication object
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Opens the taskList activity on button click
     * @param v view object of the View executing the onclick
     */
    public void taskListOpen_onclick(View v){
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        startActivity(i);
    }

    //sign out button onClick function (wrapper)
    public void signOut_onclick(View v){
        signOut();
    }

    /**
     * Method attempts to sign out of Firebase account on app
     */
    public void signOut(){
        //sign out
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Welcome.this,"Signed Out.",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Welcome.this, SignIn.class);
        startActivity(i);
        //check if anyone is signed in and act accordingly
        checkLoginState();
    }

    /**
     * Method checks if there is a user currently logged in on the app
     * and changes activities accordingly
     */
    public void checkLoginState(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                //if logged in still, display error
                if( mFirebaseUser != null ){
                    Toast.makeText(Welcome.this,"Sign Out Unsuccessful.",Toast.LENGTH_SHORT).show();
                }

                //if not logged in, go back to login screen
                else{
                    Toast.makeText(Welcome.this,"Signed Out.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Welcome.this, SignIn.class);
                    startActivity(i);
                }
            }
        };
    }


}
