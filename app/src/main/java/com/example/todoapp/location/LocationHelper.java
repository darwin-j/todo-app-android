package com.example.todoapp.location;

// Android imports
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

// Google Play Services location imports
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {

    // References to activity and UI elements
    private Activity activity;
    private Button btnLocation;   // Button to trigger location fetch
    private TextView txtLocation; // TextView to display location

    // Fused Location Provider for location services
    private FusedLocationProviderClient fusedLocationClient;

    // Request code for runtime location permission
    private static final int LOCATION_REQUEST_CODE = 101;

    // Constructor
    public LocationHelper(Activity activity, Button btnLocation, TextView txtLocation) {
        this.activity = activity;
        this.btnLocation = btnLocation;
        this.txtLocation = txtLocation;

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        // Setup button click listener
        setupListener();
    }

    // Attach click listener to the location button
    private void setupListener() {
        btnLocation.setOnClickListener(v -> getLocation()); // Calls getLocation() on click
    }

    // Method to fetch the last known location
    private void getLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request permission if not granted
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
            return;
        }

        // Permissions granted → fetch last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Location fetched successfully → display latitude & longitude
                            String loc = "Lat: " + location.getLatitude() +
                                    ", Lng: " + location.getLongitude();
                            txtLocation.setText(loc);
                        } else {
                            // Location unavailable
                            txtLocation.setText("Unable to fetch location");
                        }
                    }
                })
                .addOnFailureListener(e ->
                        // Handle errors
                        Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Handle the result of location permission request
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // If permission granted → fetch location
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

}
