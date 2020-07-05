package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.db.DatabaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity class which welcomes the user after they have signed in/signed up for the app,
 * also allowing them to log out of the app
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
    public void allTaskListOpen_onclick(View v){
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", "allTasks");
        startActivity(i);
    }

    /**
     * Opens the open taskList activity on button click
     * @param v view object of the View executing the onclick
     */
    public void openTaskListOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", "openTasks");
        startActivity(i);
    }

    /**
     * Opens user taskList activity on button click
     * @param v view object of the View executing the onclick
     */
    public void myTaskListOpen_onclick(View v){
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", "myTasks");
        startActivity(i);
    }

    /**
     * Opens task history taskList activity on button click
     * @param v view object of the View executing the onclick
     */
    public void taskHistoryListOpen_onclick(View v){
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", "taskHistory");
        startActivity(i);
    }

    public void createTaskOpen_onclick(View v){
        Intent i = new Intent(Welcome.this, CreateTaskActivity.class);
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
        mFirebaseAuth.addAuthStateListener(checkLoginState());
        DatabaseAuth.signOut();
    }

    /**
     * Method checks if there is a user currently logged in on the app
     * and changes activities accordingly
     */
    public FirebaseAuth.AuthStateListener checkLoginState(){
        return firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            //if logged in still, display error
            if( mFirebaseUser != null ){
                Toast.makeText(Welcome.this,"Sign Out Unsuccessful.",Toast.LENGTH_SHORT).show();
            }

            //if not logged in, go back to login screen
            else{
                Toast.makeText(Welcome.this,"Signed Out123.",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Welcome.this, SignIn.class);
                startActivity(i);
            }
        };
    }


}
