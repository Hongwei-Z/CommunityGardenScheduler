package com.CSCI3130.gardenapp.util.db;

import com.CSCI3130.gardenapp.util.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Global utility class for common authentication methods
 * @author Liam Hebert
 */
public class DatabaseAuth {
    /**
     * Retrieves the currently signed in Firebase User, mapped to our User object
     * @return Currently signed in User
     * @see User
     */
    public static User getCurrentUser() {
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null) {
            return new User(auth.getDisplayName(), auth.getEmail());
        }
        else{
            return new User("", "");
        }
    }

    /**
     * Signed the current user out of the system, strongly suggest going to SignIn screen when this is called
     */
    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
}
