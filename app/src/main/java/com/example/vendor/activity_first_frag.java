package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.Constants.ApiInterface;
import com.example.Models.StatusResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_first_frag extends Fragment
{


    Toolbar toolbar;
    TextView switch_txt, current_cnt_tv, new_cnt_tv, previous_cnt_tv, orders_cnt_tv, subscription_cnt_tv;
    LinearLayout new_ly, current_ly, previous_ly, subscription_ly, orders_ly, offline_layout;
    TextView new_tv, current_tv, previous_tv, subscription_tv, orders_tv;
    Switch switch_btn;

    Button subscrption_btn, order_btn;

    TextView newTabTitle, currentTabTitle, previousTabTitle, editFruits;
    View currentTabLine, previousTabLine, newTabLine;

    ProgressDialog dialog;

    FrameLayout fragmentcontainer;

    BottomNavigationView bottom_navigation;

    public int section = 1, sub_section = 1;
    private Retrofit retrofit;

    String check = " ";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.first_activity_frag, container, false);


        fragmentcontainer = rootView.findViewById(R.id.fragmentcontainer);

        check = MainActivity_.check;


//        switch_txt = rootView.findViewById(R.id.switch_txt);
//        switch_btn = rootView.findViewById(R.id.switch_btn);
//        current_ly = rootView.findViewById(R.id.current_ly);
//        current_cnt_tv = rootView.findViewById(R.id.current_cnt_tv);
//        new_ly = rootView.findViewById(R.id.new_ly);
//        new_cnt_tv = rootView.findViewById(R.id.new_cnt_tv);
//        previous_ly = rootView.findViewById(R.id.previous_ly);
//        previous_cnt_tv = rootView.findViewById(R.id.previous_cnt_tv);
//        subscription_ly = rootView.findViewById(R.id.subscription_ly);
//        orders_ly = rootView.findViewById(R.id.orders_ly);
//        orders_cnt_tv = rootView.findViewById(R.id.orders_cnt_tv);
//        subscription_cnt_tv = rootView.findViewById(R.id.subscription_cnt_tv);
//        new_tv = rootView.findViewById(R.id.new_tv);
//        current_tv = rootView.findViewById(R.id.current_tv);
//        previous_tv = rootView.findViewById(R.id.previous_tv);
//        subscription_tv = rootView.findViewById(R.id.subscription_tv);
//        orders_tv = rootView.findViewById(R.id.orders_tv);
//        offline_layout = rootView.findViewById(R.id.offline_layout);
//
//        new_cnt_tv.setTextColor(Color.RED);
//        new_tv.setTextColor(Color.RED);
//        new_ly.setBackgroundResource(R.drawable.click_shape);
//        orders_cnt_tv.setTextColor(Color.RED);
//        orders_tv.setTextColor(Color.RED);
//        orders_ly.setBackgroundResource(R.drawable.click_shape);


        View newTab = rootView.findViewById(R.id.new_Order);
        View currentTab = rootView.findViewById(R.id.current_Order);
        View previousTab = rootView.findViewById(R.id.previous_Order);

        newTabTitle = (TextView) newTab.findViewById(R.id.tab_title);
        currentTabTitle = (TextView) currentTab.findViewById(R.id.tab_title);
        previousTabTitle = (TextView) previousTab.findViewById(R.id.tab_title);

        //Accessing the line markers for each tab
        newTabLine = newTab.findViewById(R.id.tab_line);
        currentTabLine = currentTab.findViewById(R.id.tab_line);
        previousTabLine = previousTab.findViewById(R.id.tab_line);

        //Having chopped to be selected by default
        newTabLine.setVisibility(View.VISIBLE);

        //Setting the title of each tab.
        newTabTitle.setText("New");
        currentTabTitle.setText("Current");
        previousTabTitle.setText("Previous");

