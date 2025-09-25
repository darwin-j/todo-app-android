package com.example.todoapp.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {

    private Activity activity;
    private Button btnLocation;
    private TextView txtLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_REQUEST_CODE = 101;

    public LocationHelper(Activity activity, Button btnLocation, TextView txtLocation) {
        this.activity = activity;
        this.btnLocation = btnLocation;
        this.txtLocation = txtLocation;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        setupListener();
    }

    private void setupListener() {
        btnLocation.setOnClickListener(v -> getLocation());
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            String loc = "Lat: " + location.getLatitude() +
                                    ", Lng: " + location.getLongitude();
                            txtLocation.setText(loc);
                        } else {
                            txtLocation.setText("Unable to fetch location");
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

}
