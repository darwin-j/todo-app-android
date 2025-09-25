package com.example.todoapp.ui;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.location.LocationHelper;
import com.example.todoapp.NotificationHelper;
import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;



public class InputActivity extends AppCompatActivity {

    private EditText editTask;
    private Button btnAdd, btnView, btnLocation;
    private TextView txtLocation;

    private DatabaseHelper db;
    private LocationHelper locationHelper;
    private NotificationHelper notificationHelper;

    private int notificationIdCounter = 0; // unique ID for each notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        db = new DatabaseHelper(this);
        notificationHelper = new NotificationHelper(this);

        // Task UI
        editTask = findViewById(R.id.editTask);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }



        btnAdd.setOnClickListener(v -> {
            String taskText = editTask.getText().toString();
            if (!taskText.isEmpty()) {
                db.addTask(taskText);
                editTask.setText("");
                notificationHelper.showNotification("Task Added", taskText, notificationIdCounter++);
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();  // <-- use 'this'
            }
        });

        btnView.setOnClickListener(v -> startActivity(new Intent(InputActivity.this, ListActivity.class)));

        // Location UI
        btnLocation = findViewById(R.id.btnLocation);
        txtLocation = findViewById(R.id.txtLocation);
        locationHelper = new LocationHelper(this, btnLocation, txtLocation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // API 26+
            NotificationChannel channel = new NotificationChannel(
                    "todo_channel",
                    "ToDo Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for To-Do actions");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }


}
