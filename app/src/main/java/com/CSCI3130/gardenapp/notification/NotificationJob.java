package com.CSCI3130.gardenapp.notification;

import android.Manifest;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.db.TaskDatabase;
import com.CSCI3130.gardenapp.db.DatabaseAuth;
import com.CSCI3130.gardenapp.task_view.ActiveTaskListContext;
import com.CSCI3130.gardenapp.task_actions.TaskDetailInfo;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Notification Job that reads the database and fires alerts based on defined criteria
 *
 * @author Liam Hebert
 */
public class NotificationJob extends JobService {
    protected static Map<Integer, Task> sentNotifications = new HashMap<>();
    protected static TaskDatabase db = new TaskDatabase();
    private static int count = 0;
    private Location currLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    static {
        db.setDbRead(ActiveTaskListContext.MY_TASKS);
    }

    String CHANNEL_ID = "3130";
    private NotificationManagerCompat notificationManager;

    /**
     * Entry point for the job, checking database for valid alerts based on criteria and firing alerts
     *
     * @param params parameters that called this task
     * @return true since we want this task to be rescheduled in the future
     * @see JobService
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        notificationManager = NotificationManagerCompat.from(getApplicationContext());

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

        db.getDbRead().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshotTask : dataSnapshot.getChildren()) {
                            Task task = dataSnapshotTask.getValue(Task.class);
                            if (task != null) {
                                if (checkValidDueDateAlert(task)) {
                                    sendDueDateAlert(task);
                                } else if (checkValidWeatherAlert(task)) {
                                    sendWeatherAlert(task);
                                }
                            }
                        }
                        jobFinished(params, true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        jobFinished(params, true);
                    }
                });
        NotificationGuardian.scheduleNotificationJob(getApplicationContext());
        return true;
    }

    /**
     * Checks if an alert satisfies the Due Date Alert criteria. Currently set to same day
     *
     * @param task Task to verify
     * @return boolean value if condition is satisfied
     */
    protected boolean checkValidDueDateAlert(Task task) {
        long days = TimeUnit.MILLISECONDS.toDays(task.getDateDue());
        long currentDay = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());
        return days == currentDay && task.getDateCompleted() == -1 && !sentNotifications.containsValue(task) && task.getWeatherTrigger() == WeatherCondition.NONE;
    }

    private void sendDueDateAlert(Task task) {
        count++;
        notificationManager.cancel(count);
        sentNotifications.put(count, task);

        Intent intent = new Intent(getApplicationContext(), TaskDetailInfo.class);
        intent.putExtra(getString(R.string.task_extra), task);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), count, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(task.getName() + getString(R.string.duedate_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(task.getDescription()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager.notify(count, builder.build());
    }

    /**
     * Checks if an alert satisfies the Weather Alert criteria. Currently set to checking the current weather condition and matching
     *
     * @param task Task to verify
     * @return boolean value if condition is satisfied
     */
    protected boolean checkValidWeatherAlert(Task task) {
        WeatherCondition taskCondition = task.getWeatherTrigger();
        return taskCondition != WeatherCondition.NONE && CurrentWeather.currentWeatherList.contains(taskCondition) && !sentNotifications.containsValue(task);
    }

    private void sendWeatherAlert(Task task) {
        count++;
        notificationManager.cancel(count);
        sentNotifications.put(count, task);

        Intent intent = new Intent(getApplicationContext(), TaskDetailInfo.class);
        intent.putExtra(getString(R.string.task_extra), task);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), count, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(task.getName() + getString(R.string.weather_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("It is currently " + task.getWeatherTrigger().name() + "."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager.notify(count, builder.build());
    }

    /**
     * Checks if an alert satisfies the Location Alert criteria. Currently set to comparing the user's current location and the location of the task
     *
     * @param task Task to verify
     * @return boolean value if condition is satisfied
     */
    protected boolean checkValidLocationAlert(Task task) {
        if (DatabaseAuth.getCurrentUser().getId().equals(task.getUser()) || task.getUser().equals("")) {//if open or assigned task
            //check user perms
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false; //if location perms not given do not send notification
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {//get user's location
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currLocation = location;
                    }
                }
            });

            //get lat and long values from task object
            double lat = Double.parseDouble((task.getLocation().split(",")[0]));
            double lng = Double.parseDouble((task.getLocation().split(" ")[1]));

            //if user's location is the same as the task's
            if (currLocation.getLongitude() == lng && currLocation.getLatitude() == lat) {
                return true;//send notification
            }
            else {
                return false;//do not send notification
            }
        }
        else {//if task is assigned to someone else
            return false;//do not send notification
        }
    }

    private void sendLocationAlert(Task task) {
        count++;
        notificationManager.cancel(count);
        sentNotifications.put(count, task);

        Intent intent = new Intent(getApplicationContext(), TaskDetailInfo.class);
        intent.putExtra(getString(R.string.task_extra), task);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), count, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentTitle(getString(R.string.location_title) + task.getName())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(task.getDescription()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager.notify(count, builder.build());
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
