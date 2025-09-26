package com.example.todoapp;

// Android imports for notifications and permissions
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    // Notification channel constants
    private static final String CHANNEL_ID = "todo_channel";
    private static final String CHANNEL_NAME = "ToDo Notifications";
    private static final String CHANNEL_DESC = "Notifications for To-Do actions";

    private Context context; // Activity or application context

    // Constructor
    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel(); // Ensure the notification channel exists
    }

    // Create notification channel for Android 8.0+ (API 26+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Only for API 26+
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,          // Unique channel ID
                    CHANNEL_NAME,        // User-visible channel name
                    NotificationManager.IMPORTANCE_DEFAULT // Importance level
            );
            channel.setDescription(CHANNEL_DESC); // Channel description

            // Get system NotificationManager and create the channel
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Show a notification with given title, message, and unique ID
    public void showNotification(String title, String message, int id) {

        // Check permission for Android 13+ (POST_NOTIFICATIONS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return; // Skip showing notification if permission not granted
            }
        }

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Icon for notification
                .setContentTitle(title)    // Notification title
                .setContentText(message)   // Notification message
                .setAutoCancel(true);      // Dismiss notification on click

        // Show notification
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(id, builder.build()); // Use unique ID for each notification
    }

}
