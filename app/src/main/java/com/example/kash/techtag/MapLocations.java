package com.example.kash.techtag;

import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;

import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.maps.model.LatLngBounds.*;

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

public class MapLocations extends BaseActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    long volumeUpPress = -1;
    long volumeDownPress = -1;

    Marker molineMarker;
    Marker sydneyMarker;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;

    private static final String TAG = "MAPS_ACTIVITY";
    boolean mRequestingLocationUpdates;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    String currentUserEmail;
    String currentUserUID;
    String enteredCode;
    private static final String GROUPS = "groups";
    private static final String STATUS = "status";
    private static final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_locations);

        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        enteredCode = getIntent().getStringExtra("GROUP_CODE");

        mRequestingLocationUpdates = false;

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // To set multiple markers (simulating when a person is moving)
        /*
        MarkerOptions tempTest = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Marker in Sydney");

        int tempTestLatitude = -34;
        int[] tempTestLongitude = {140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160};

        for (int i = 0; i < tempTestLongitude.length; i++) {
            tempTest.position(new LatLng(tempTestLatitude, tempTestLongitude[i]));
        }
        */

        ArrayList<Marker> mapMarkers = new ArrayList<>();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        /*Marker*/
        sydneyMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Sydney"));

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng moline = new LatLng(41.5067, -90.5151);
        /*Marker*/
        molineMarker = mMap.addMarker(new MarkerOptions()
                .position(moline)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Moline"));

        mapMarkers.add(sydneyMarker);
        mapMarkers.add(molineMarker);

        Builder builder = new Builder();
        for (Marker marker : mapMarkers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
    }

    /**
     * As of 2017 April 20 testing, this method works.
     * Player uses the volume up and down buttons to tag another
     * player by pressing the volume up button and then the
     * volume down button within 750 milliseconds.
     * If volumeUpPress has not been clicked or the difference
     * between volumeUpPress or volumeDownPress, the two
     * variables get reset to their original values of -1.
     * @param keyCode   The keycode of the button being pressed
     * @param event     The event that the button is undergoing
     * @return True when one of the keys listed.
     *          The return of the original function that is
     *          being overridden.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                volumeUpPress = System.currentTimeMillis();
                Log.d("onKeyDown", "found " + volumeUpPress);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                volumeDownPress = System.currentTimeMillis();
                if ((volumeUpPress != -1) && (volumeDownPress - volumeUpPress <= 750)) {
                    // --------------------------- //
                    // Tag the other person method //
                    // --------------------------- //
                } else {
                    volumeUpPress = -1;
                    volumeDownPress = -1;
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onLocationChanged(Location location) {
        //mDatabase.child("groups").child()
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mRequestingLocationUpdates) {
            Log.i(TAG, "in onConnected(), starting location updates");
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void startLocationUpdates() {

    }

    private void updateFirebase() {

        // User myUser = new User (currentUserEmail, currentUserUID, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
                // mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).child("tagged"));

        // mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).setValue(myUser);
    }

    private void snapshotListener() {
        mDatabase.child(GROUPS).child(enteredCode).child(USERS).child(currentUserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
