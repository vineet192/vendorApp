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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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


    List<subscription_dataholder> list = new ArrayList<>();


    ArrayList<String> orderID = new ArrayList<>();
    ArrayList<String> startdate = new ArrayList<>();
    ArrayList<String> enddate = new ArrayList<>();
    ArrayList<String> total_price = new ArrayList<>();
    ArrayList<String> subscription_type = new ArrayList<>();
//    ArrayList<HashMap> items = new ArrayList<>();

    ArrayList<JSONObject> Orders = new ArrayList<JSONObject>();

    String tempjson;

    public SharedPreferences sharedPref;

    public JSONArray array;
    public JSONObject obj, object;

    public static String JsonData,ve,o;

    Gson gson = new Gson();

    SharedPreferences.Editor edit;

    EditText vendorphone,orderet;
    Button submit;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_subscription_frag, container, false);

        recyclerview_new_subscription = rootView.findViewById(R.id.recyclerview_new_subscription);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        tempjson = inputStreamToString(getActivity().getResources().openRawResource(R.raw.json_data_subs));


        vendorphone=rootView.findViewById(R.id.vendorphone);
        submit=rootView.findViewById(R.id.submit);
        orderet=rootView.findViewById(R.id.orderet);


        try {
            obj = new JSONObject(tempjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit = sharedPref.edit();

//        Orders.add(obj);

        Log.d("Orders", String.valueOf(obj));


        edit.putString("new_subscription", String.valueOf(obj));

        edit.commit();

        Log.d("without click", sharedPref.getString("new_subscription", null));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendphone();
            }
        });


        try {
            String jsonGet = sharedPref.getString("new_subscription", null);
            if (!(jsonGet == null)) {
//                Log.i("neworder",jsonGet);

                JSONObject j1 = new JSONObject(jsonGet);

                array = j1.getJSONArray("sorders");

                for (int i = 0; i < array.length(); i++) {
                    String a;
                    obj = array.getJSONObject(i);
                    orderID.add(obj.getString("sorder_id"));
                    startdate.add(obj.getString("startdate"));
                    JSONArray a2 = obj.getJSONArray("delivery_order_date");
                    if (a2.length() == 0)
                        a = "end date";
                    else
                        a = a2.getString((a2.length() - 1));
                    enddate.add(a);
                    total_price.add(obj.getString("total_price"));
                    subscription_type.add(obj.getString("subs_type"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        list = new ArrayList<>();

        recyclerview_new_subscription.setHasFixedSize(true);
        recyclerview_new_subscription.setLayoutManager(new LinearLayoutManager(getActivity()));

        for (int i = 0; i < 1; i++) {
            subscription_dataholder List = new subscription_dataholder(orderID.get(i), startdate.get(i), enddate.get(i), total_price.get(i), subscription_type.get(i));
            list.add(List);
        }


        adapter = new new_subscription_adapter(list, getActivity());
        recyclerview_new_subscription.setAdapter(adapter);
        adapter = null;
        list = new ArrayList<>();

        return rootView;
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

    private void sendphone() {

        ve=vendorphone.getText().toString();
        o= orderet.getText().toString();

        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", ve);
        op.put("sorder_id", o);
        String outputreq = gson.toJson(op);

        Intent intent = new Intent(getContext(), new_subs_detail.class);
        intent.putExtra("data", outputreq);
        startActivity(intent);


    }


}
