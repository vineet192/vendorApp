package com.example.vendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class new_sub_removeditems_frag extends Fragment {


    RecyclerView subs_removed_item_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productpricelist = new ArrayList<>();
    private List<subscription_dataholder> list = new ArrayList<>();

    public JSONArray array, arr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    List<JSONObject> sharedList = new ArrayList<>();

    static String removedJson;

    Gson gson = new Gson();

    TextView price_total,enddate,startdate;

    String temp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_new_sub_removeditems, container, false);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        subs_removed_item_recycler = rootView.findViewById(R.id.subs_removed_item_recycler);
        price_total = rootView.findViewById(R.id.price_total);
        enddate = rootView.findViewById(R.id.enddate);
        startdate = rootView.findViewById(R.id.startdate);


        subs_removed_item_recycler.setHasFixedSize(true);
        subs_removed_item_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderid_ = new_subs_detail.orderId_;



        startdate.setText(new_subs_detail_frag.StartDate);
        enddate.setText(new_subs_detail_frag.EndDate);
        price_total.setText(new_subs_detail_frag.TotalPrice);

        temp = "removed_items" + orderid_;



        if(sharedPref.contains(temp))
        {

            String jsonGet = sharedPref.getString(temp, null);
            try {
                array = new JSONArray(jsonGet);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    productnamelist.add((String) object1.get("product_name"));
                    productpricelist.add((String.valueOf(object1.get("product_price"))));
                }

                for (int i = 0; i < array.length(); i++) {
                    subscription_dataholder List = new subscription_dataholder(productnamelist.get(i), productpricelist.get(i));
                    list.add(List);
                }
                adapter = new new_sub_removeitem_adapter(list, getActivity());
                subs_removed_item_recycler.setAdapter(adapter);
                adapter = null;
                list = new ArrayList<>();



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rootView;

    }
}
