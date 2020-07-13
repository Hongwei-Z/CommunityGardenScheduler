package com.CSCI3130.gardenapp.util.data;

import com.CSCI3130.gardenapp.util.TaskRepeatCondition;
import com.CSCI3130.gardenapp.util.db.DatabaseAuth;

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
                "Location-" + count,
                trigger,
                -1,
                TaskRepeatCondition.REPEAT_NONE);
    }

    public static Task generateTask(boolean currentUser, long dueDate) {
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
                "Location-" + count,
                WeatherCondition.NONE,
                dueDate,
                TaskRepeatCondition.REPEAT_NONE);
    }

    public static Task generateTask(boolean currentUser, TaskRepeatCondition repeat) {
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
                "Location-" + count,
                WeatherCondition.NONE,
                -1,
                repeat);
    }

    public static Task generateTask(boolean currentUser) {
        return generateTask(currentUser, WeatherCondition.NONE);
    }
}
