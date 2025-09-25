package com.example.todoapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.db.DatabaseHelper;

public class MetricsActivity extends AppCompatActivity {

    private TextView txtPending, txtCompleted;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);

        db = new DatabaseHelper(this);

        txtPending = findViewById(R.id.txtPending);
        txtCompleted = findViewById(R.id.txtCompleted);

        int pending = db.getCountByStatus(0);
        int completed = db.getCountByStatus(1);

        txtPending.setText("Pending: " + pending);
        txtCompleted.setText("Completed: " + completed);
    }
}
