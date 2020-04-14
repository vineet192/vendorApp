package com.example.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapters.ItemsAdapter;
import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.DeliveryBoyLocation;
import com.example.Models.MyItem;
import com.example.Models.ProductDescriptionResponse;
import com.example.Utility.LatLngInterpolator;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.maps.GeoApiContext;
import com.google.maps.android.SphericalUtil;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyTrackingfragment extends Fragment implements OnMapReadyCallback {

    private String TAG = "[DelBoyTraFra]";

    View v;
    String userlat, userlon;
    String deliveryboyname, deliveryboyphoneno;
    public static final int DEFAULT_ZOOM = 2;
    List<Polyline> tripspolylines = new ArrayList<>();
    private GoogleMap mMap;
    public View mapView;
    FirebaseFirestore mDb;
    public DeliveryBoyLocation deliveryBoyLocation;
    TextView tripduration, tripdistance;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    public GeoApiContext geoApiContext;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    ArrayList<Marker> deliveryroutemarkers = new ArrayList<>();
    private Marker mDeliveryBoysMarker;
    private ListenerRegistration listenerRegistrationForDeliveryBoysLocation;
    private boolean shouldTrackDeliveryBoy = false;

    public DeliveryBoyTrackingfragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.deliveryboytrackingfragmentlayout, container, false);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AAAAqU0wAQ0:APA91bHZQx1ocLDX4GHdo9v6kaNBQxBDUG0MrUhbMDQlDwM7Utl77fd1tQOpFCl56B3hON-2pwWDw2LYlfNjScSR9HC2q5HZbxcQ5uvP1_fZVv5hnBQb-FmCp815azSwpAnk0d7GnAz7")
                .setApplicationId("1:727144464653:android:9de0a8bb96a7ea230ef044")
                .setDatabaseUrl("https://techtest-96309.firebaseio.com")
                .setProjectId("techtest-96309")
                .build();
        FirebaseApp app = null;
        //List<FirebaseApp> firebaseAppList=FirebaseApp.getApps(getContext());
        if (FirebaseApp.getApps(getContext()).size() == 1) {
            app = FirebaseApp.initializeApp(getContext(), options, "track");
        } else {
            app = FirebaseApp.getInstance("track");
        }

        userlat = ((ProductsClient) getContext().getApplicationContext()).getUserlat();
        userlon = ((ProductsClient) getContext().getApplicationContext()).getUserlon();
        mDb = FirebaseFirestore.getInstance(app);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        geoApiContext = new GeoApiContext
                .Builder()
                .apiKey("AIzaSyC0QZoeVfd7W0ye_3GFeOnz1MU7nPSRDg0")
                .build();
        tripdistance = v.findViewById(R.id.totaldistance);
        tripduration = v.findViewById(R.id.tripduration);

        deliveryboyname = getArguments().getString("deliveryboyname");
        deliveryboyphoneno = getArguments().getString("deliveryboyphoneno");
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);
        mapView = supportMapFragment.getView();
        return v;
    }

//    private void startUserLocationsRunnable() {
////        mHandler.postDelayed(mRunnable = new Runnable() {
////            @Override
////            public void run() {
////                getDeliveryBoyLocation();
////                //runnablecallnum++;
////                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
////            }
////        }, LOCATION_UPDATE_INTERVAL);
//    }


