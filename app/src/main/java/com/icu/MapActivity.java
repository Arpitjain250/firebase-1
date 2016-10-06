package com.icu;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.icu.database.Message;
import com.icu.database.MessageRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sitaram on 10/6/16.
 */

public class MapActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    private static final String FIREBASE_URL = "https://icuapp.firebaseio.com/";

    private Firebase firebase;
    private ValueEventListener fireBaseConnectionListener;
    private ChildEventListener mListener;
    private GoogleMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("MapView", "Map ready");
                map = googleMap;
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude))
                        .title("Marker")
                        .draggable(true)
                        .snippet("Location")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                checkPermissions();
            }
        });
        firebase = new Firebase(FIREBASE_URL).child("message");

        fireBaseConnectionListener = firebase.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Log.d("Map Activity", "Connected to Firebase");
                } else {
                    Log.d("Map Activity", "Disconnected from Firebase");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getMessages();
    }

    private void initMap() {
        try {
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void publishMessage(Message message) {
        firebase.push().setValue(message);
        MessageRepository.insertOrUpdate(this, message);
    }


    private void updateMap(Location location) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("Marker")
                .draggable(true)
                .snippet("Location")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
        map.animateCamera(cameraUpdate);
        Message message = new Message();
        message.setLatitude(location.getLatitude());
        message.setLongitude(location.getLongitude());
        message.setMessage("Location Published");
        message.setUserId(SharedPreferenceManager.getInstance(this).getString("userid"));
        publishMessage(message);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location changed", location.getAltitude() + location.getLongitude() + "");
        updateMap(location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ArrayList<String> permissons = new ArrayList<>();
            permissons.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissons.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            String[] items = permissons.toArray(new String[permissons.size()]);
            requestPermissions(items, 4);
        } else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            initMap();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.ACCESS_FINE_LOCATION:
                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                        initMap();
                        break;

                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            firebase.getRoot().child(".info/connected").removeEventListener(fireBaseConnectionListener);
            locationManager.removeUpdates(this);
            locationManager = null;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }


    DataListener dataListener = new DataListener() {
        @Override
        public void onMessageReceived(Message messages) {
            Log.d("Data received", messages.getMessage());
        }
    };

    private void getMessages() {
        new FirebaseDataAdapter(firebase.limit(20), Message.class, dataListener);

    }

}
