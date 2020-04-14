package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class previous_order_detail_frag extends Fragment {

    TextView date, time, totalcost, deliverycharge, tax;
    RecyclerView previousorder_detail_recycler;
    RecyclerView.Adapter adapter = null;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    public JSONArray array, arr;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    List<order_dataholder> list = new ArrayList<>();


    String orderid_, order_Detail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.previous_orderdetail_frag, container, false);

        orderid_ = previous_order_detail.orderId_;
        order_Detail = previous_order_detail.order_Detail;

        previousorder_detail_recycler = rootView.findViewById(R.id.previousorder_detail_recycler);

        previousorder_detail_recycler.setHasFixedSize(true);
        previousorder_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String jsonGet = order_Detail;


        try {

            JSONObject jsonObject = new JSONObject(jsonGet);

            JSONArray arr = jsonObject.getJSONArray("orders");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                if (object.getString("order_id").equals(orderid_)) {
                    date.setText(object.getString("date"));
                    time.setText(object.getString("time"));
                    totalcost.setText(object.getString("price"));
                    tax.setText(("tax"));
                    deliverycharge.setText(("delivery_charge"));

                    //set image

                    JSONArray array = object.getJSONArray("items");
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object1 = array.getJSONObject(j);
                        prod_name.add(object1.getString("prod_name"));
                        prod_quantity.add(object1.getString("prod_quan"));
                        int s = Integer.parseInt(object1.getString("prod_quan")) * Integer.parseInt(object1.getString("prod_price"));
                        prod_price.add(String.valueOf(s));
                    }

                    for (int k = 0; k < array.length(); k++) {
                        order_dataholder List = new order_dataholder(prod_name.get(k), prod_quantity.get(k), prod_price.get(k));
                        list.add(List);
                    }
                    adapter = new previous_order_detail_adapter(list, getActivity());
                    previousorder_detail_recycler.setAdapter(adapter);
                    adapter = null;
                    list = new ArrayList<>();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
