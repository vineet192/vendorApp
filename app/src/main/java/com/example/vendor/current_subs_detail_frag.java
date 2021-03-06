package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.Models.DeleiveryBoy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
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

    public static String orderId_;
    public static boolean active = false;
    String deliver_boy_phone;
    ArrayList<DeleiveryBoy> listDeliveryBoy;
    RecyclerView subsItems_recyclerView;

    RecyclerView.Adapter adapter=null;
    private List<subscription_dataholder> list= new ArrayList<>();

    public SharedPreferences sharedPref;

    ArrayList<HashMap> sharedList = new ArrayList<>();

    Gson gson=new Gson();


    ImageView move_back_iv,phonenumber,navigation,deliveryyboyimage;

    TextView packing_status,startdate,enddate,deliverycharge,tax,totalcost,deliveryboy_name,deliveryboy_arivingstatus
            ,deliveryboy_vehicle,deliveryboy_otp;

    Button process_btn;

    LinearLayout schedule_ly,subscription_ly,remove_ly,layoutDeliveryBoyDetails;
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
        order_Detail= currentsubs_detail.order_Detail;
        Log.d("orderID",""+orderId_);
        Log.d("order_Detail",""+order_Detail);
        if(orderId_!=null && order_Detail!=null)
            saveOrderIdData(orderId_,order_Detail);
        else if(orderId_==null && order_Detail==null)
            loadOrderIdData();
        Log.d("orderID",""+orderId_);
        Log.d("order_Detail",""+order_Detail);
        layoutDeliveryBoyDetails = rootView.findViewById(R.id.deliveryBoyDetails);
        subsItems_recyclerView= rootView.findViewById(R.id.subsItems_recyclerView);
        move_back_iv= rootView.findViewById(R.id.move_back_iv);
        phonenumber= rootView.findViewById(R.id.phonenumber);
        navigation= rootView.findViewById(R.id.navigation);
        deliveryyboyimage= rootView.findViewById(R.id.deliveryyboyimage);
        startdate= rootView.findViewById(R.id.startdate);
        enddate= rootView.findViewById(R.id.enddate);
        deliverycharge= rootView.findViewById(R.id.deliverycharge);
        tax= rootView.findViewById(R.id.tax);
        deliveryboy_name= rootView.findViewById(R.id.deliveryboy_name);
        totalcost= rootView.findViewById(R.id.totalcost);
        deliveryboy_arivingstatus= rootView.findViewById(R.id.deliveryboy_arivingstatus);
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
        loadData();
        loadSubscriptionDeliveryData();
        return  rootView;
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
                        startdate.setText(object.getString("date"));
                        enddate.setText(("enddate"));
                        //packing_status.setText(("packing_status"));
                        totalcost.setText(("500"));
                        tax.setText(("tax"));
                        deliverycharge.setText(("delivery_charge"));

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

    public void  loadSubscriptionDeliveryData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences for deliveryBoyDetailsSubscription", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DeleiveryBoy>>() {}.getType();
        String json = sharedPreferences.getString("list", null);
        listDeliveryBoy = gson.fromJson(json, type);
        Log.d("jsonSubscription",""+json);
        Log.d("listSubscription",listDeliveryBoy.toString());
        if (listDeliveryBoy == null) {
            listDeliveryBoy = new ArrayList<>();
        }

        for(int i=0;i<listDeliveryBoy.size();i++){
            DeleiveryBoy currentBoy = listDeliveryBoy.get(i);
            if(currentBoy.getOrder_id().equals(orderId_)){
                layoutDeliveryBoyDetails.setVisibility(View.VISIBLE);
                deliveryboy_name.setText(currentBoy.getDel_boy_name()+"\n"+currentBoy.getDel_boy_phone());
                Picasso.get().load(Uri.parse(currentBoy.getPhotoUrl())).placeholder(R.mipmap.ic_launcher).into(deliveryyboyimage);
                Log.d("DataFromCurrentBoySubs",currentBoy.getDel_boy_name()+currentBoy.getDel_boy_phone()+"*"+currentBoy.getPhotoUrl());
                Log.d("BoyArrivedSubs",""+currentBoy.getArrived());
                if(currentBoy.getArrived())
                    deliveryboy_arivingstatus.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HI","start");
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HI","stop");
        active = false;
    }

    public void saveOrderIdData(String orderId, String order_Detail){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared_for_orderId_subFrag",getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orderId",orderId);
        editor.putString("orderDetails",order_Detail);
        editor.apply();
    }

    private void loadOrderIdData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared_for_orderId_subFrag",getActivity().MODE_PRIVATE);
        String orderID = sharedPreferences.getString("orderId",null);
        String orderDetails = sharedPreferences.getString("orderDetails",null);
        orderId_=orderID;
        order_Detail=orderDetails;
    }


}
