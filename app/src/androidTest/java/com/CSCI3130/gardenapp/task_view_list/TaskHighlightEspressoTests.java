package com.CSCI3130.gardenapp.task_view_list;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import com.CSCI3130.gardenapp.R;


public class TaskHighlightEspressoTests {

    @Rule
    public IntentsTestRule<TaskViewList> activityScenarioRule = new IntentsTestRule<>(TaskViewList.class, true, false);

    @Test
    public void recyclerViewCardsContainImage() {
        onView(AllOf.allOf(ViewMatchers.withId(R.id.task_card), hasDescendant(withId(R.id.task_due_symbol))));
    }
}

