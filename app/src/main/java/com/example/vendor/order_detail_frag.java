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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


public class order_detail_frag extends Fragment {

    RecyclerView order_detail_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productquanlist = new ArrayList<>();
    private List<neworders_model> list = new ArrayList<>();

    public static JSONArray array, arr, namearr, quanarr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_, date_order, time_order, totalprice_order;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView date, time, totalcost, deliverycharge, tax, reject_tv;
    Button accept_btn;

    static String order_Detail;
    String url_sent = "https://gocoding.azurewebsites.net/vendorresponse/";

    Gson gson = new Gson();

    String response;

    ProgressDialog dialog;

    String temp2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_order_details, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        order_detail_recycler = rootView.findViewById(R.id.order_detail_recycler);

        order_detail_recycler.setHasFixedSize(true);
        order_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        orderid_ = Order_detail.orderId_;

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        temp2 = "new_orders" + orderid_;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadrecycler();
                dialog.dismiss();
            }
        }, 200);


        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog pdialog;
                pdialog = new ProgressDialog(getContext()); // this = YourActivity
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setTitle("Loading");
                pdialog.setMessage("Loading. Please wait...");
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();

                ArrayList<JSONObject> orders = new ArrayList<JSONObject>();
                JSONObject orders_ = new JSONObject();

                HashMap<String, String> op = new HashMap<>();
                op.put("vendor_phone", "3");
                op.put("order_Id", orderid_);
                try {
                    String str = sharedPref.getString(temp2, null);
                    JSONObject object1 = new JSONObject(str);
                    JSONArray arr5 = object1.getJSONArray("name");
                    JSONArray jsonArray=object1.getJSONArray("quan");
                    op.put("items", String.valueOf(arr5));
                    op.put("quantity", String.valueOf(jsonArray));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String outputreq = gson.toJson(op);

                try {
                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_sent).openConnection()));
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
                    response = sb.toString();
                    pdialog.dismiss();

                } catch (MalformedURLException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("MalformedURLException", e.getMessage());
                } catch (ProtocolException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("ProtocolException", e.getMessage());
                } catch (IOException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("IOException", e.getMessage());

                }

                try {
                    JSONObject o9 = new JSONObject(response);
                    String check = o9.getString("success");
                    if (check.equals("Accepted")) {
                        Intent i = new Intent(getContext(), MainActivity_.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getContext(), "not accepted", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "not accepted", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        reject_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog pdialog;
                pdialog = new ProgressDialog(getContext()); // this = YourActivity
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setTitle("Loading");
                pdialog.setMessage("Loading. Please wait...");
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();

                JSONArray a = new JSONArray();
                HashMap<String, String> op = new HashMap<>();
                op.put("order_Id", orderid_);
                op.put("vendor_phone", "3");
                List<String> list = new ArrayList<>();
                op.put("items", String.valueOf(list));
                String outputreq = gson.toJson(op);

                try {
                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_sent).openConnection()));
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
                    response = sb.toString();
                    pdialog.dismiss();

                } catch (MalformedURLException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("MalformedURLException", e.getMessage());
                } catch (ProtocolException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("ProtocolException", e.getMessage());
                } catch (IOException e) {
                    pdialog.dismiss();
                    e.printStackTrace();
                    Log.d("IOException", e.getMessage());

                }

                try {
                    JSONObject o9 = new JSONObject(response);
                    String check = o9.getString("success");
                    if (check.equals("Rejected")) {
                        Intent i = new Intent(getContext(), MainActivity_.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getContext(), "not accepted", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), "not accepted", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;

    }

    public void loadrecycler() {
        try {
            String jsonGet = sharedPref.getString("newordernotify", null);
            JSONObject ob = new JSONObject(jsonGet);

            Log.d("second",jsonGet);

            JSONArray array = ob.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (orderid_.equals(object.getString("order_id"))) ;
                {
                    date.setText(object.getString("date"));
                    time.setText(object.getString("time"));
                    deliverycharge.setText(("delivery_charge"));
                    tax.setText(("tax"));
                    totalcost.setText(("total_price"));

                    namearr = object.getJSONArray("total_order");
                    quanarr = object.getJSONArray("quantity");

                    if(!sharedPref.contains(temp2)) {
                        HashMap<String, JSONArray> hashMap = new HashMap<>();
                        hashMap.put("name", namearr);
                        hashMap.put("quan", quanarr);

                        edit = sharedPref.edit();
                        edit.putString(temp2, String.valueOf(hashMap));
                        edit.commit();
                    }

                    String str = sharedPref.getString(temp2, null);
                    JSONObject object1 = new JSONObject(str);
                    arr = object1.getJSONArray("name");
                    for (int j = 0; j < arr.length(); j++) {
                        productnamelist.add(arr.getString(j));
                    }
                    arr = object1.getJSONArray("quan");
                    for (int j = 0; j < arr.length(); j++) {
                        productquanlist.add(arr.getString(j));
                    }
                }
            }
            date_order = date.getText().toString();
            time_order = time.getText().toString();
            totalprice_order = totalcost.getText().toString();

            for (int i = 0; i < arr.length(); i++) {
                neworders_model List = new neworders_model(productnamelist.get(i), productquanlist.get(i));
                list.add(List);
            }
            adapter = new orderdetail_adapter(list, getActivity());
            order_detail_recycler.setAdapter(adapter);
            adapter = null;
            list = new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
