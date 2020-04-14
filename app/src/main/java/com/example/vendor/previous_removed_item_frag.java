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

import com.example.Models.order_dataholder;

public class previous_removed_item_frag extends Fragment {

    TextView date,time,totalcost;
    RecyclerView previousorder_removed_recycler;
    RecyclerView.Adapter adapter=null;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    public JSONArray array,arr;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    List<order_dataholder> list= new ArrayList<>();


    String orderid_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.previous_removeditem_frag,container,false);

        orderid_= previous_order_detail.orderId_;

        previousorder_removed_recycler=rootView.findViewById(R.id.previousorder_removed_recycler);

        previousorder_removed_recycler.setHasFixedSize(true);
        previousorder_removed_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        date=rootView.findViewById(R.id.date);
        time=rootView.findViewById(R.id.time);
        totalcost=rootView.findViewById(R.id.totalcost);


        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String jsonGet = order_history_frag.previousorder_jsonData;

        try {

            JSONObject jsonObject = new JSONObject(jsonGet);
            Log.d("removedatafull", String.valueOf(jsonObject));

            Log.d("removeid", String.valueOf(orderid_));



            JSONArray arr= jsonObject.getJSONArray("orders");

            Log.d("check", String.valueOf(arr));


            for(int i=0;i<arr.length();i++)
            {
                JSONObject object=arr.getJSONObject(i);
                if(object.getString("order_id").equals(orderid_))
                {
                    date.setText(object.getString("date"));
                    time.setText(object.getString("time"));
                    totalcost.setText(object.getString("price"));

                    Log.d("removedata", String.valueOf(object));


                    //set image

                    JSONArray array=object.getJSONArray("rejected_items");
                    for(int j=0;j<array.length();j++)
                    {
                        JSONObject object1= array.getJSONObject(j);
//                        if(object1.getString("check").equals("false")){
                            prod_name.add(object1.getString("prod_name"));
                            prod_quantity.add(object1.getString("prod_quan"));
                            int s= Integer.parseInt(object1.getString("prod_quan"))*Integer.parseInt(object1.getString("prod_price"));
                            prod_price.add(String.valueOf(s));
//                        }
                    }

                    for (int k=0;k<prod_name.size();k++) {
                        order_dataholder List = new order_dataholder(prod_name.get(k),prod_quantity.get(k),prod_price.get(k));
                        Log.d("hello", String.valueOf(List));
                        list.add(List);
                    }
                    adapter = new previous_removed_item_adapter(list,getActivity());
                    previousorder_removed_recycler.setAdapter(adapter);
                    adapter=null;
                    list=new ArrayList<>();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  rootView;
    }
}
