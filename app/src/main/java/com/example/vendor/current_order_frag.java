package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Models.order_dataholder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class current_order_frag extends Fragment {

    public SharedPreferences sharedPref;


    ArrayList<String> orderID = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> total_price = new ArrayList<String>();
    ArrayList<String> deliveryboy_arivingtime = new ArrayList<String>();
    ArrayList<String> deliveryboy_otp = new ArrayList<String>();
    ArrayList<String> deliveryboy_phone = new ArrayList<String>();
//    ArrayList<String> deliveryboy_name = new ArrayList<String>();

    private String AUTH_URL = "http://192.168.43.55:3000/auth";




    ProgressDialog dialog;
    RecyclerView recyclerview_current_order;
    RecyclerView.Adapter cadapter=null;

    private List<order_dataholder> list;
    SwipeRefreshLayout pullToRefreshnew;

    Gson gson = new Gson();
    ArrayList<HashMap> sharedList = new ArrayList<>();
    String url_recieve = "https://gocoding.azurewebsites.net/vendor/ongoing/";
    static String order_Detail;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.current_order_frag,container,false);


        recyclerview_current_order=rootView.findViewById(R.id.recyclerview_current_order);
        pullToRefreshnew = rootView.findViewById(R.id.pullToRefreshnew);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



        recyclerview_current_order.setHasFixedSize(true);
        recyclerview_current_order.setLayoutManager(new LinearLayoutManager(getActivity()));

        list=new ArrayList<>();

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                dialog.dismiss();
            }
        }, 200);


        return rootView;
    }

    public void loadData() {
        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", "10");
        String outputreq = gson.toJson(op);

        Log.d("input", outputreq);

        try {
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_recieve).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestProperty("Accept-Encoding", "identity");
            httpcon.setRequestMethod("POST");
            httpcon.setChunkedStreamingMode(0);
            httpcon.connect();

            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(outputreq);
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            Log.d("comingdata", sb.toString());
            String o = sb.toString();
            order_Detail = sb.toString();

            loadrecycler();

        } catch (MalformedURLException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("MalformedURLException", e.getMessage());
        } catch (ProtocolException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("ProtocolException", e.getMessage());
        } catch (IOException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("IOException", e.getMessage());

        }
    }


    public void loadrecycler(){

        String jsonGet=order_Detail;

        try {
            JSONObject object= new JSONObject(jsonGet);

            JSONArray array= object.getJSONArray("myorders");
            for(int i=0;i<array.length();i++)
            {
                JSONObject object1=array.getJSONObject(i);
                orderID.add(object1.getString("order_id"));
                time.add(object1.getString("time"));
                date.add(object1.getString("date"));
                total_price.add(object1.getString("price"));
                deliveryboy_arivingtime.add(("waiting"));
                deliveryboy_otp.add(object1.getString("otp"));
                deliveryboy_phone.add(object1.getString("delivery_boy_phone"));
            }

            for (int i=0;i<array.length();i++) {
                order_dataholder List = new order_dataholder(orderID.get(i),date.get(i),time.get(i),total_price.get(i),deliveryboy_arivingtime.get(i),deliveryboy_phone.get(i),deliveryboy_otp.get(i));
                list.add(List);
            }
            cadapter = new currentorder_adapter(list,getActivity());
            recyclerview_current_order.setAdapter(cadapter);
            cadapter=null;
            list=new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
