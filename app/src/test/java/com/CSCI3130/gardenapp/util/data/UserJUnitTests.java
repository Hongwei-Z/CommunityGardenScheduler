package com.CSCI3130.gardenapp.util.data;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UserJUnitTests {
    @Test
    public void userTaskAssignment() {
        Task task = new Task("Cover tomatoes", "cover if raining", 4, "", "", System.currentTimeMillis(), "repeat-none");
        User user = new User("Logan Sutherland", "sutherland@dal.ca", "L0GAN");
        user.addTask(task);
        task.setUser(user.getUsername());

        assertTrue(user.isAssignedTo(task));
    }
}
