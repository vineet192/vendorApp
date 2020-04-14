package com.example.vendor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.PolylineData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static java.lang.Double.parseDouble;

public class current_order_detail_tracking extends AppCompatActivity implements LocationListener,
        OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private static final String TAG = "current_order_detail_tr";
    private static final String fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int location_permission_request_code = 1234;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int DEFAULT_ZOOM = 5;
    private static final String KEY_VENDOR_LAT = "vendor_lat";
    private static final String KEY_VENDOR_LONG = "vendor_long";

    private MapView mMapView;
    private TextView tripDuration;

    Location currentLocation;
    LocationManager locationManager;
    public GeoApiContext geoApiContext;
    GoogleMap mMap;

    SharedPreferences sharedPreferences;

    String vendorLat;
    String vendorLong;

    private ArrayList<PolylineData> polylineData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail_tracking);

        geoApiContext = new GeoApiContext
                .Builder()
                .apiKey("AIzaSyB7URlD8s2pt2MjUIM4e6C2nL2-5XhKDqo")
                .build();

        mMapView = findViewById(R.id.current_Order_tracking_map);
        tripDuration = findViewById(R.id.current_Order_tracking_tripDuration);

        getLocationPermission();
        getDeviceLocation();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(KEY_VENDOR_LAT)) {
            vendorLat = sharedPreferences.getString(KEY_VENDOR_LAT, "vendor_lat").trim();
        }
        if (sharedPreferences.contains(KEY_VENDOR_LONG)){
            vendorLong = sharedPreferences.getString(KEY_VENDOR_LONG, "vendor_long").trim();
        }

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(false);

        LatLng destination = new LatLng(Double.parseDouble(vendorLat), Double.parseDouble(vendorLong));
        calculateDirections(destination);

        mMap.setOnPolylineClickListener(this);

    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), fine_location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), coarse_location) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: called");

        switch (requestCode) {
            case location_permission_request_code: {

                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                }

            }
        }

    }

    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: getting device location");
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 2, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, this);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, "getLocation: " + e.getMessage());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        currentLocation = location;
        Log.d("onLocationChanged", "onLocationChanged: " + currentLocation.getLatitude());
        LatLng latLng = new LatLng(25.124578, 87.235689);
        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));*/
        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);*/

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "onProviderDisabled: Please Enable the GPS and Network", Toast.LENGTH_SHORT).show();
    }

    private void calculateDirections(LatLng latLngDestination) {

        Log.d(TAG, "calculateDirections: Calculating Direction");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(latLngDestination.latitude,
                latLngDestination.longitude);

        DirectionsApiRequest directions = new DirectionsApiRequest(geoApiContext);
        directions.alternatives(true);
        directions.origin(new com.google.maps.model.LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

        Log.d(TAG, "calculateDirections: " + destination.toString());

        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {

                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: geoCodedWayPoint: " + result.geocodedWaypoints.toString());
                addPolyLinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get direction" + e.getMessage());
            }
        });
    }

    private void addPolyLinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);


                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for (com.google.maps.model.LatLng latLng : decodedPath) {

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(current_order_detail_tracking.this, R.color.darkGrey));
                    polyline.setClickable(true);
                    polylineData.add(new PolylineData(polyline, route.legs[0]));

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPolylineClick(Polyline polyline) {

        for (PolylineData mPolyLinesData : polylineData) {
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if (polyline.getId().equals(mPolyLinesData.getPolyline().getId())) {
                mPolyLinesData.getPolyline().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mPolyLinesData.getPolyline().setZIndex(1);

                tripDuration.setText("Trip Duration : " + mPolyLinesData.getLeg().duration);
            } else {
                mPolyLinesData.getPolyline().setColor(ContextCompat.getColor(this, R.color.darkGrey));
                mPolyLinesData.getPolyline().setZIndex(0);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