//        if(check.equals("trueorder"))
//        {
//            setfrag(1, 1);
//
//        }


        switch_txt = rootView.findViewById(R.id.switch_txt);
        switch_btn = rootView.findViewById(R.id.switch_btn);
        offline_layout = rootView.findViewById(R.id.offline_layout);

        subscrption_btn = rootView.findViewById(R.id.subscription_btn);
        order_btn = rootView.findViewById(R.id.order_btn);

        retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);

        if (sharedPreferences.getString(getString(R.string.vendor_active_status_key), "inactive").equals("active"))
        {
            switch_txt.setText("Online");
            switch_txt.setTextColor(Color.GREEN);
            fragmentcontainer.setVisibility(View.VISIBLE);
            offline_layout.setVisibility(View.INVISIBLE);
            switch_btn.setOnClickListener(null);
            switch_btn.setChecked(true);
            setfrag(section, sub_section);
        } else
        {
            switch_txt.setText("Offline");
            switch_txt.setTextColor(Color.RED);
            fragmentcontainer.setVisibility(View.INVISIBLE);
            offline_layout.setVisibility(View.VISIBLE);
            switch_btn.setOnClickListener(null);
            switch_btn.setChecked(false);
            Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentcontainer);
//            getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

        }

        switch_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog = new ProgressDialog(getContext()); // this = YourActivity
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("Loading");
                dialog.setMessage("Loading. Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                if (switch_btn.isChecked())
                {
                    dialog.setTitle("Activating");
                    dialog.setMessage("Activating. Please wait...");
                    dialog.show();

                    switch_txt.setText("Online");
                    switch_txt.setTextColor(Color.GREEN);
                    fragmentcontainer.setVisibility(View.VISIBLE);
                    offline_layout.setVisibility(View.INVISIBLE);
                    setfrag(section, sub_section);


                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                            getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String phone = sharedPreferences.getString(getString(R.string.vendor_phone_key), "3");

                    JsonObject requestObj = new JsonObject();
                    requestObj.addProperty("vendor_phone", phone);
                    requestObj.addProperty("status", "active");

                    Call<StatusResponse> activationCall = apiInterface.setVendorStatus(requestObj);

                    activationCall.enqueue(new Callback<StatusResponse>()
                    {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response)
                        {
                            Log.d("letscheck", String.valueOf(response.body()));
                            if (response.isSuccessful() && response.body().getSuccess().equals("true"))
                            {
                                setfrag(section, sub_section);
                                editor.putString(getString(R.string.vendor_active_status_key), "active");
                                editor.commit();
                                Toast.makeText(getContext(), "Activated successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else
                            {
                                switch_txt.setText("Offline");
                                switch_btn.setChecked(false);
                                switch_btn.setOnCheckedChangeListener(null);
                                switch_txt.setTextColor(Color.RED);
                                fragmentcontainer.setVisibility(View.INVISIBLE);
                                offline_layout.setVisibility(View.VISIBLE);
                                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentcontainer);
                                if (f != null)
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t)
                        {

                        }
                    });

                } else
                {
                    dialog.setTitle("Deactivating");
                    dialog.setMessage("Deactivating. Please wait...");
                    dialog.show();

                    switch_txt.setText("Offline");
                    switch_txt.setTextColor(Color.RED);
                    fragmentcontainer.setVisibility(View.INVISIBLE);
                    offline_layout.setVisibility(View.VISIBLE);

                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                            getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String phone = sharedPreferences.getString(getString(R.string.vendor_phone_key), "3");

                    JsonObject requestObj = new JsonObject();
                    requestObj.addProperty("vendor_phone", phone);
                    requestObj.addProperty("status", "inactive");

                    Call<StatusResponse> activationCall = apiInterface.setVendorStatus(requestObj);

                    activationCall.enqueue(new Callback<StatusResponse>()
                    {
                        @Override
                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response)
                        {
                            if (response.isSuccessful() && response.body().getSuccess().equals("true"))
                            {
                                editor.putString(getString(R.string.vendor_active_status_key), "inactive");
                                editor.commit();
                                Toast.makeText(getContext(), "Deactivated successfully", Toast.LENGTH_SHORT).show();
                                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentcontainer);
                                if (f != null)
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
                                dialog.dismiss();
                            } else
                            {
                                switch_txt.setText("Online");
                                switch_txt.setTextColor(Color.GREEN);
                                switch_btn.setChecked(true);
                                fragmentcontainer.setVisibility(View.VISIBLE);
                                offline_layout.setVisibility(View.INVISIBLE);
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<StatusResponse> call, Throwable t)
                        {

                        }
                    });


                }
            }
        });

        newTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                section = 1;

                newTabLine.setVisibility(View.VISIBLE);
                currentTabLine.setVisibility(View.INVISIBLE);
                previousTabLine.setVisibility(View.INVISIBLE);

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

                if (switch_btn.isChecked())
                    setfrag(section, sub_section);
            }
        });

        currentTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                section = 2;

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

                newTabLine.setVisibility(View.INVISIBLE);
                currentTabLine.setVisibility(View.VISIBLE);
                previousTabLine.setVisibility(View.INVISIBLE);


                if (switch_btn.isChecked())
                    setfrag(section, sub_section);
            }
        });

        previousTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                section = 3;

                newTabLine.setVisibility(View.INVISIBLE);
                currentTabLine.setVisibility(View.INVISIBLE);
                previousTabLine.setVisibility(View.VISIBLE);

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

                if (switch_btn.isChecked())
                {
                    setfrag(section, sub_section);
                }
            }
        });

        subscrption_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sub_section = 2;

                subscrption_btn.setBackgroundResource(R.drawable.click_shape);
                order_btn.setBackgroundResource(R.drawable.shape);
                subscrption_btn.setTextColor(Color.parseColor("#f05a48"));
                order_btn.setTextColor(Color.BLACK);

