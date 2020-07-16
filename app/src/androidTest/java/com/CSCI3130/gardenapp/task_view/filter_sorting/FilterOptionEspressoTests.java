package com.CSCI3130.gardenapp.task_view.filter_sorting;

import android.widget.DatePicker;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import com.CSCI3130.gardenapp.R;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

public class FilterOptionEspressoTests {

    @Rule
    public IntentsTestRule<FilterPopUp> activityScenarioRule
            = new IntentsTestRule<>(FilterPopUp.class);

    //tests if start date is displayed correctly on button
    @Test
    public void test_startDate_display() {
        onView(ViewMatchers.withId(R.id.startDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 25));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.startDateButton))
                .check(matches(withText("25-08-2020")));
    }

    //tests if end date is displayed correctly on button
    @Test
    public void test_endDate_display() {
        onView(withId(R.id.endDateButton)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 8, 25));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.endDateButton))
                .check(matches(withText("25-08-2020")));
    }


}
