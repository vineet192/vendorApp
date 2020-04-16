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

}
