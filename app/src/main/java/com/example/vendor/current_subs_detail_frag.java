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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class current_subs_detail_frag extends Fragment {

    String orderId_;

    String deliver_boy_phone;

    RecyclerView subsItems_recyclerView;

    RecyclerView.Adapter adapter=null;
    private List<subscription_dataholder> list= new ArrayList<>();

    public SharedPreferences sharedPref;

    ArrayList<HashMap> sharedList = new ArrayList<>();

    Gson gson=new Gson();


    ImageView move_back_iv,phonenumber,navigation,deliveryyboyimage;

    TextView orderId,packing_status,startdate,enddate,deliverycharge,tax,totalcost,deliveryboy_name,deliveryboy_arivingstatus
            ,deliveryboy_vehicle,deliveryboy_otp;

    Button process_btn;

    LinearLayout schedule_ly,subscription_ly,remove_ly;
    TextView schedule_tv,subscription_tv,orderid,remove_tv;

    ProgressDialog dialog;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    String url_recieve = "https://gocoding.azurewebsites.net/vendor/ongoing/";
    String order_Detail;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.current_subs_detail_frag,container,false);


        orderId_= currentsubs_detail.orderId_;
//        order_Detail= currentsubs_detail.order_Detail;

        subsItems_recyclerView= rootView.findViewById(R.id.subsItems_recyclerView);
        move_back_iv= rootView.findViewById(R.id.move_back_iv);
        phonenumber= rootView.findViewById(R.id.phonenumber);
        navigation= rootView.findViewById(R.id.navigation);
        deliveryyboyimage= rootView.findViewById(R.id.deliveryyboyimage);
        orderId= rootView.findViewById(R.id.orderId);
        startdate= rootView.findViewById(R.id.startdate);
        enddate= rootView.findViewById(R.id.enddate);
        packing_status= rootView.findViewById(R.id.packing_status);
        deliverycharge= rootView.findViewById(R.id.deliverycharge);
        tax= rootView.findViewById(R.id.tax);
        deliveryboy_name= rootView.findViewById(R.id.deliveryboy_name);
        totalcost= rootView.findViewById(R.id.totalcost);
        deliveryboy_arivingstatus= rootView.findViewById(R.id.deliveryboy_arivingstatus);
        deliveryboy_vehicle= rootView.findViewById(R.id.deliveryboy_vehicle);
        deliveryboy_otp= rootView.findViewById(R.id.deliveryboy_otp);
        process_btn= rootView.findViewById(R.id.process_btn);

        subsItems_recyclerView.setHasFixedSize(true);
        subsItems_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dialog = new ProgressDialog(getActivity()); // this = YourActivity
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
                loadrecycler();
                dialog.dismiss();
            }
        }, 200);

        sharedPref = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),current_order_detail_tracking.class);
                startActivity(intent);
            }
        });

        return  rootView;
    }

    public void loadData() {
        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", "3");
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

        if(jsonGet!=null)
        {
            try {
                JSONObject object2=new JSONObject(jsonGet);
                JSONArray arr= object2.getJSONArray("mysorders");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject object=arr.getJSONObject(i);
                    if(object.getString("sorder_id").equals(orderId_))
                    {
                        orderId.setText(object.getString("sorder_id"));
                        startdate.setText(object.getString("date"));
                        enddate.setText(("enddate"));
                        packing_status.setText(("packing_status"));
                        deliveryboy_arivingstatus.setText(("waiting"));
                        deliveryboy_vehicle.setText(("waiting"));
                        deliveryboy_otp.setText(("waiting"));
                        totalcost.setText(("total_price"));
                        tax.setText(("tax"));
                        deliverycharge.setText(("delivery_charge"));
                        deliveryboy_name.setText(("waiting"));
                        deliver_boy_phone=object.getString("delivery_boy_phone");

                        //set image

                        JSONArray array=object.getJSONArray("items");
                        for(int j=0;j<array.length();j++)
                        {
                            JSONObject object1= array.getJSONObject(j);
                            prod_name.add(object1.getString("prod_name"));
                            prod_quantity.add(object1.getString("prod_quan"));
//                            int s= Integer.parseInt(object1.getString("quantity"))*Integer.parseInt(object1.getString("prod_price"));
                            prod_price.add(object1.getString("prod_price"));
                        }

                        for (int k=0;k<array.length();k++) {
                            subscription_dataholder List = new subscription_dataholder(prod_name.get(k),prod_quantity.get(k),prod_price.get(k));
                            Log.d("hello", String.valueOf(List));
                            list.add(List);
                        }
                        adapter = new currentsub_detail_adapter(list,getActivity());
                        subsItems_recyclerView.setAdapter(adapter);
                        adapter=null;
                        list=new ArrayList<>();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
