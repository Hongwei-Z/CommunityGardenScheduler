package com.CSCI3130.gardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherTestActivity extends AppCompatActivity {

    TextView tempTxt, descTxt, cityTxt;

    private String temperature;
    private String description;
    private String city;
    private String humidity;
    private String windSpeed;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_test);

        tempTxt = (TextView) findViewById(R.id.tempTxt);
        descTxt = (TextView) findViewById(R.id.descTxt);
        cityTxt = (TextView) findViewById(R.id.cityTxt);

        mQueue = Volley.newRequestQueue(this);

        //get weather for Halifax
        getCurrentWeatherFromCoordinates(44.648766, -63.575237);
    }

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
                            description = object.getString("description");
                            city = response.getString("name");
                            humidity = main_object.getString("humidity");
                            JSONObject wind_object = response.getJSONObject("wind");
                            windSpeed = wind_object.getString("speed");

                            tempTxt.setText(temperature + " " + humidity);
                            descTxt.setText(description + " " + windSpeed);
                            cityTxt.setText(city);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(WeatherTestActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

        if (isRaining(description)){
            conditionList.add(WeatherCondition.RAIN);
        }
        if (isDry(humidity)){
            conditionList.add(WeatherCondition.DRY);
        }
        if (isCold(temperature)){
            conditionList.add(WeatherCondition.COLD);
        }
        if (isHot(temperature)){
            conditionList.add(WeatherCondition.HOT);
        }
        if (isWindy(windSpeed)){
            conditionList.add(WeatherCondition.WINDY);
        }

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
     * @param humidity - humidity percentage as string value
     * @return boolean
     */
    public boolean isDry(String humidity){
        double h = Double.parseDouble(humidity);
        return (h < 50);
    }

    /**
     * Returns whether it is cold outside to cause concern for plants, meaning frost can develop (<0 degrees Celsius)
     * @param temperature - Celsius temperature as string value
     * @return boolean
     */
    public boolean isCold(String temperature){
        double t = Double.parseDouble(temperature);
        return (t <= 0);
    }

    /**
     * Returns whether it is hot enough outside for concern about plants, meaning it is >29 degress Celsius
     * @param temperature - Celsius temperature as string value
     * @return boolean
     */
    public boolean isHot(String temperature){
        double t = Double.parseDouble(temperature);
        return (t >= 29);
    }

    /**
     * Returns whether it is windy enough outside to cause concern, meaning wind speed is >11 m/s
     * @param windSpeed -
     * @return boolean
     */
    public boolean isWindy(String windSpeed){
        double w = Double.parseDouble(windSpeed);
        return (w >= 11);
    }

    //get methods for weather data
    public String getTemperature(){ return temperature; }
    public String getDescription(){ return description; }
    public String getCity(){ return city; }
    public String getHumidity(){ return humidity; }
    public String getWindSpeed(){ return windSpeed; }

}