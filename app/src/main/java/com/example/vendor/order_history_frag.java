package com.example.vendor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class order_history_frag extends Fragment {


    ArrayList<String> orderID = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> total_price = new ArrayList<String>();
    ArrayList<String> deliveryboy_arivingtime = new ArrayList<String>();
    ArrayList<String> deliveryboy_otp = new ArrayList<String>();
    ArrayList<String> deliveryboy_phone = new ArrayList<String>();
    ArrayList<String> deliveryboy_name = new ArrayList<String>();
    ArrayList<String> package_status = new ArrayList<String>();


    ProgressDialog progressDialog;
    RecyclerView historyItems_recyclerView;
    RecyclerView.Adapter cadapter = null;

    private List<order_dataholder> list;
    SwipeRefreshLayout pullToRefreshnew;

    Gson gson = new Gson();
    ArrayList<HashMap> sharedList = new ArrayList<>();

    String url = "https://gocoding.azurewebsites.net/vendor/history/";

    public static String previousorder_jsonData;

    ProgressDialog dialog;

    TextView noordertv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_history_frag, container, false);


        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        historyItems_recyclerView = rootView.findViewById(R.id.historyItems_recyclerView);
        pullToRefreshnew = rootView.findViewById(R.id.pullToRefreshnew);

//        noordertv = rootView.findViewById(R.id.noordertv);
//        noordertv.setVisibility(View.INVISIBLE);


        historyItems_recyclerView.setHasFixedSize(true);
        historyItems_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "donne", Toast.LENGTH_LONG).show();
                loadData();
                dialog.dismiss();
            }
        }, 3000);


        return rootView;
    }


    public void loadData() {
        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", "3");
        String outputreq = gson.toJson(op);

        Log.d("input", outputreq);

        try {
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

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            Log.d("comingdata", sb.toString());
            previousorder_jsonData = sb.toString();

            try {
                JSONObject jsonObject = new JSONObject(previousorder_jsonData);

                JSONArray array = jsonObject.getJSONArray("orders");

//            if(array.length()==0)
//                noordertv.setVisibility(View.VISIBLE);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject1 = array.getJSONObject(i);

                    orderID.add(i, jsonObject1.getString("order_id"));
                    time.add(i, jsonObject1.getString("time"));
                    date.add(i, jsonObject1.getString("date"));
                    total_price.add(i, jsonObject1.getString("price"));
                    package_status.add(i, ("order_status"));

                }

                for (int i = 0; i < array.length(); i++) {
                    order_dataholder List = new order_dataholder(orderID.get(i), date.get(i), time.get(i), total_price.get(i), package_status.get(i));
                    list.add(List);
                }
                cadapter = new order_history_adapter(list, getActivity());
                historyItems_recyclerView.setAdapter(cadapter);
                cadapter = null;
                dialog.dismiss();
                list = new ArrayList<>();

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
//            noordertv.setVisibility(View.VISIBLE);
            }

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

}
