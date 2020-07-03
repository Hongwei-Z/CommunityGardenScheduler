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

/**
 * Weather maanager class - handles weather requests and outputs information whenever needed
 * @author Arjav Gupta
 */
public class WeatherManager{

    private RequestQueue mQueue;

    private String temp;
    private String description;
    private String city;

    /**
     * Empty constructor for weather manager class
     */
    public WeatherManager(Context context) { mQueue = Volley.newRequestQueue(context);}


    /**
     * Method gets weather JSON data from latitude and longitude coordinates
     * @param latitude - double, longitude of given location
     * @param longitude - double, latitude of given location
     */
    public void getCurrentWeatherFromCoordinates(double latitude, double longitude){

        //weather api url with given coordinates
        String url = ("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=47babba29fd7f383e46baa5083f06b44");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main_object = response.getJSONObject("main");
                            JSONArray array = response.getJSONArray("weather");
                            JSONObject object = array.getJSONObject(0);
                            temp = String.valueOf(main_object.getDouble("temp"));
                            description = object.getString("description");
                            city = response.getString("name");


                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        temp = "error";
                    }
                });
        mQueue.add(jsonObjectRequest);
    }

    public String getTemperature(){ return temp; }
    public String getDescription(){ return description; }
    public String getCity(){ return city; }
}
