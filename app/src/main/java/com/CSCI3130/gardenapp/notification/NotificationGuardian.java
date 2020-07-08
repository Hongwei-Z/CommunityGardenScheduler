package com.CSCI3130.gardenapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.CSCI3130.gardenapp.R;

import java.util.concurrent.TimeUnit;

/**
 * Notification Scheduler that initializes on SYSTEM_BOOT or Welcome page.
 * @author Liam Hebert
 */
public class NotificationGuardian extends BroadcastReceiver {

    /**
     * Broadcast Receiver entry point, launches the scheduling loop
     * @param context Broadcast context
     * @param intent Broadcast intent
     */
    @Override
    public void onReceive(Context context, Intent intent){
        startScheduling(context);
    }

    /**
     * Entry point for notification scheduling, initialize the notification channel
     * @param context Android Context for activity, ideally application context
     */
    public static void startScheduling(Context context) {
        CharSequence name = context.getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        String CHANNEL_ID = "3130";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        scheduleNotificationJob(context);
    }

    /**
     * Schedule a new notification check, triggering the notification class
     * @param context Android Context for building the job
     */
    public static void scheduleNotificationJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, NotificationJob.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(TimeUnit.SECONDS.toMillis(15)); // wait at least
        builder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(30)); // maximum delay

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

}
