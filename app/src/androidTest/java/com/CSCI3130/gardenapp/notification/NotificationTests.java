package com.CSCI3130.gardenapp.notification;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.*;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.Welcome;
import com.CSCI3130.gardenapp.util.DateFormatUtils;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.TaskGenerator;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskTestDatabase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class NotificationTests {
    @Rule
    public IntentsTestRule<Welcome> rule = new IntentsTestRule<>(Welcome.class, false, false);

    UiDevice mDevice;
    Task task;

    @Before
    public void setUp() throws InterruptedException {
        task = TaskGenerator.generateTask(true);
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        rule.launchActivity(null);
        Thread.sleep(5000);
        NotificationJob.db = new TaskTestDatabase();
        CurrentWeather.currentWeatherList.add(WeatherCondition.HOT);
    }


    @Test
    /*
     * Testing that a due date alert fires appropriately
     */
    public void testValidDueDateAlert() throws UiObjectNotFoundException {
        task.setDateDue(System.currentTimeMillis());
        NotificationJob.db.uploadTask(task);
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(task.getName() + rule.getActivity().getString(R.string.duedate_title))), 30000);
        UiObject notification = mDevice.findObject(new UiSelector().text(task.getName() + rule.getActivity().getString(R.string.duedate_title)));
        notification.clickAndWaitForNewWindow();
        checkProperLoad(task, false);
    }

    @Test
    /*
     * Testing that a valid weather alert is sent
     */
    public void testValidWeatherAlert() throws UiObjectNotFoundException {
        task.setWeatherTrigger(WeatherCondition.HOT);
        NotificationJob.db.uploadTask(task);
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(task.getName() + rule.getActivity().getString(R.string.weather_title))), 30000);
        UiObject notification = mDevice.findObject(new UiSelector().text(task.getName() + rule.getActivity().getString(R.string.weather_title)));
        mDevice.findObject(new UiSelector().text("It is currently " + task.getWeatherTrigger().name() + "."));
        notification.clickAndWaitForNewWindow();
        checkProperLoad(task, true);
    }

    public void checkProperLoad(Task task, boolean weather) {
        onView(withId(R.id.taskTitle)).check(matches(withText(task.getName())));
        onView(withId(R.id.taskDescription)).check(matches(withText(task.getDescription())));
        onView(withId(R.id.taskLocation)).check(matches(withText("Location " + task.getLocation())));
        onView(withId(R.id.taskPriority)).check(matches(withText("" + task.getPriority())));
        if (!weather)
            onView(withId(R.id.taskDuedate)).check(matches(withText("Due on: " + DateFormatUtils.getDateFormatted(task.getDateDue()))));
    }

}
