package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class previous_subs_detail_frag extends Fragment {

    RecyclerView new_subs_detail_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productpricelist = new ArrayList<>();
    ArrayList<String> prod_price = new ArrayList<>();

    private List<subscription_dataholder> list = new ArrayList<>();

    public JSONArray array, arr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_, StartDate, EndDate, TotalPrice, sub_type;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView startDate, endDate, totalcost, deliverycharge, tax, reject_tv;
    Button accept_btn;

    int totalprice = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_previous_subs_detail, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        new_subs_detail_recycler = rootView.findViewById(R.id.new_subs_detail_recycler);

        new_subs_detail_recycler.setHasFixedSize(true);
        new_subs_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        startDate = rootView.findViewById(R.id.startDate);
        endDate = rootView.findViewById(R.id.endDate);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);


//        sub_type = previous_subs_detail.subs_type;
        orderid_ = previous_subs_detail.orderId_;

//        orderid_ = previous_subs_detail.orderId_;


        try {
            String jsonGet = previous_subs_detail.order_Detail;
            JSONObject object = new JSONObject(jsonGet);
            array = object.getJSONArray("sorders");
            for (int i = 0; i < array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);
                if (ob.getString("sorder_id").equals(orderid_)) {

                    startDate.setText(ob.getString("date").toString());
                    endDate.setText(("enddate").toString());
                    deliverycharge.setText(("delivery_charge").toString());
                    tax.setText(("tax").toString());
                    totalcost.setText(("total_price").toString());

                    arr = ob.getJSONArray("items");
                    for (int j = 0; j < arr.length(); j++) {
                        JSONObject object1 = arr.getJSONObject(j);
//                        if (object1.getString("check").equals("true")) {
                            productnamelist.add(object1.getString("prod_name"));
                            productpricelist.add(("prod_quan"));
                            int s = Integer.parseInt(("4")) * Integer.parseInt(object1.getString("prod_price"));
                            prod_price.add(String.valueOf(s));
//                        }
                    }
                }
            }

            for (int k = 0; k < arr.length(); k++) {
                subscription_dataholder List = new subscription_dataholder(productnamelist.get(k), productpricelist.get(k));
                list.add(List);
            }


            adapter = new previous_sub_detail_adapter(list, getActivity());
            new_subs_detail_recycler.setAdapter(adapter);
            adapter = null;
            list = new ArrayList<>();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return rootView;

    }
}
