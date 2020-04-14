package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Models.order_dataholder;
import com.google.gson.Gson;

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

public class remove_item_frag extends Fragment {

    RecyclerView removed_item_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productpricelist = new ArrayList<>();
    private List<neworders_model> list = new ArrayList<>();

    public JSONArray array, arr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    List<JSONObject> sharedList = new ArrayList<>();

    static String removedJson;

    Gson gson = new Gson();

    TextView date,time,totalPrice;

    String temp,temp2;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_remove_item, container, false);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        removed_item_recycler = rootView.findViewById(R.id.removed_item_recycler);

        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        totalPrice = rootView.findViewById(R.id.totalPrice);


        removed_item_recycler.setHasFixedSize(true);
        removed_item_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderid_ = Order_detail.orderId_;

        date.setText(order_detail_frag.date_order);
        time.setText(order_detail_frag.time_order);
        totalPrice.setText(order_detail_frag.totalprice_order);


        temp="removed_items"+orderid_;
        temp2 = "new_orders" + orderid_;


        if(sharedPref.contains(temp))
        {

            String jsonGet = sharedPref.getString(temp, null);
            try {
                object = new JSONObject(jsonGet);
                array=object.getJSONArray("name");
                arr=object.getJSONArray("quan");
                for (int i = 0; i < array.length(); i++) {
                    productnamelist.add(array.getString(i));
                    productpricelist.add(arr.getString(i));
                }

                for (int i = 0; i < array.length(); i++) {
                    neworders_model List = new neworders_model(productnamelist.get(i), productpricelist.get(i));
                    list.add(List);
                }
                adapter = new remove_item_adapter(list, getActivity());
                removed_item_recycler.setAdapter(adapter);
                adapter = null;
                list = new ArrayList<>();



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rootView;

    }
}
