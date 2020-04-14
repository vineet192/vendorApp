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

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class new_order_frag extends Fragment {


    ArrayList<String> orderID = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> total_price = new ArrayList<>();
//    ArrayList<String> timer = new ArrayList<>();
//    ArrayList<HashMap> items = new ArrayList<>();

    ArrayList<JSONObject> Orders = new ArrayList<JSONObject>();

    String tempjson;

    public SharedPreferences sharedPref;

    private String AUTH_URL = "http://gocoding.azurewebsites.net/delivery/pusher/beams-auth";



    RecyclerView recyclerview_new_order;

    RecyclerView.Adapter adapter=null;
    private List<order_dataholder> list;

    SwipeRefreshLayout pullToRefreshnew;

    String userId= "9140134040";

    EditText vendorphone,orderet;
    Button submit;


    public JSONArray array;
    public JSONObject obj,object;

    public static String JsonData, ve,o;

    Gson gson = new Gson();

    SharedPreferences.Editor edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.new_order_frag,container,false);

        recyclerview_new_order=rootView.findViewById(R.id.recyclerview_new_order);
        pullToRefreshnew = rootView.findViewById(R.id.pullToRefreshnew);

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        edit=sharedPref.edit();
        edit.putString("removed_items","");
        edit.apply();


        vendorphone=rootView.findViewById(R.id.vendorphone);
        orderet=rootView.findViewById(R.id.orderet);
        submit=rootView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendphone();
            }
        });


        tempjson =inputStreamToString(getActivity().getResources().openRawResource(R.raw.json_data));

//        Log.d("tempjson",tempjson);

        try {
            obj=new JSONObject(tempjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         edit = sharedPref.edit();

        Orders.add(obj);

        Log.d("Orders", String.valueOf(Orders));


        edit.putString("new_order", String.valueOf(Orders));
        edit.putString(userId,"hey");
        edit.commit();

        Log.d("clickcheck",sharedPref.getString("new_order", null));


        Log.d("withoutclick",sharedPref.getString(userId, null));


        try{
            String jsonGet = sharedPref.getString("new_order", null);
            if(!(jsonGet==null))
            {
                Log.i("neworder",jsonGet);

                array = new JSONArray(jsonGet);

                for(int i=0;i<array.length();i++)
                {
                    obj=array.getJSONObject(i);
                    orderID.add(obj.getString("order_id"));
                    time.add(obj.getString("time"));
                    date.add(obj.getString("date"));
                    total_price.add(obj.getString("total_price"));
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerview_new_order.setHasFixedSize(true);
        recyclerview_new_order.setLayoutManager(new LinearLayoutManager(getActivity()));

        list=new ArrayList<>();

        for (int i=0;i<1;i++) {
            order_dataholder List = new order_dataholder(orderID.get(i),date.get(i),time.get(i),total_price.get(i));
            list.add(List);
        }


        adapter = new neworder_adapter(list,getActivity());
        recyclerview_new_order.setAdapter(adapter);
        adapter=null;
        list=new ArrayList<>();

        return rootView;
    }

    private void sendphone() {

        ve=vendorphone.getText().toString();
        o= orderet.getText().toString();

        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", ve);
        op.put("order_id",o);
        String outputreq = gson.toJson(op);

        Intent intent = new Intent(getContext(), Order_detail.class);
        intent.putExtra("data", outputreq);
        startActivity(intent);


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

    @Override
    public void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(getActivity(), new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("inAppNotificationMessage");
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i("MyActivity", "Payload was missing");
                } else {
                    Log.i("MyActivity", messagePayload);
                    // Now update the UI based on your message payload!
                }
            }
        });
    }


}
