package com.CSCI3130.gardenapp;
import android.content.Context;

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

/**
 * Current weather class - class full of static variables which hold the current weather conditions
 * @author Arjav Gupta
 */
public class CurrentWeather {

    public static double temperature;
    public static String description;
    public static String city;
    public static double humidity;
    public static double windSpeed;
    public static ArrayList<WeatherCondition> currentWeatherList = new ArrayList<WeatherCondition>();

}