//    void getDeliveryBoyLocation() {
//
//        DocumentReference locationRef = mDb.collection("DeliveryBoyLocation")
//                .document(deliveryboyphoneno);
//
//
//        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    deliveryBoyLocation = task.getResult().toObject(DeliveryBoyLocation.class);
//                    showDeliveryBoyLocation(deliveryBoyLocation);
//                }
//            }
//        });
//
//    }

    private void startObservingChangesOfDeliveryBoysLocation() {
        DocumentReference locationRef = mDb.collection("DeliveryBoyLocation")
                .document(deliveryboyphoneno);


        listenerRegistrationForDeliveryBoysLocation = locationRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    deliveryBoyLocation = snapshot.toObject(DeliveryBoyLocation.class);
                    showDeliveryBoyLocation(deliveryBoyLocation);
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void stopLocationUpdates() {
        listenerRegistrationForDeliveryBoysLocation.remove();
    }

    void showDeliveryBoyLocation(DeliveryBoyLocation deliveryboylocation) {
        for (int i = 0; i < deliveryroutemarkers.size(); i++) {
            deliveryroutemarkers.get(i).remove();
        }
        calculateDirections();


        if(mDeliveryBoysMarker == null) {
            mDeliveryBoysMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(deliveryboylocation.getGeo_point().getLatitude()
                            , deliveryboylocation.getGeo_point().getLongitude()))
                    .title(deliveryboyname)
                    .snippet("delivery boy"));
            mDeliveryBoysMarker.showInfoWindow();
        }else{
            LatLngInterpolator.animateMarkerTo(mDeliveryBoysMarker,
                    new LatLng(deliveryboylocation.getGeo_point().getLatitude()
                            , deliveryboylocation.getGeo_point().getLongitude()),
                    new LatLngInterpolator.AccelerateDecelerate());
        }

//        deliveryroutemarkers.add(mDeliveryBoysMarker);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldTrackDeliveryBoy) {
            startObservingChangesOfDeliveryBoysLocation();
        }
    }

    void calculateDirections() {
        RouteManager routeManager = new RouteManager();
        // Create the RoutePlan and add two waypoints
        RoutePlan routePlan = new RoutePlan();
        routePlan.addWaypoint(new GeoCoordinate(deliveryBoyLocation.getGeo_point().getLatitude()
                , deliveryBoyLocation.getGeo_point().getLongitude()));
        routePlan.addWaypoint(new GeoCoordinate(Double.parseDouble(userlat)
                , Double.parseDouble(userlon)));

        RouteOptions routeOptions = new RouteOptions();
        routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
        routeOptions.setRouteType(RouteOptions.Type.FASTEST);

        routePlan.setRouteOptions(routeOptions);
        routeManager.calculateRoute(routePlan, new RouteListener());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // getDeliveryBoyLocationFirstTime();
        LatLngBounds latLngBounds = toBounds(new LatLng(Double.parseDouble(userlat)
                , Double.parseDouble(userlon)), 1000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 2));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 2));
        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(userlat)
                        , Double.parseDouble(userlon)))
                .icon(BitmapDescriptorFactory.defaultMarker(240))
                .title("You")
                .snippet("Vendor"));

//        startUserLocationsRunnable();
        MapEngine.getInstance().init(new ApplicationContext(getContext()), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if (error != Error.NONE)
                    throw new RuntimeException(error.getDetails(), error.getThrowable());
                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                //calculateDirections();
//                startUserLocationsRunnable();
                shouldTrackDeliveryBoy = true;
                startObservingChangesOfDeliveryBoysLocation();
            }
        });

    }

    private class RouteListener implements RouteManager.Listener {

        @Override
        public void onProgress(int i) {
            //Toast.makeText(OrderTrackingMapActivity.this,"finding the route",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCalculateRouteFinished(RouteManager.Error error, List<RouteResult> list) {
            Route route = list.get(0).getRoute();

            int triptime = route.getTta(Route.TrafficPenaltyMode.DISABLED, Route.WHOLE_ROUTE).getDuration();
            tripduration.setText("Remaining Time : " + String.valueOf(triptime) + " seconds");
            int tripdis = route.getLength();
            tripdistance.setText("Remaining Distance : " + String.valueOf(tripdis) + " meters");
            //int timeInSeconds = route.getTtaExcludingTraffic(Route.WHOLE_ROUTE).getDuration();
            List<LatLng> newDecodedPath = new ArrayList<>();
            List<GeoCoordinate> geoCoordinates = list.get(0).getRoute().getRouteGeometry();
            for (int i = 0; i < geoCoordinates.size(); i++) {
                newDecodedPath.add(new LatLng(geoCoordinates.get(i).getLatitude()
                        , geoCoordinates.get(i).getLongitude()));
            }
            for (int j = 0; j < tripspolylines.size(); j++) {
                tripspolylines.get(j).remove();
            }
            Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
            polyline.setColor(R.color.darkgray);
            tripspolylines.add(polyline);
            //polyline.setClickable(true);
        }
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

}