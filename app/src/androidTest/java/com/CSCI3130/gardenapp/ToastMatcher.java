package com.CSCI3130.gardenapp;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

//This code has been adapted from http://www.qaautomated.com/2016/01/how-to-test-toast-message-using-espresso.html
//it is used for espresso testing toast displays
public class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("Toast");
    }

    @Override
    public boolean matchesSafely(Root rt) {

        //gets window parameter type
        int type = rt.getWindowLayoutParams().get().type;

        //checks if a a toast is present and whether its token matches that of the app
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            IBinder windowToken = rt.getDecorView().getWindowToken();
            IBinder appToken = rt.getDecorView().getApplicationWindowToken();
            return windowToken == appToken;
        }
        return false;
    }
}
