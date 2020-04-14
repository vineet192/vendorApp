package com.example.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.Models.DeleiveryBoy;
import com.example.Models.OrderDescriptionResponse;
import com.example.vendor.HomeActivity;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Deleivery_Boy_Details extends Fragment {

    View v;
    private TextView name;
    private TextView phone_no;
    private Button track;
    private Button call;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private DeleiveryBoy details;
    FragmentManager manager;
    private FragmentActivity myContext;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public Deleivery_Boy_Details(){}

    public static Deleivery_Boy_Details newInstance(DeleiveryBoy details) {
        Deleivery_Boy_Details fragment = new Deleivery_Boy_Details();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, details);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.deleivery_boy_details, container, false);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        manager = myContext.getSupportFragmentManager();
        details = (DeleiveryBoy) getArguments().getSerializable(DESCRIBABLE_KEY);

        name = v.findViewById(R.id.edittextname);
        phone_no = v.findViewById(R.id.edittextphone);

        name.setText(details.getDel_boy_name());
        phone_no.setText(details.getDel_boy_phone());

        track = v.findViewById(R.id.trackLocation);
        call = v.findViewById(R.id.callboy);

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userpermission();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number = Uri.parse("tel:" + details.getDel_boy_phone());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    void userpermission()
    {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getDeviceLocation();
            return;
        }
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getDeviceLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package","com.example.Fragments", null));
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getContext(), "Please allow permission for tracking", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }
    void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                ((ProductsClient)getContext().getApplicationContext()).setUserlat(String.valueOf(mLastKnownLocation.getLatitude()));
                                ((ProductsClient)getContext().getApplicationContext()).setUserlon(String.valueOf(mLastKnownLocation.getLongitude()));
                                Toast.makeText(getContext(), "Tracking...", Toast.LENGTH_SHORT).show();
                                ((HomeActivity) getActivity()).loaddeliveryboytrackingfragment(details.getDel_boy_name()
                                        ,details.getDel_boy_phone());
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        ((ProductsClient)getContext().getApplicationContext()).setUserlat(String.valueOf(mLastKnownLocation.getLatitude()));
                                        ((ProductsClient)getContext().getApplicationContext()).setUserlon(String.valueOf(mLastKnownLocation.getLongitude()));
                                        Toast.makeText(getContext(), "Tracking...", Toast.LENGTH_SHORT).show();
                                        ((HomeActivity) getActivity()).loaddeliveryboytrackingfragment(details.getDel_boy_name()
                                                ,details.getDel_boy_phone());
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(getContext(), "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
