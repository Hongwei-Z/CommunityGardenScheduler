package com.CSCI3130.gardenapp.util.data;

import com.CSCI3130.gardenapp.db.DatabaseAuth;

public class TaskGenerator {
    private static int count = 0;

    public static Task generateTask(boolean currentUser, WeatherCondition trigger) {
        count++;
        String userID;
        if (currentUser) {
            userID = DatabaseAuth.getCurrentUser().getUsername();
        } else {
            userID = "";
        }
        return new Task(
                "Task-" + count,
                "This is a Description-" + count,
                1 + (count % 5), userID,
                "Location-" + count,
                trigger,
                System.currentTimeMillis(),
                "repeat-none");
    }

    public static Task generateTask(boolean currentUser) {
        return generateTask(currentUser, WeatherCondition.NONE);
    }
}
