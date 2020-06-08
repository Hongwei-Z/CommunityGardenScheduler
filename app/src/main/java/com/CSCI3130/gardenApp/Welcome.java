package com.CSCI3130.gardenApp;

//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//welcome screen class
public class Welcome extends AppCompatActivity {

    //UI element declarations
    TextView welcomeTxt;

    //firebase authentication object and state listener
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //retrieve current instance of firebase authentication object
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    //sign out button onClick function (wrapper)
    public void signOut_onclick(View v){
        signOut();
    }

    //sign out function
    public void signOut(){
        //sign out
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Welcome.this,"Signed Out.",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Welcome.this, SignIn.class);
        startActivity(i);
        //check if anyone is signed in and act accordingly
        checkLoginState();
    }

    //check if anyone is logged in
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
