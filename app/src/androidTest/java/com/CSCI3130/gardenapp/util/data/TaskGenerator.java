package com.CSCI3130.gardenapp.util.data;

import com.CSCI3130.gardenapp.util.db.DatabaseAuth;
import com.google.android.gms.maps.model.LatLng;

public class TaskGenerator {
    private static int count = 0;
    public static Task generateTask(boolean currentUser, WeatherCondition trigger) {
        count++;
        String userID;
        if(currentUser) {
            userID = DatabaseAuth.getCurrentUser().getUsername();
        } else {
            userID = "";
        }
        return new Task(
                "Task-" + count,
                "This is a Description-" + count,
                1 + (count%5), userID,
                "44.6454, -63.5766",
                trigger,
                -1,
                "repeat-none");
    }

    public static Task generateTask(boolean currentUser, long dueDate ) {
        count++;
        String userID;
        if(currentUser) {
            userID = DatabaseAuth.getCurrentUser().getUsername();
        } else {
            userID = "";
        }
        return new Task(
                "Task-" + count,
                "This is a Description-" + count,
                1 + (count%5), userID,
                "44.6454, -63.5766",
                WeatherCondition.NONE,
                dueDate,
                "repeat-none");
    }

    public static Task generateTask(boolean currentUser, String repeat) {
        count++;
        String userID;
        if(currentUser) {
            userID = DatabaseAuth.getCurrentUser().getUsername();
        } else {
            userID = "";
        }
        return new Task(
                "Task-" + count,
                "This is a Description-" + count,
                1 + (count%5), userID,
                "44.6454, -63.5766",
                WeatherCondition.NONE,
                -1,
                repeat);
    }

    public static Task generateTask(boolean currentUser) {
        return generateTask(currentUser, WeatherCondition.NONE);
    }
}
