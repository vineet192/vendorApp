package com.example.vendor;

import android.content.Context;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class new_order_frag extends Fragment {

    RecyclerView recyclerview_new_order;
    RecyclerView.Adapter adapter=null;
    private ArrayList<order_dataholder> listOrders;
    SwipeRefreshLayout pullToRefreshnew;

    String tempjson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.new_order_frag,container,false);
        loadNewOrderData();

        recyclerview_new_order=rootView.findViewById(R.id.recyclerview_new_order);
        pullToRefreshnew = rootView.findViewById(R.id.pullToRefreshnew);
        recyclerview_new_order.setHasFixedSize(true);
        recyclerview_new_order.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new neworder_adapter(listOrders,getContext());
        recyclerview_new_order.setAdapter(adapter);

        tempjson =inputStreamToString(getActivity().getResources().openRawResource(R.raw.json_data));

        SharedPreferences  sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        try {
            if(sharedPref.contains("vishnu"))
            {
                String s=sharedPref.getString("vishnu",null);
                JSONObject o= new JSONObject(s);
                JSONArray a= o.getJSONArray("list");
                JSONObject o1= new JSONObject(tempjson);
                a.put(o1);
                Log.d("101",String.valueOf(a));
                HashMap<String,JSONArray> hashMap= new HashMap<>();
                hashMap.put("list",a);
                Log.d("102", String.valueOf(hashMap));
                SharedPreferences.Editor editor=sharedPref.edit();
                editor.remove("vishnu");
                editor.putString("vishnu", String.valueOf(hashMap));
                editor.commit();
                String str= sharedPref.getString("vishnu",null);
                Log.d("103",str);
            }
            else {
                JSONObject o1= new JSONObject(tempjson);
                JSONArray a= new JSONArray();
                a.put(o1);
                Log.d("104",String.valueOf(a));
                HashMap<String,JSONArray> hashMap= new HashMap<>();
                hashMap.put("list",a);
                Log.d("105", String.valueOf(hashMap));
                SharedPreferences.Editor editor=sharedPref.edit();
                editor.putString("vishnu", String.valueOf(hashMap));
                editor.commit();
                String sttr= sharedPref.getString("vishnu",null);
                Log.d("106",sttr);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    public void  loadNewOrderData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences for newOrder", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<order_dataholder>>() {}.getType();
        listOrders = gson.fromJson(json, type);

        if (listOrders == null) {
            listOrders = new ArrayList<>();
        }
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