//                orders_tv.setTextColor(Color.BLACK);
//                orders_cnt_tv.setTextColor(Color.BLACK);
//
//                subscription_cnt_tv.setTextColor(Color.RED);
//                subscription_tv.setTextColor(Color.RED);
//
//                subscription_ly.setBackgroundResource(R.drawable.click_shape);
//                orders_ly.setBackgroundResource(R.drawable.shape);

                if (switch_btn.isChecked())
                    setfrag(section, sub_section);
            }
        });

        order_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sub_section = 1;

                order_btn.setBackgroundResource(R.drawable.click_shape);
                subscrption_btn.setBackgroundResource(R.drawable.shape);
                order_btn.setTextColor(Color.parseColor("#F05A48"));
                subscrption_btn.setTextColor(Color.BLACK);

//                subscription_tv.setTextColor(Color.BLACK);
//                subscription_cnt_tv.setTextColor(Color.BLACK);
//
//
//                orders_cnt_tv.setTextColor(Color.RED);
//                orders_tv.setTextColor(Color.RED);
//
//                orders_ly.setBackgroundResource(R.drawable.click_shape);
//                subscription_ly.setBackgroundResource(R.drawable.shape);

                if (switch_btn.isChecked())
                    setfrag(section, sub_section);
            }
        });

//        new_ly.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                section = 1;
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
//                if (switch_btn.isChecked())
//                    setfrag(section, sub_section);
//            }
//        });
//
//        current_ly.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                section = 2;
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
//                if (switch_btn.isChecked())
//                    setfrag(section, sub_section);
//            }
//        });
//
//        previous_ly.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                section = 3;
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
//                if (switch_btn.isChecked())
//                {
//                    setfrag(section, sub_section);
//                }
//            }
//        });
//
//        subscription_ly.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                sub_section = 2;
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
//                if (switch_btn.isChecked())
//                    setfrag(section, sub_section);
//            }
//        });
//
//        orders_ly.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                sub_section = 1;
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
//                if (switch_btn.isChecked())
//                    setfrag(section, sub_section);
//            }
//        });


        return rootView;
    }

    public void setfrag(int section, int subsection)
    {

        if (section == 1 && subsection == 1)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new new_order_frag()).commit();
        else if (section == 1 && subsection == 2)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new new_subscription_frag()).commit();
        else if (section == 2 && subsection == 1)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new current_order_frag()).commit();
        else if (section == 2 && subsection == 2)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new current_subscription_frag()).commit();
        else if (section == 3 && subsection == 1)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new order_history_frag()).commit();
        else if (section == 3 && subsection == 2)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new subscription_history_frag()).commit();


    }
}
