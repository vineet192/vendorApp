package com.example.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.Models.order_dataholder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class new_subscription_frag extends Fragment {

    RecyclerView recyclerview_new_subscription;
    RecyclerView.Adapter adapter = null;
    private ArrayList<subscription_dataholder> listOrders;
    List<subscription_dataholder> list = new ArrayList<>();
    public static boolean active = false;

    ArrayList<String> orderID = new ArrayList<>();
    ArrayList<String> startdate = new ArrayList<>();
    ArrayList<String> enddate = new ArrayList<>();
    ArrayList<String> total_price = new ArrayList<>();

    public SharedPreferences sharedPref;

    public JSONArray array;
    public JSONObject obj, object;

    Gson gson = new Gson();

    SharedPreferences.Editor edit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_subscription_frag, container, false);

        recyclerview_new_subscription = rootView.findViewById(R.id.recyclerview_new_subscription);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        loadNewOrderData();


//        try {
//            obj = new JSONObject(tempjson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        edit = sharedPref.edit();
//
////        Orders.add(obj);
//
//        Log.d("Orders", String.valueOf(obj));
//
//
//        edit.putString("new_subscription", String.valueOf(obj));
//
//        edit.commit();
//
//        Log.d("without click", sharedPref.getString("new_subscription", null));
//
//
//        try {
//            String jsonGet = sharedPref.getString("new_subscription", null);
//            if (!(jsonGet == null)) {
////                Log.i("neworder",jsonGet);
//
//                JSONObject j1 = new JSONObject(jsonGet);
//
//                array = j1.getJSONArray("sorders");
//
//                for (int i = 0; i < array.length(); i++) {
//                    String a;
//                    obj = array.getJSONObject(i);
//                    orderID.add(obj.getString("sorder_id"));
//                    startdate.add(obj.getString("startdate"));
//                    JSONArray a2 = obj.getJSONArray("delivery_order_date");
//                    if (a2.length() == 0)
//                        a = "end date";
//                    else
//                        a = a2.getString((a2.length() - 1));
//                    enddate.add(a);
//                    total_price.add(obj.getString("total_price"));
//                    subscription_type.add(obj.getString("subs_type"));
//                }
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        recyclerview_new_subscription.setHasFixedSize(true);
        recyclerview_new_subscription.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new new_subscription_adapter(listOrders,getContext());
        recyclerview_new_subscription.setAdapter(adapter);

        return rootView;
    }

    public void  loadNewOrderData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences for newSubscription", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<subscription_dataholder>>() {}.getType();
        listOrders = gson.fromJson(json, type);

        if (listOrders == null) {
            listOrders = new ArrayList<>();
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

}
