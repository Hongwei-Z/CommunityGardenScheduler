package com.CSCI3130.gardenapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.*;
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
    public void testWeatherDisplay() {
        onView(withId(R.id.weatherCityTxt)).check(matches(withText("Current weather in " + CurrentWeather.city + ":")));
        onView(withId(R.id.weatherDescTxt)).check(matches(withText(CurrentWeather.description)));
        onView(withId(R.id.weatherTempTxt)).check(matches(withText("Temperature: " + CurrentWeather.temperature + " °C")));
        onView(withId(R.id.weatherHumidTxt)).check(matches(withText("Humidity: " + CurrentWeather.humidity + "%")));
        onView(withId(R.id.weatherWindTxt)).check(matches(withText("Wind Speed: " + CurrentWeather.windSpeed + " m/s")));
        onView(withId(R.id.welcome_title)).check(matches(withText("Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " !")));
    }
}
