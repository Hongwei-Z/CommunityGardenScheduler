package com.CSCI3130.gardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI3130.gardenapp.create_task.CreateTaskActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity class which welcomes the user after they have signed in/signed up for the app,
 * also allowing them to log out of the app
 * @author Arjav Gupta
 */
public class Welcome extends AppCompatActivity {

    //UI element declarations
    TextView weatherCityTxt, weatherDescTxt, weatherTempTxt, weatherHumidTxt, weatherWindTxt;

    //weather variables
    private String temperature;
    private String description;
    private String city;
    private String humidity;
    private String windSpeed;

    private RequestQueue mQueue;

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

        mQueue = Volley.newRequestQueue(this);

        weatherCityTxt = (TextView) findViewById(R.id.weatherCityTxt);
        weatherDescTxt = (TextView) findViewById(R.id.weatherDescTxt);
        weatherTempTxt = (TextView) findViewById(R.id.weatherTempTxt);
        weatherHumidTxt = (TextView) findViewById(R.id.weatherHumidTxt);
        weatherWindTxt = (TextView) findViewById(R.id.weatherWindTxt);

        //get weather for Halifax and save them for use in other classes
        getCurrentWeatherFromCoordinates(44.648766, -63.575237);

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
        mAuthStateListener = firebaseAuth -> {
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
        };
    }

    //weather management

    /**
     * Method gets weather JSON data from latitude and longitude coordinates
     * @param latitude - double, longitude of given location
     * @param longitude - double, latitude of given location
     */
    public void getCurrentWeatherFromCoordinates(double latitude, double longitude){

        //weather api url with given coordinates
        String url = ("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=Metric&appid=47babba29fd7f383e46baa5083f06b44");

        //request json object using given url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main_object = response.getJSONObject("main");
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject object = array.getJSONObject(0);

                            temperature = String.valueOf(main_object.getDouble("temp"));
                            CurrentWeather.temperature = Double.parseDouble(temperature);
                            weatherTempTxt.setText("Temperature: " + CurrentWeather.temperature + " °C");

                            description = object.getString("description");
                            CurrentWeather.description = description;
                            weatherDescTxt.setText(CurrentWeather.description);

                            city = response.getString("name");
                            CurrentWeather.city = city;
                            weatherCityTxt.setText("Current weather in " + CurrentWeather.city + ":");

                            humidity = main_object.getString("humidity");
                            CurrentWeather.humidity = Double.parseDouble(humidity);
                            weatherHumidTxt.setText("Humidity: " + CurrentWeather.humidity + "%");

                            JSONObject wind_object = response.getJSONObject("wind");
                            windSpeed = wind_object.getString("speed");
                            CurrentWeather.windSpeed = Double.parseDouble(windSpeed);
                            weatherWindTxt.setText("Wind Speed: " + CurrentWeather.windSpeed + " m/s");

                            CurrentWeather.currentWeatherList = getWeatherConditions();

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });
        mQueue.add(jsonObjectRequest);

    }


    /**
     * Method determines weather conditions in arraylist of weather condition enums
     * @return ArrayList of weather condition enums
     */
    public ArrayList<WeatherCondition> getWeatherConditions(){
        ArrayList<WeatherCondition> conditionList = new ArrayList<WeatherCondition>();

        if (isRaining(CurrentWeather.description)){
            conditionList.add(WeatherCondition.RAIN);
        }
        if (isDry(CurrentWeather.humidity)){
            conditionList.add(WeatherCondition.DRY);
        }
        if (isCold(CurrentWeather.temperature)){
            conditionList.add(WeatherCondition.COLD);
        }
        if (isHot(CurrentWeather.temperature)){
            conditionList.add(WeatherCondition.HOT);
        }
        if (isWindy(CurrentWeather.windSpeed)){
            conditionList.add(WeatherCondition.WINDY);
        }

        CurrentWeather.currentWeatherList = conditionList;

        return conditionList;
    }


    /**
     * Returns whether it is raining or not based on text description of current weather
     * @param description - text description from weather JSON
     * @return boolean
     */
    public boolean isRaining(String description){
        return (description.contains("rain"));
    }

    /**
     * Returns whether it is dry enough outside to cause concern, meaning humidity is lower than 50%
     * @param humidity - humidity percentage
     * @return boolean
     */
    public boolean isDry(double humidity){
        return (humidity < 50);
    }

    /**
     * Returns whether it is cold outside to cause concern for plants, meaning frost can develop (<0 degrees Celsius)
     * @param temperature - Celsius temperature
     * @return boolean
     */
    public boolean isCold(double temperature){
        return (temperature <= 0);
    }

    /**
     * Returns whether it is hot enough outside for concern about plants, meaning it is >29 degress Celsius
     * @param temperature - Celsius temperature
     * @return boolean
     */
    public boolean isHot(double temperature){
        return (temperature >= 29);
    }

    /**
     * Returns whether it is windy enough outside to cause concern, meaning wind speed is >11 m/s
     * @param windSpeed -
     * @return boolean
     */
    public boolean isWindy(double windSpeed){
        return (windSpeed >= 11);
    }

    //get methods for weather data
    public String getTemperature(){ return temperature; }
    public String getDescription(){ return description; }
    public String getCity(){ return city; }
    public String getHumidity(){ return humidity; }
    public String getWindSpeed(){ return windSpeed; }


}
