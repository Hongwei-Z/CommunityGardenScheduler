package com.CSCI3130.gardenapp.util.db;

import com.CSCI3130.gardenapp.util.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DatabaseAuth {
    public static User getCurrentUser() {
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null) {
            return new User(auth.getDisplayName(), auth.getEmail());
        }
        else{
            return new User("", "");
        }
    }
}
