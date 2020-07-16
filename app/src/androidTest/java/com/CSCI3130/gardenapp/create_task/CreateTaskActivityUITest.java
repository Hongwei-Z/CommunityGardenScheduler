package com.CSCI3130.gardenapp.create_task;

import android.location.Location;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;


@LargeTest
public class CreateTaskActivityUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public TaskTestDatabase testDB;
    private CreateTaskActivity activity;
    LatLng selectedLocation = new LatLng(44.64541, -63.57661);

    @Before
    public void setUp() {
        activityScenarioRule.launchActivity(null);
        testDB = new TaskTestDatabase();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        com.google.android.gms.tasks.Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    @After
    public void tearDown() {
        TaskTestDatabase db = (TaskTestDatabase) activity.db;
        db.clearDatabase();
    }

    private void clickCorrectPriority(int val) {
        int[] ids = {R.id.buttonPriority1, R.id.buttonPriority2, R.id.buttonPriority3, R.id.buttonPriority4, R.id.buttonPriority5};
        onView(withId(ids[val - 1])).perform(click());
    }

    @Test
    public void testCorrectInputs() throws InterruptedException {
        Task task = TaskGenerator.generateTask(false, System.currentTimeMillis() + 10000);
        //neat inputs
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        task.setLocation(String.format("%.5f", selectedLocation.latitude) + ", " + String.format("%.5f", selectedLocation.longitude));
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testMissingTitle() {
        Task task = TaskGenerator.generateTask(true);
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingDescription() {
        Task task = TaskGenerator.generateTask(true);
        //neat inputs
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testAllError() {
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        testDB.ensureNoDatabaseActivity();
    }

    @Test
    public void testWeatherSelected() {
        Task task = TaskGenerator.generateTask(false, WeatherCondition.HOT);
        onView(withId(R.id.weatherTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.weatherSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Hot"))).perform(click());
        task.setLocation(String.format("%.5f", selectedLocation.latitude) + ", " + String.format("%.5f", selectedLocation.longitude));
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testRepeatedSelected() {
        Task task = TaskGenerator.generateTask(false, "repeat-weekly");
        onView(withId(R.id.repeatTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.repeatSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Repeat every week"))).perform(click());
        task.setLocation(String.format("%.5f", selectedLocation.latitude) + ", " + String.format("%.5f", selectedLocation.longitude));
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));
        testDB.checkForTask(task);
    }

    @Test
    public void testDueDate() {
        Task task = TaskGenerator.generateTask(false, 1598350371000L);
        onView(withId(R.id.dateTypeButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(typeText(task.getName()), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText(task.getDescription()), closeSoftKeyboard());
        clickCorrectPriority(task.getPriority());
        onView(withId(R.id.dueDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 25));
        onView(withText("OK")).perform(click());
        task.setLocation(String.format("%.5f", selectedLocation.latitude) + ", " + String.format("%.5f", selectedLocation.longitude));
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        testDB.checkForTask(task);
    }

    @Test
    public void testConditionToggle() {
        onView(withId(R.id.dueDate)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.repeatTypeButton)).perform(click());
        onView(withId(R.id.repeatSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.dueDate)).check(matches(not(isDisplayed())));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.weatherTypeButton)).perform(click());
        onView(withId(R.id.weatherSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.dueDate)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));

        onView(withId(R.id.dateTypeButton)).perform(click());
        onView(withId(R.id.dueDate)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherSpinner)).check(matches(not(isDisplayed())));
        onView(withId(R.id.repeatSpinner)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testLocationMap() throws InterruptedException, UiObjectNotFoundException {
        Thread.sleep(2000);
        //check map is displayed
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.mapLayout)).check(matches(isDisplayed()));
        String locationText ="Location " + String.format("%.5f", selectedLocation.latitude) + ", " + String.format("%.5f", selectedLocation.longitude);

        // if successfully got location, check that is displayed, else check default
        onView(withId(R.id.locationText)).check(matches(withText(locationText)));
        Thread.sleep(3000);

        // drag map to new location and click to add new marker
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject map = device.findObject(new UiSelector().descriptionContains("Google Map"));
        map.dragTo(100, 100,  40);
        map.click();

        //ensure location has changed from previous value
        onView(withId(R.id.locationText)).check(matches(not(withText(locationText))));
    }
}
