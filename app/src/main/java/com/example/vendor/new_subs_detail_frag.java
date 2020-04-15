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

public class new_subs_detail_frag extends Fragment {

    RecyclerView new_subs_detail_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productquanlist = new ArrayList<>();
    private List<neworders_model> list = new ArrayList<>();

    public static JSONArray array, arr, namearr, quanarr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_, StartDate, EndDate, TotalPrice, sub_type;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView startDate, endDate, totalcost, deliverycharge, tax, reject_tv;
    Button accept_btn;

    static String order_Detail;

    Gson gson = new Gson();

    ProgressDialog dialog;

    String url_recieve = "https://gocoding.azurewebsites.net/vendor/send_sorder_items/";

    String temp2,temp;

    String url_sent, response;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_new_subs_detail, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        new_subs_detail_recycler = rootView.findViewById(R.id.new_subs_detail_recycler);

        new_subs_detail_recycler.setHasFixedSize(true);
        new_subs_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        startDate = rootView.findViewById(R.id.startDate);
        endDate = rootView.findViewById(R.id.endDate);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);


        sub_type = new_subscription_adapter.subs_type;
        orderid_ = new_subs_detail.orderId_;

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        temp="removed_items"+orderid_;
        temp2 = "new_orders" + orderid_;

            Toast.makeText(getContext(), "not stored", Toast.LENGTH_LONG).show();
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

                HashMap<String, String> op = new HashMap<>();
                op.put("order_Id", orderid_);
                op.put("vendor_phone", new_subscription_frag.ve);
                try {
                    JSONObject o1 = new JSONObject(order_Detail);
                    JSONArray a1 = o1.getJSONArray("items");
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < a1.length(); i++) {
                        JSONObject o5 = a1.getJSONObject(i);
                        list.add(o5.getString("product_name"));
                    }
                    op.put("items", String.valueOf(list));
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
                    JSONObject o9= new JSONObject(response);
                    String check=o9.getString("success");
                    if(check.equals("Accepted")){
                        Intent i = new Intent(getContext(), MainActivity_.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getContext(),"not accepted",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(),"not accepted",Toast.LENGTH_LONG).show();
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
                op.put("vendor_phone", new_subscription_frag.ve);
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
                    JSONObject o9= new JSONObject(response);
                    String check=o9.getString("success");
                    if(check.equals("Rejected")){
                        Intent i = new Intent(getContext(), MainActivity_.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getContext(),"not accepted",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(),"not accepted",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return rootView;

    }



//    public void loadrecycler() {
//        try {
//            String jsonGet = sharedPref.getString(temp2, null);
//            JSONObject ob = new JSONObject(jsonGet);
//
//            arr = ob.getJSONArray("items");
//
//            JSONObject o9 = arr.getJSONObject(0);
//
//            startDate.setText(("date"));
//            endDate.setText(("enddate"));
//            deliverycharge.setText(("delivery_charge"));
//            tax.setText(("tax"));
//            totalcost.setText(("total_price"));
//            StartDate=startDate.getText().toString();
//            EndDate=endDate.getText().toString();
//            TotalPrice=totalcost.getText().toString();
//
//            for (int j = 0; j < arr.length(); j++) {
//                JSONObject object1 = arr.getJSONObject(j);
//                productnamelist.add((String) object1.get("product_name"));
//                productpricelist.add((String.valueOf(object1.get("product_price"))));
//            }
//            for (int i = 0; i < arr.length(); i++) {
//                subscription_dataholder List = new subscription_dataholder(productnamelist.get(i), productpricelist.get(i));
//                list.add(List);
//            }
//            adapter = new new_sub_detail_adapter(list, getActivity());
//            new_subs_detail_recycler.setAdapter(adapter);
//            dialog.dismiss();
//            adapter = null;
//            list = new ArrayList<>();
//
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//    }

    public void loadrecycler() {
        try {
            String jsonGet = sharedPref.getString("newsubnotify", null);
            JSONObject ob = new JSONObject(jsonGet);

            Log.d("second",jsonGet);

            JSONArray array = ob.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (orderid_.equals(object.getString("order_id"))) ;
                {
                    startDate.setText(object.getString("date"));
                    endDate.setText(object.getString("time"));
                    deliverycharge.setText(("delivery_charge"));
                    tax.setText(("tax"));
                    totalcost.setText(("total_price"));


                    namearr = object.getJSONArray("total_order");
                    quanarr = object.getJSONArray("quantity");

                    JSONObject object2 = new JSONObject();
                    object2.put("name", namearr);
                    object2.put("quan", quanarr);

                    edit = sharedPref.edit();
                    edit.putString(temp2, String.valueOf(object2));
                    edit.commit();

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
            StartDate=startDate.getText().toString();
            EndDate=endDate.getText().toString();
            TotalPrice=totalcost.getText().toString();

            for (int i = 0; i < arr.length(); i++) {
                neworders_model List = new neworders_model(productnamelist.get(i), productquanlist.get(i));
                list.add(List);
            }
            adapter = new new_sub_detail_adapter(list, getActivity());
            new_subs_detail_recycler.setAdapter(adapter);
            adapter = null;
            list = new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
