package com.example.todoapp.ui;

// Android imports
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// App imports
import com.example.todoapp.R;
import com.example.todoapp.adapter.TaskAdapter;
import com.example.todoapp.db.DatabaseHelper;
import com.example.todoapp.model.Task;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    // UI elements
    private RecyclerView recyclerView; // RecyclerView to display list of tasks
    private Button btnMetrics;         // Button to view metrics

    // Helpers & data
    private DatabaseHelper db;         // Handles database operations
    private TaskAdapter adapter;       // Adapter for RecyclerView
    private List<Task> taskList;       // List of tasks from DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.activity_list);

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerView); // RecyclerView from layout
        btnMetrics = findViewById(R.id.btnMetrics);     // Button from layout

        // Fetch all tasks from database
        taskList = db.getAllTasks();

        // Initialize adapter with task list and database helper
        adapter = new TaskAdapter(this, taskList, db);

        // Set layout manager for RecyclerView (vertical list)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Attach adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Set click listener for Metrics button
        btnMetrics.setOnClickListener(v -> {
            // Start MetricsActivity when button is clicked
            startActivity(new Intent(ListActivity.this, MetricsActivity.class));
        });
    }
}
