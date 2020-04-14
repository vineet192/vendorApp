package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class previous_sub_removeditems_frag extends Fragment {

    TextView startdate, enddate, totalcost;
    RecyclerView subs_removed_item_recycler;
    RecyclerView.Adapter adapter = null;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    public JSONArray array, arr;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    List<subscription_dataholder> list = new ArrayList<>();


    String orderid_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_previous_sub_removed, container, false);

        orderid_ = previous_subs_detail.orderId_;

        subs_removed_item_recycler = rootView.findViewById(R.id.subs_removed_item_recycler);

        subs_removed_item_recycler.setHasFixedSize(true);
        subs_removed_item_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        startdate = rootView.findViewById(R.id.startdate);
        enddate = rootView.findViewById(R.id.enddate);
        totalcost = rootView.findViewById(R.id.price_total);


        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String jsonGet = previous_subs_detail.order_Detail;

        try {

            JSONObject jsonObject = new JSONObject(jsonGet);
            JSONArray arr = jsonObject.getJSONArray("sorders");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                if (object.getString("sorder_id").equals(orderid_)) {
                    String se="s";
                    startdate.setText(object.getString("date"));
                    JSONArray array1= object.getJSONArray("delivery_order_date");
                    se=array1.getString((array1.length()-1));
                    enddate.setText((se));
                    totalcost.setText(("total_price"));

                    JSONArray array = object.getJSONArray("rejected_items");
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object1 = array.getJSONObject(j);
//                        if (object1.getString("check").equals("false")) {
                            prod_name.add(object1.getString("prod_name"));
                            prod_quantity.add(("prod_quan"));
                            int s = Integer.parseInt("4") * Integer.parseInt(object1.getString("prod_price"));
                            prod_price.add(String.valueOf(s));
//                        }
                    }

                    for (int k = 0; k < prod_name.size(); k++) {
                        subscription_dataholder List = new subscription_dataholder(prod_name.get(k), prod_quantity.get(k));
                        Log.d("hello", String.valueOf(List));
                        list.add(List);
                    }
                    adapter = new previous_sub_detail_adapter(list, getActivity());
                    subs_removed_item_recycler.setAdapter(adapter);
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
