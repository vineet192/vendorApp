package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Constants.ApiInterface;
import com.example.Models.LoginRequest;
import com.example.Models.StatusResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class more_page_frag extends Fragment
{

    TextView profile_name, profile_id;
    ImageView profile_image;
    LinearLayout profile_ly, earning_ly, notices_ly, logout_ly;
    BottomNavigationView bottom_navigation;
    View logoutLoader;
    private SharedPreferences sharedPreferences;

    Gson gson = new Gson();

    ProgressDialog dialog;

    String jsonop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.frag_more_page, container, false);

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        profile_name = rootView.findViewById(R.id.profile_name);
        profile_id = rootView.findViewById(R.id.profile_id);
        profile_image = (ImageView) rootView.findViewById(R.id.profile_image);
        profile_ly = rootView.findViewById(R.id.profile_ly);
        earning_ly = rootView.findViewById(R.id.earning_ly);
        notices_ly = rootView.findViewById(R.id.notices_ly);
        logout_ly = rootView.findViewById(R.id.logout_ly);
        logoutLoader = rootView.findViewById(R.id.logout_loader);
        bottom_navigation = getActivity().findViewById(R.id.bottom_navigation);
        sharedPreferences = getContext().getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setpage();
                dialog.dismiss();
            }
        }, 200);

        profile_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), more_detail.class);
                intent.putExtra("sec", "1");
                startActivity(intent);
            }
        });

        earning_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), MyEarningActivity.class);
                intent.putExtra("sec", "1");
                startActivity(intent);
            }
        });

        notices_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), more_detail.class);
                intent.putExtra("sec", "1");
                startActivity(intent);
            }
        });

        logout_ly.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logoutLoader.setVisibility(View.VISIBLE);
                bottom_navigation.setVisibility(View.GONE);
                logout();
            }
        });

        return rootView;
    }

    private void logout()
    {
        profile_ly.setOnClickListener(null);
        earning_ly.setOnClickListener(null);
        notices_ly.setOnClickListener(null);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String phone = sharedPreferences.getString(getString(R.string.vendor_phone_key), "-1");

        JsonObject requestObj = new JsonObject();
        requestObj.addProperty("vendor_phone", phone);
        requestObj.addProperty("status", "inactive");

        Call<StatusResponse> activationCall = apiInterface.setVendorStatus(requestObj);

        activationCall.enqueue(new Callback<StatusResponse>()
        {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response)
            {
                if (response.body().getSuccess().equals("true"))
                {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentcontainer);
                    editor.clear();
                    editor.commit();
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                    logoutLoader.setVisibility(View.GONE);
                } else
                {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    logoutLoader.setVisibility(View.GONE);
                    bottom_navigation.setVisibility(View.VISIBLE);
                    getFragmentManager()
                            .beginTransaction()
                            .detach(more_page_frag.this)
                            .attach(more_page_frag.this)
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t)
            {

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                getFragmentManager()
                        .beginTransaction()
                        .detach(more_page_frag.this)
                        .attach(more_page_frag.this)
                        .commit();
            }
        });

    }

    public void setpage(){

        String url = "https://gocoding.azurewebsites.net/vendor/history/";
        profile_name.setText(sharedPreferences.getString(getString(R.string.vendor_name_key), "defaultName"));
        profile_id.setText(sharedPreferences.getString(getString(R.string.vendor_id_key), "defaultId"));

        Picasso.get().load("https://gocoding.azurewebsites.net"
                + sharedPreferences.getString(getString(R.string.vendor_image_key), "null"))
                .placeholder(R.drawable.default_fruit)
                .into(profile_image);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", "919191");
        String outputreq = gson.toJson(op);

        Log.d("input", outputreq);

        try
        {
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(outputreq);
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            br.close();
            Log.d("comingdata", sb.toString());
            jsonop = sb.toString();

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.d("MalformedURLException", e.getMessage());
        } catch (ProtocolException e)
        {
            e.printStackTrace();
            Log.d("ProtocolException", e.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.d("IOException", e.getMessage());

        }

    }
}
