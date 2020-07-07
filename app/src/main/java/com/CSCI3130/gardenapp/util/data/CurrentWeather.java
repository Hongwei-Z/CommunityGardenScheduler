package com.CSCI3130.gardenapp.util.data;

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
}
