package com.example.kash.techtag;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;

//public class MapLocations extends FragmentActivity implements OnMapReadyCallback {
public class MapLocations extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    long volumeUpPress = -1;
    long volumeDownPress = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_locations);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Marker sydneyMarker = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Sydney"));

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng moline = new LatLng(41.5067, -90.5151);
        Marker molineMarker = mMap.addMarker(new MarkerOptions()
                .position(moline)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Moline"));

        mapMarkers.add(sydneyMarker);
        mapMarkers.add(molineMarker);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mapMarkers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

    }

    /**
     * Player uses the volume up and down buttons to tag another
     * player by pressing the volume up button and then the
     * volume down button within 750 milliseconds.
     * If volumeUpPress has not been clicked or the difference
     * between volumeUpPress or volumeDownPress, the two
     * variables get reset to their original values of -1.
     * @param keyCode   The keycode of the button being pressed
     * @param event     The event that the button is undergoing
     * @return  True when one of the keys listed.
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

}
