package com.CSCI3130.gardenapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.CSCI3130.gardenapp.db.DatabaseAuth;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class WelcomeEspressoTests {
    @Rule
    public ActivityScenarioRule<Welcome> activityScenarioRule
            = new ActivityScenarioRule<>(Welcome.class);

    @Test
    public void testButtonClickable() {
        onView(withId(R.id.allTaskListBtn)).check(matches(isClickable()));
        onView(withId(R.id.myTasksBtn)).check(matches(isClickable()));
        onView(withId(R.id.openTasksBtn)).check(matches(isClickable()));
        onView(withId(R.id.taskHistoryBtn)).check(matches(isClickable()));
        onView(withId(R.id.signOutBtn_welcome2)).check(matches(isClickable()));
    }

    @Test
    public void testWeatherDisplay() throws InterruptedException {
        Thread.sleep(2000); //to allow the weather to be polled from the db
        onView(withId(R.id.weatherCityTxt)).check(matches(withText("Current weather in " + CurrentWeather.city + ":")));
        onView(withId(R.id.weatherDescTxt)).check(matches(withText(CurrentWeather.description)));
        onView(withId(R.id.weatherTempTxt)).check(matches(withText("Temperature: " + CurrentWeather.temperature + " °C")));
        onView(withId(R.id.weatherHumidTxt)).check(matches(withText("Humidity: " + CurrentWeather.humidity + "%")));
        onView(withId(R.id.weatherWindTxt)).check(matches(withText("Wind Speed: " + CurrentWeather.windSpeed + " m/s")));
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            onView(withId(R.id.welcome_title)).check(matches(withText("Welcome " + DatabaseAuth.getCurrentUser().getUsername() + " !")));
    }
}
