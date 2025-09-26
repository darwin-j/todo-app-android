package com.example.todoapp;

// Android imports
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

// Import the activity to start
import com.example.todoapp.ui.InputActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Directly navigate to InputActivity when the app starts
        Intent intent = new Intent(MainActivity.this, InputActivity.class);

        // Start the InputActivity
        startActivity(intent);

        // Finish MainActivity so the user cannot return to it
        // It acts as a launcher/redirect activity only
        finish();
    }
}
