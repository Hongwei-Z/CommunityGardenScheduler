package com.CSCI3130.gardenapp.util.data;

import com.CSCI3130.gardenapp.util.data.User;
import org.junit.Test;
import com.CSCI3130.gardenapp.util.data.Task;

import static org.junit.Assert.*;

public class UserJUnitTests {
    @Test
    public void userTaskAssignment() {
        Task task = new Task("Cover tomatoes", "cover if raining", 4, "", "", System.currentTimeMillis());
        User user = new User("Logan Sutherland", "sutherland@dal.ca");
        user.addTask(task);
        task.setUser(user.getUsername());

        assertTrue(user.isAssignedTo(task));
    }
}
