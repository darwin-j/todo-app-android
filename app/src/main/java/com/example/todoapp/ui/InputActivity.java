package com.example.todoapp.ui;

// Android imports
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// Custom helper imports
import com.example.todoapp.location.LocationHelper;
import com.example.todoapp.NotificationHelper;
import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;

// Notification & permissions imports
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;

public class InputActivity extends AppCompatActivity {

    // UI elements
    private EditText editTask;
    private Button btnAdd, btnView, btnLocation;
    private TextView txtLocation;

    // Helpers
    private DatabaseHelper db;             // Handles DB operations for tasks
    private LocationHelper locationHelper; // Handles location fetching and display
    private NotificationHelper notificationHelper; // Handles showing notifications

    private int notificationIdCounter = 0; // Unique ID for each notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_input);

        // Initialize helpers
        db = new DatabaseHelper(this);               // DB helper for storing tasks
        notificationHelper = new NotificationHelper(this); // Notification helper

        // Initialize Task UI elements
        editTask = findViewById(R.id.editTask); // Input field for task text
        btnAdd = findViewById(R.id.btnAdd);     // Button to add a task
        btnView = findViewById(R.id.btnView);   // Button to view all tasks

        // Check runtime permission for notifications on Android 13+ (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission if not granted
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }

        // Set click listener for "Add Task" button
        btnAdd.setOnClickListener(v -> {
            String taskText = editTask.getText().toString(); // Get text from EditText
            if (!taskText.isEmpty()) { // Only add if text is not empty
                db.addTask(taskText);             // Add task to database
                editTask.setText("");             // Clear input field
                notificationHelper.showNotification(
                        "Task Added", taskText, notificationIdCounter++
                ); // Show notification
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show(); // Show toast
            }
        });

        // Set click listener for "View Tasks" button
        btnView.setOnClickListener(v ->
                startActivity(new Intent(InputActivity.this, ListActivity.class))
        ); // Opens ListActivity to show all tasks

        // Initialize Location UI elements
        btnLocation = findViewById(R.id.btnLocation); // Button to fetch location
        txtLocation = findViewById(R.id.txtLocation); // TextView to display location

        // Initialize location helper with button and TextView
        locationHelper = new LocationHelper(this, btnLocation, txtLocation);
    }

    // Handle runtime permission result for location or notifications
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Delegate permission result to LocationHelper
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Create a notification channel (required for Android 8.0+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+
            NotificationChannel channel = new NotificationChannel(
                    "todo_channel",                  // Channel ID
                    "ToDo Notifications",            // Channel name
                    NotificationManager.IMPORTANCE_DEFAULT // Importance level
            );
            channel.setDescription("Notifications for To-Do actions"); // Optional description

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel); // Register the channel
            }
        }
    }
}
