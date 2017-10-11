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

/*
 * Copyright 2017  Koushhik Kumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
