package com.CSCI3130.gardenapp;

import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeatherConditionUnitTests {

    Welcome w = new Welcome();

    @Test
    public void rain_test_true(){ assertTrue(w.isRaining("light rain")); }

    @Test
    public void rain_test_false(){ assertFalse(w.isRaining("there is no water falling from the sky")); }

    @Test
    public void cold_test_true(){ assertTrue(w.isCold(-50)); }

    @Test
    public void cold_test_false(){ assertFalse(w.isCold(50)); }

    @Test
    public void hot_test_true(){ assertTrue(w.isHot(50)); }

    @Test
    public void hot_test_false(){ assertFalse(w.isHot(-50)); }

    @Test
    public void dry_test_true(){ assertTrue(w.isDry(20)); }

    @Test
    public void dry_test_false(){ assertFalse(w.isDry(80));}

    @Test
    public void windy_test_true(){ assertTrue(w.isWindy(20)); }

    @Test
    public void windy_test_false(){ assertFalse(w.isWindy(0.5)); }

    @Test
    public void weatherConditionListRespondsToCurrWeather(){
        //for the sake of testing, the current weather condition is "dry"
        CurrentWeather.windSpeed = 0.5;
        CurrentWeather.humidity = 40; //dry condition
        CurrentWeather.temperature = 25;
        CurrentWeather.description = "there's no water falling from the sky and it's sunny";
        CurrentWeather.city = "Halifax";

        w.getWeatherConditions();
        ArrayList<WeatherCondition> compare = new ArrayList<WeatherCondition>();
        compare.add(WeatherCondition.DRY);
        assertTrue(CurrentWeather.currentWeatherList.equals(compare));
    }

}
