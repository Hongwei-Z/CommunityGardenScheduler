package com.CSCI3130.gardenapp.util.data;

import com.CSCI3130.gardenapp.util.db.DatabaseAuth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskGenerator {
    private static int count = 0;
    public static Task generateTask(boolean currentUser) {
        count++;
        String userID;
        if(currentUser) {
            userID = DatabaseAuth.getCurrentUser().getUsername();
        } else {
            userID = "TEST_USER-" + count;
        }
        return new Task(
                "Task-" + count,
                "This is a Description-" + count,
                1 + (count%5), userID,
                "Location-" + count,
                System.currentTimeMillis());
    }
}
