package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Fragments.InventoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity_ extends AppCompatActivity
{
    FrameLayout fullpagefragcontainer;
    BottomNavigationView bottom_navigation;
    SharedPreferences sharedPref;

    ProgressDialog dialog;

    public static String check="false";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        fullpagefragcontainer = findViewById(R.id.fullpagefragcontainer);
        sharedPref = this.getSharedPreferences(
                "myPref", Context.MODE_PRIVATE);




        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new activity_first_frag()).commit();

        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("Task : ", "Hi getInstanceId failed * "+task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("(Token)",token);
                    }
                });


        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.Orders:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new activity_first_frag()).commit();
                        return true;
                    case R.id.Invenory:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new InventoryFragment()).commit();
                        return true;case R.id.More:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new more_page_frag()).commit();
                        return true;
                }
                return false;
            }
        });

    }

}


