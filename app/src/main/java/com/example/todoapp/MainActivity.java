package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.ui.InputActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Directly go to InputActivity
        Intent intent = new Intent(MainActivity.this, InputActivity.class);
        startActivity(intent);
        finish();
    }
}
