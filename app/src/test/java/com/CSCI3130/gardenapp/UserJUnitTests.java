package com.CSCI3130.gardenapp;

import org.junit.Test;
import com.CSCI3130.gardenapp.User;
import com.CSCI3130.gardenapp.Task;

import static org.junit.Assert.*;

public class UserJUnitTests {
    @Test
    public void userTaskAssignment() {
        Task task = new Task("Cover tomatoes", "cover if raining", 4, "", "June 4th, 2020");
        User user = new User("Logan Sutherland", "sutherland@dal.ca");
        user.addTask(task);
        task.setUser(user.getUsername());

        assertTrue(user.isAssignedTo(task));
    }
}
