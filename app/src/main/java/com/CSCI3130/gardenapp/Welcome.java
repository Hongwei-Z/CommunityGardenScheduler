package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.CSCI3130.gardenapp.notification.NotificationGuardian;
import com.CSCI3130.gardenapp.task_view_list.ActiveTaskListContext;
import com.CSCI3130.gardenapp.task_view_list.TaskViewList;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.db.DatabaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity class which welcomes the user after they have signed in/signed up for the app,
 * also allowing them to log out of the app
 *
 * @author Arjav Gupta
 */
public class Welcome extends AppCompatActivity {

    //UI element declarations
    TextView weatherCityTxt, weatherDescTxt, weatherTempTxt, weatherHumidTxt, weatherWindTxt, welcome_title;
    //firebase authentication object and state listener
    FirebaseAuth mFirebaseAuth;
    //weather variables

    //ImageView display weather symbol
    ImageView weatherImg;

    /**
     * onCreate method for initial activity setup
     *
     * @param savedInstanceState - saved instance of app, autogenerated by android studio
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        NotificationGuardian.startScheduling(getApplicationContext());
        //retrieve current instance of firebase authentication object
        mFirebaseAuth = FirebaseAuth.getInstance();
        //get weather for Halifax and save them for use in other classes
        CurrentWeather.setQueue(getApplicationContext());
        CurrentWeather.getCurrentWeatherFromCoordinates(44.648766, -63.575237, this);

        weatherCityTxt = findViewById(R.id.weatherCityTxt);
        weatherDescTxt = findViewById(R.id.weatherDescTxt);
        weatherTempTxt = findViewById(R.id.weatherTempTxt);
        weatherHumidTxt = findViewById(R.id.weatherHumidTxt);
        weatherWindTxt = findViewById(R.id.weatherWindTxt);
        welcome_title = findViewById(R.id.welcome_title);
        weatherImg = findViewById(R.id.weatherimg);
    }

    /**
     * Listener method to update weather when called
     */
    public void setWeather() {
        weatherTempTxt.setText("Temperature: " + CurrentWeather.temperature + " °C");
        weatherDescTxt.setText(CurrentWeather.description);
        weatherCityTxt.setText("Current weather in " + CurrentWeather.city + ":");
        weatherHumidTxt.setText("Humidity: " + CurrentWeather.humidity + "%");
        weatherWindTxt.setText("Wind Speed: " + CurrentWeather.windSpeed + " m/s");
        welcome_title.setText("Welcome " + mFirebaseAuth.getCurrentUser().getDisplayName()+ " !");

        //Pick a weather symbol by current weather condition
        String weatherDesc = CurrentWeather.description;
        if (weatherDesc.equals("clear sky")){
            weatherImg.setImageResource(R.drawable.clear_sky);
        }
        else if (weatherDesc.equals("few clouds")){
            weatherImg.setImageResource(R.drawable.few_clouds);
        }
        else if (weatherDesc.equals("scattered clouds")){
            weatherImg.setImageResource(R.drawable.scattered_clouds);
        }
        else if (weatherDesc.equals("broken clouds")){
            weatherImg.setImageResource(R.drawable.broken_clouds);
        }
        else if (weatherDesc.equals("shower rain")){
            weatherImg.setImageResource(R.drawable.shower_rain);
        }
        else if (weatherDesc.contains("rain")){
            weatherImg.setImageResource(R.drawable.rain);
        }
        else if (weatherDesc.contains("thunderstorm")){
            weatherImg.setImageResource(R.drawable.thunderstorm);
        }
        else if (weatherDesc.contains("snow")){
            weatherImg.setImageResource(R.drawable.snow);
        }
        else if (weatherDesc.equals("mist")){
            weatherImg.setImageResource(R.drawable.mist);
        }
    }

    /**
     * Opens the taskList activity on button click
     *
     * @param v view object of the View executing the onclick
     */
    public void allTaskListOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", ActiveTaskListContext.ALL_TASKS);
        startActivity(i);
    }

    /**
     * Opens the open taskList activity on button click
     *
     * @param v view object of the View executing the onclick
     */
    public void openTaskListOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", ActiveTaskListContext.OPEN_TASKS);
        startActivity(i);
    }

    /**
     * Opens user taskList activity on button click
     *
     * @param v view object of the View executing the onclick
     */
    public void myTaskListOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", ActiveTaskListContext.MY_TASKS);
        startActivity(i);
    }

    /**
     * Opens task history taskList activity on button click
     *
     * @param v view object of the View executing the onclick
     */
    public void taskHistoryListOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, TaskViewList.class);
        i.putExtra("activeTaskListContext", ActiveTaskListContext.TASK_HISTORY);
        startActivity(i);
    }

    public void createTaskOpen_onclick(View v) {
        Intent i = new Intent(Welcome.this, CreateTaskActivity.class);
        startActivity(i);
    }

    //sign out button onClick function (wrapper)
    public void signOut_onclick(View v) {
        signOut();
    }

    /**
     * Method attempts to sign out of Firebase account on app
     */
    public void signOut() {
        //sign out
        mFirebaseAuth.addAuthStateListener(checkLoginState());
        DatabaseAuth.signOut();
    }

    /**
     * Method checks if there is a user currently logged in on the app
     * and changes activities accordingly
     */
    public FirebaseAuth.AuthStateListener checkLoginState() {
        return firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            //if logged in still, display error
            if (mFirebaseUser != null) {
                Toast.makeText(Welcome.this, "Sign Out Unsuccessful.", Toast.LENGTH_SHORT).show();
            }

            //if not logged in, go back to login screen
            else {
                Toast.makeText(Welcome.this, "Signed Out.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Welcome.this, SignIn.class);
                startActivity(i);
            }
        };
    }


}
