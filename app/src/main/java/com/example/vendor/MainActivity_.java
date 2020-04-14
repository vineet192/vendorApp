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

//    Toolbar toolbar;
//    TextView switch_txt,current_cnt_tv,new_cnt_tv,previous_cnt_tv,orders_cnt_tv,subscription_cnt_tv;
//    LinearLayout new_ly,current_ly,previous_ly,subscription_ly,orders_ly,offline_layout;
//    TextView new_tv,current_tv,previous_tv,subscription_tv,orders_tv;
//    Switch switch_btn;

    FrameLayout fullpagefragcontainer;
    BottomNavigationView bottom_navigation;
    SharedPreferences sharedPref;

//    public int section=1,sub_section=1;

    ProgressDialog dialog;

    public static String check="false";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

//        toolbar= findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        fullpagefragcontainer = findViewById(R.id.fullpagefragcontainer);
        sharedPref = this.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);


//        fullpagefragcontainer= findViewById(R.id.fullpagefragcontainer);
//
//
//
//        switch_txt=findViewById(R.id.switch_txt);
//        switch_btn=findViewById(R.id.switch_btn);
//        current_ly=findViewById(R.id.current_ly);
//        current_cnt_tv=findViewById(R.id.current_cnt_tv);
//        new_ly=findViewById(R.id.new_ly);
//        new_cnt_tv=findViewById(R.id.new_cnt_tv);
//        previous_ly=findViewById(R.id.previous_ly);
//        previous_cnt_tv=findViewById(R.id.previous_cnt_tv);
//        subscription_ly=findViewById(R.id.subscription_ly);
//        orders_ly=findViewById(R.id.orders_ly);
//        orders_cnt_tv=findViewById(R.id.orders_cnt_tv);
//        subscription_cnt_tv=findViewById(R.id.subscription_cnt_tv);
//        new_tv=findViewById(R.id.new_tv);
//        current_tv=findViewById(R.id.current_tv);
//        previous_tv=findViewById(R.id.previous_tv);
//        subscription_tv=findViewById(R.id.subscription_tv);
//        orders_tv=findViewById(R.id.orders_tv);
//        offline_layout=findViewById(R.id.offline_layout);


//        new_cnt_tv.setTextColor(Color.RED);
//        new_tv.setTextColor(Color.RED);
//        new_ly.setBackgroundResource(R.drawable.click_shape);
//        orders_cnt_tv.setTextColor(Color.RED);
//        orders_tv.setTextColor(Color.RED);
//        orders_ly.setBackgroundResource(R.drawable.click_shape);


        if (savedInstanceState == null)
        {
//            subscription_tv.setTextColor(Color.BLACK);
//            previous_tv.setTextColor(Color.BLACK);
//            current_tv.setTextColor(Color.BLACK);
//            subscription_cnt_tv.setTextColor(Color.BLACK);
//            previous_cnt_tv.setTextColor(Color.BLACK);
//            current_cnt_tv.setTextColor(Color.BLACK);
//            new_cnt_tv.setTextColor(Color.RED);
//            new_tv.setTextColor(Color.RED);
//            orders_cnt_tv.setTextColor(Color.RED);
//            orders_tv.setTextColor(Color.RED);
            getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new activity_first_frag()).commit();

        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("Task : ", "Hi getInstanceId failed * "+task.getException());
                            return;
                            //Remember that there is error in generating token if mobile is not connected to internet.
                        }
                        String token = task.getResult().getToken();
                        Log.d("(Token)",token);
                    }
                });

