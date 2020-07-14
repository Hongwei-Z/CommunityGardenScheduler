package com.CSCI3130.gardenapp.notification;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.CSCI3130.gardenapp.R;
import com.CSCI3130.gardenapp.TaskDetailInfo;
import com.CSCI3130.gardenapp.task_view_list.ActiveTaskListContext;
import com.CSCI3130.gardenapp.util.data.CurrentWeather;
import com.CSCI3130.gardenapp.util.data.Task;
import com.CSCI3130.gardenapp.util.data.WeatherCondition;
import com.CSCI3130.gardenapp.util.db.TaskDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Notification Job that reads the database and fires alerts based on defined criteria
 * @author Liam Hebert
 */
public class NotificationJob extends JobService {
    protected static Map<Integer, Task> sentNotifications = new HashMap<>();
    private static int count = 0;
    protected static TaskDatabase db = new TaskDatabase();
    static {
        db.setDbRead(ActiveTaskListContext.MY_TASKS);
    }

    private NotificationManagerCompat notificationManager;
    String CHANNEL_ID = "3130";

    /**
     * Entry point for the job, checking database for valid alerts based on criteria and firing alerts
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

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
