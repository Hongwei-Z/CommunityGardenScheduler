package com.CSCI3130.gardenapp.util.data;

import android.app.Activity;
import android.content.Context;
import com.CSCI3130.gardenapp.Welcome;
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
import java.util.Arrays;

/**
 * Current weather class - class full of static variables which hold the current weather conditions
 *
 * @author Arjav Gupta
 */
public class CurrentWeather {

    public static double temperature;
    public static String description;
    public static String city;
    public static double humidity;
    public static double windSpeed;
    public static ArrayList<WeatherCondition> currentWeatherList = new ArrayList<WeatherCondition>();
    public static ArrayList<CharSequence> spinnerWeatherConditions = new ArrayList<>(Arrays.asList("Rain", "Dry", "Cold", "Hot", "Windy"));
    private static RequestQueue mQueue;


    public static void setQueue(Context context){
        if (context != null)
            mQueue = Volley.newRequestQueue(context);
    }

    /**
     * Method gets weather JSON data from latitude and longitude coordinates
     *
     * @param latitude  - double, longitude of given location
     * @param longitude - double, latitude of given location
     */
    public static void getCurrentWeatherFromCoordinates(double latitude, double longitude, Welcome activity) {
        //weather api url with given coordinates
        String url = ("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=Metric&appid=47babba29fd7f383e46baa5083f06b44");

        //request json object using given url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONObject main_object = response.getJSONObject("main");
                        JSONArray array = response.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        CurrentWeather.temperature = Double.parseDouble(String.valueOf(main_object.getDouble("temp")));

                        CurrentWeather.description = object.getString("description");

                        CurrentWeather.city = response.getString("name");

                        CurrentWeather.humidity = Double.parseDouble(main_object.getString("humidity"));

                        JSONObject wind_object = response.getJSONObject("wind");

                        CurrentWeather.windSpeed = Double.parseDouble(wind_object.getString("speed"));

                        CurrentWeather.currentWeatherList = getWeatherConditions();
                        activity.setWeather();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    // TODO: Handle error
                });
        mQueue.add(jsonObjectRequest);
    }


    /**
     * Method determines weather conditions in arraylist of weather condition enums
     *
     * @return ArrayList of weather condition enums
     */
    public static ArrayList<WeatherCondition> getWeatherConditions() {
        ArrayList<WeatherCondition> conditionList = new ArrayList<WeatherCondition>();

        if (isRaining(CurrentWeather.description)) {
            conditionList.add(WeatherCondition.RAIN);
        }
        if (isDry(CurrentWeather.humidity)) {
            conditionList.add(WeatherCondition.DRY);
        }
        if (isCold(CurrentWeather.temperature)) {
            conditionList.add(WeatherCondition.COLD);
        }
        if (isHot(CurrentWeather.temperature)) {
            conditionList.add(WeatherCondition.HOT);
        }
        if (isWindy(CurrentWeather.windSpeed)) {
            conditionList.add(WeatherCondition.WINDY);
        }

        CurrentWeather.currentWeatherList = conditionList;

        return conditionList;
    }


    /**
     * Returns whether it is raining or not based on text description of current weather
     *
     * @param description - text description from weather JSON
     * @return boolean
     */
    public static boolean isRaining(String description) {
        return (description.contains("rain"));
    }

    /**
     * Returns whether it is dry enough outside to cause concern, meaning humidity is lower than 50%
     *
     * @param humidity - humidity percentage
     * @return boolean
     */
    public static boolean isDry(double humidity) {
        return (humidity < 50);
    }

    /**
     * Returns whether it is cold outside to cause concern for plants, meaning frost can develop (<0 degrees Celsius)
     *
     * @param temperature - Celsius temperature
     * @return boolean
     */
    public static boolean isCold(double temperature) {
        return (temperature <= 0);
    }

    /**
     * Returns whether it is hot enough outside for concern about plants, meaning it is >29 degress Celsius
     *
     * @param temperature - Celsius temperature
     * @return boolean
     */
    public static boolean isHot(double temperature) {
        return (temperature >= 29);
    }

    /**
     * Returns whether it is windy enough outside to cause concern, meaning wind speed is >11 m/s
     *
     * @param windSpeed -
     * @return boolean
     */
    public static boolean isWindy(double windSpeed) {
        return (windSpeed >= 11);
    }
}
