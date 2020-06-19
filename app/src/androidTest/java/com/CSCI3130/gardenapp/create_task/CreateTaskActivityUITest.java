package com.CSCI3130.gardenapp.create_task;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.db.DatabaseTaskTestWriter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.junit.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.fail;


@LargeTest
public class CreateTaskActivityUITest {
    @Rule
    public ActivityTestRule<CreateTaskActivity> activityScenarioRule = new ActivityTestRule<CreateTaskActivity>(CreateTaskActivity.class, true, false);
    public DatabaseTaskTestWriter testDB;
    private CreateTaskActivity activity;
    @Before
    public void setUp(){
        activityScenarioRule.launchActivity(null);
        testDB = new DatabaseTaskTestWriter();
        activity = activityScenarioRule.getActivity();
        activity.db = testDB;
    }

    @After
    public void tearDown() {
        DatabaseTaskTestWriter db = (DatabaseTaskTestWriter) activity.db;
        db.clearDatabase();
    }

    private void ensureNoDatabaseActivity() {
        testDB.getDb().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    fail();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fail();
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void testCorrectInputs() throws InterruptedException {
        //neat inputs
        onView(ViewMatchers.withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("Success!")));

        final boolean[] flag = {false};
        testDB.getDb().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Assert.assertEquals(1, dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Task task = child.getValue(Task.class);
                    Assert.assertEquals(new Task("Liam Really Rocks!",
                            "This is but a short description describing how amazing and awesome Liam is!",
                            1,
                            "",
                            "Dalhousie University",
                            LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))), task);
                }
                flag[0] = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                fail();
            }
        });
        Thread.sleep(100);
        Assert.assertTrue(flag[0]);
    }

    @Test
    public void testMissingTitle() {
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingDescription() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingLocation() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.buttonPriority1)).perform(click());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        ensureNoDatabaseActivity();
    }

    @Test
    public void testMissingPriority() {
        onView(withId(R.id.editTitle)).perform(typeText("Liam Really Rocks!"), closeSoftKeyboard());
        onView(withId(R.id.editDescription)).perform(typeText("This is but a short description describing how amazing and awesome Liam is!"), closeSoftKeyboard());
        onView(withId(R.id.editLocation)).perform(typeText("Dalhousie University"), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmAdd)).perform(click());

        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
        ensureNoDatabaseActivity();
    }

    @Test
    public void testAllError() {
        onView(withId(R.id.buttonConfirmAdd)).perform(click());
        onView(withId(R.id.textErrorText)).check(matches(withText("Missing Priority")));
        onView(withId(R.id.editLocation)).check(matches(hasErrorText("Missing Location")));
        onView(withId(R.id.editDescription)).check(matches(hasErrorText("Missing Description")));
        onView(withId(R.id.editTitle)).check(matches(hasErrorText("Missing Title")));
        ensureNoDatabaseActivity();
    }
}