//        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    switch_txt.setText("Online");
//                    switch_txt.setTextColor(Color.GREEN);
//                    setfrag(section,sub_section);
//                    offline_layout.setVisibility(View.INVISIBLE);
//                }
//                else {
//                    switch_txt.setText("Ofline");
//                    switch_txt.setTextColor(Color.RED);
//                    offline_layout.setVisibility(View.VISIBLE);
//                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                    }                }
//            }
//        });
//
//        new_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                section=1;
//
//                previous_tv.setTextColor(Color.BLACK);
//                current_tv.setTextColor(Color.BLACK);
//                previous_cnt_tv.setTextColor(Color.BLACK);
//                current_cnt_tv.setTextColor(Color.BLACK);
//
//                new_cnt_tv.setTextColor(Color.RED);
//                new_tv.setTextColor(Color.RED);
//
//                new_ly.setBackgroundResource(R.drawable.click_shape);
//                current_ly.setBackgroundResource(R.drawable.shape);
//                previous_ly.setBackgroundResource(R.drawable.shape);
//
//                if(switch_btn.isChecked())
//                    setfrag(section,sub_section);
//            }
//        });
//
//        current_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                section=2;
//
//                previous_tv.setTextColor(Color.BLACK);
//                new_tv.setTextColor(Color.BLACK);
//                previous_cnt_tv.setTextColor(Color.BLACK);
//                new_cnt_tv.setTextColor(Color.BLACK);
//
//                current_cnt_tv.setTextColor(Color.RED);
//                current_tv.setTextColor(Color.RED);
//
//                current_ly.setBackgroundResource(R.drawable.click_shape);
//                new_ly.setBackgroundResource(R.drawable.shape);
//                previous_ly.setBackgroundResource(R.drawable.shape);
//
//
//                if(switch_btn.isChecked())
//                    setfrag(section,sub_section);            }
//        });
//
//        previous_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                section=3;
//
//                current_tv.setTextColor(Color.BLACK);
//                new_tv.setTextColor(Color.BLACK);
//                new_cnt_tv.setTextColor(Color.BLACK);
//                current_cnt_tv.setTextColor(Color.BLACK);
//
//                previous_cnt_tv.setTextColor(Color.RED);
//                previous_tv.setTextColor(Color.RED);
//
//                previous_ly.setBackgroundResource(R.drawable.click_shape);
//                new_ly.setBackgroundResource(R.drawable.shape);
//                current_ly.setBackgroundResource(R.drawable.shape);
//                subscription_ly.setBackgroundResource(R.drawable.shape);
//                orders_ly.setBackgroundResource(R.drawable.shape);
//
//                if(switch_btn.isChecked())
//                    setfrag(section,sub_section);            }
//        });
//
//        subscription_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sub_section=2;
//
//                orders_tv.setTextColor(Color.BLACK);
//                orders_cnt_tv.setTextColor(Color.BLACK);
//
//                subscription_cnt_tv.setTextColor(Color.RED);
//                subscription_tv.setTextColor(Color.RED);
//
//                subscription_ly.setBackgroundResource(R.drawable.click_shape);
//                orders_ly.setBackgroundResource(R.drawable.shape);
//
//                if(switch_btn.isChecked())
//                    setfrag(section,sub_section);            }
//        });
//
//        orders_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sub_section=1;
//
//                subscription_tv.setTextColor(Color.BLACK);
//                subscription_cnt_tv.setTextColor(Color.BLACK);
//
//
//                orders_cnt_tv.setTextColor(Color.RED);
//                orders_tv.setTextColor(Color.RED);
//
//                orders_ly.setBackgroundResource(R.drawable.click_shape);
//                subscription_ly.setBackgroundResource(R.drawable.shape);
//
//                if(switch_btn.isChecked())
//                    setfrag(section,sub_section);            }
//        });

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
                        return true;
//                        viewFragment(new OneFragment(), FRAGMENT_OTHER);
                    case R.id.More:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fullpagefragcontainer, new more_page_frag()).commit();
                        return true;
                }
                return false;
            }
        });

    }


    //    public void setfrag(int section,int subsection)
//    {
//
//        if(section==1&&subsection==1)
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new new_order_frag()).commit();
//        else if(section==1&&subsection==2)
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new new_subscription_frag()).commit();
//        else if(section==2&&subsection==1)
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new current_order_frag()).commit();
//        else if(section==2&&subsection==2)
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new current_subscription_frag()).commit();
//        else if(section==3&&subsection==1)
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new order_history_frag()).commit();
////        else if(section==3&&subsection==2)
////            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new current_subscription_frag()).commit();
//
//
//    }


}


