package com.example.kash.techtag;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Set;

public class SetupGPS extends AppCompatActivity {

    LocationManager service;
    boolean enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_gps);

        service = (LocationManager) getSystemService(LOCATION_SERVICE);

        enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (enabled) {
            Log.d("Enabled?", "Yes");
            Intent mainIntent = new Intent(SetupGPS.this, MainActivity.class);
            SetupGPS.this.startActivity(mainIntent);
        }

        setupButtons();
    }

    @Override
    protected void onResume() {
        if (enabled) {
            Log.d("Enabled?", "Yes");
            Intent mainIntent = new Intent(SetupGPS.this, MainActivity.class);
            SetupGPS.this.startActivity(mainIntent);
        }
        super.onResume();
    }

    private void setupButtons() {
        Button allow = (Button) findViewById(R.id.allow);
        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!enabled) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                SetupGPS.this.startActivity(settingsIntent);
            }
            }
        });

    }
}
