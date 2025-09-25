package com.example.todoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.adapter.TaskAdapter;
import com.example.todoapp.db.DatabaseHelper;
import com.example.todoapp.model.Task;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnMetrics;
    private DatabaseHelper db;
    private TaskAdapter adapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        btnMetrics = findViewById(R.id.btnMetrics);

        taskList = db.getAllTasks();
        adapter = new TaskAdapter(this, taskList, db);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnMetrics.setOnClickListener(v -> {
            startActivity(new Intent(ListActivity.this, MetricsActivity.class));
        });
    }
}
