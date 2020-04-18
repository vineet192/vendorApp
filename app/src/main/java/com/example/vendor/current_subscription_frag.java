package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class current_subscription_frag extends Fragment {


    public SharedPreferences sharedPref;


    ArrayList<String> orderID = new ArrayList<String>();
    ArrayList<String> enddate = new ArrayList<String>();
    ArrayList<String> startdate = new ArrayList<String>();
    ArrayList<String> total_price = new ArrayList<String>();
    ArrayList<String> deliveryboy_arivingtime = new ArrayList<String>();
    ArrayList<String> deliveryboy_otp = new ArrayList<String>();
    ArrayList<String> deliveryboy_phone = new ArrayList<String>();
    //    ArrayList<String> deliveryboy_name = new ArrayList<String>();
    ArrayList<String> subs_type = new ArrayList<String>();


    String url_recieve = "https://gocoding.azurewebsites.net/vendor/ongoing/";


    ProgressDialog dialog;
    RecyclerView recyclerview_current_subscription;
    RecyclerView.Adapter cadapter = null;

    private List<subscription_dataholder> list;
    SwipeRefreshLayout pullToRefreshnew;

    Gson gson = new Gson();
    ArrayList<HashMap> sharedList = new ArrayList<>();

    static String order_Detail;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_subscription_frag, container, false);

        recyclerview_current_subscription = rootView.findViewById(R.id.recyclerview_current_subscription);
        pullToRefreshnew = rootView.findViewById(R.id.pullToRefreshnew);

        recyclerview_current_subscription.setHasFixedSize(true);
        recyclerview_current_subscription.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                dialog.dismiss();
            }
        }, 200);


        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String jsonGet = sharedPref.getString("accepted_subs", null);


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

    private void loadrecycler() {
        String jsonGet=order_Detail;
        Log.d("DatafromServer",""+order_Detail);
        try {
            JSONObject object=new JSONObject(jsonGet);
            JSONArray array= object.getJSONArray("mysorders");

            for (int i = 0; i < array.length(); i++) {

                JSONObject object1 = array.getJSONObject(i);

                orderID.add(object1.getString("sorder_id"));
                enddate.add(("enddate"));
                startdate.add(object1.getString("date"));
                total_price.add(("total_price"));
                deliveryboy_arivingtime.add(("waiting"));
                deliveryboy_otp.add(("waiting"));
                deliveryboy_phone.add(("waiting"));
                subs_type.add(("waiting"));
            }

            for (int i = 0; i < array.length(); i++) {
                subscription_dataholder List = new subscription_dataholder(orderID.get(i), startdate.get(i), enddate.get(i), total_price.get(i), deliveryboy_arivingtime.get(i), deliveryboy_phone.get(i), deliveryboy_otp.get(i),subs_type.get(i));
                list.add(List);
            }
            cadapter = new current_subscription_frag_adapter(list, getActivity());
            recyclerview_current_subscription.setAdapter(cadapter);
            cadapter = null;
            list = new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
