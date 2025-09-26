package com.example.todoapp.ui;

// Android imports
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// App imports
import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;

public class MetricsActivity extends AppCompatActivity {

    // UI elements to show metrics
    private TextView txtPending;   // Shows number of pending tasks
    private TextView txtCompleted; // Shows number of completed tasks

    private DatabaseHelper db;     // Database helper to fetch task data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_metrics);

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Initialize TextViews from layout
        txtPending = findViewById(R.id.txtPending);
        txtCompleted = findViewById(R.id.txtCompleted);

        // Fetch count of tasks with status 0 (pending) and 1 (completed)
        int pending = db.getCountByStatus(0);
        int completed = db.getCountByStatus(1);

        // Display counts in the TextViews
        txtPending.setText("Pending: " + pending);
        txtCompleted.setText("Completed: " + completed);
    }
}
