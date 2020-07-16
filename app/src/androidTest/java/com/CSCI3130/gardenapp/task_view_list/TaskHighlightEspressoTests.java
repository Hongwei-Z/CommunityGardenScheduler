package com.CSCI3130.gardenapp.task_view_list;

import android.util.Log;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import androidx.test.rule.ActivityTestRule;

import com.CSCI3130.gardenapp.R;

import java.util.concurrent.TimeUnit;


public class TaskHighlightEspressoTests {

    @Rule
    public ActivityTestRule<TaskViewList> activityScenarioRule = new ActivityTestRule<TaskViewList>(TaskViewList.class, true, false);

    @Test
    public void recyclerViewCardsContainImage() {
        onView(AllOf.allOf(ViewMatchers.withId(R.id.task_card), hasDescendant(withId(R.id.task_due_symbol))));
    }

    @Test
    public void testShown_One (){
        long nowdate = 1594869824409L;
        long date = 1592798031000L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(date - nowdate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
        } else if (diff_date == 2 || diff_date ==3 ){
            Log.i("K", "due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
        }
    }

    @Test
    public void testShown_Two (){
        long nowdate = 1594873362654L;
        long date = 1592798031000L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(date - nowdate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
        } else if (diff_date == 2 || diff_date ==3 ){
            Log.i("K", "due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
        }
    }
    @Test
    public void testShown_Three (){
        long nowdate = 1594873383633L;
        long date = 1592798031000L;
        long diff_date = TimeUnit.MILLISECONDS.toDays(date - nowdate);

        if (diff_date == 1) {
            Log.i("K", "due in one day");
        } else if (diff_date == 2 || diff_date ==3 ){
            Log.i("K", "due in two/three days");
        } else {
            Log.i("K", "Hide symbol");
        }
    }
}
