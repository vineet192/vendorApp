package com.example.vendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class currentsubs_detail extends AppCompatActivity {

    static String orderId_,order_Detail;

    RecyclerView subsItems_recyclerView;

    RecyclerView.Adapter adapter=null;
    private List<subscription_dataholder> list= new ArrayList<>();

    public SharedPreferences sharedPref;

    ArrayList<HashMap> sharedList = new ArrayList<>();

    Gson gson=new Gson();

    Toolbar toolbar;

    ImageView move_back_iv,phonenumber,navigation,deliveryyboyimage;

//    TextView orderId,packing_status,startdate,enddate,deliverycharge,tax,totalcost,deliveryboy_name,deliveryboy_arivingstatus
//            ,deliveryboy_vehicle,deliveryboy_otp;

    Button process_btn;

    LinearLayout schedule_ly,subscription_ly;
    TextView schedule_tv,subscription_tv,orderid;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentsubs_detail);

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderid= findViewById(R.id.orderid);

        Intent intent = getIntent();
        orderId_=intent.getStringExtra("orderId");
        order_Detail=intent.getStringExtra("orderDetail");
        if(orderId_!=null)
            saveOrderIdData(orderId_);
        else if(orderId_==null)
            loadOrderIdData();
        Log.d("orderIDCurrentSub",""+orderId_);
        Log.d("order_DetailCurrentSub",""+order_Detail);
        orderid.setText(orderId_);

        String a = intent.getStringExtra("checkForLaunch");
        if(a!=null && a.equals("yes")){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new current_subs_detail_frag()).commit();
        }
//        schedule_ly= findViewById(R.id.schedule_ly);
//        subscription_ly= findViewById(R.id.subscription_ly);
//        schedule_tv= findViewById(R.id.schedule_tv);
//        subscription_tv= findViewById(R.id.subscription_tv);
//        orderid= findViewById(R.id.orderid);



//        subscription_tv.setTextColor(Color.RED);
//        schedule_tv.setTextColor(Color.BLACK);
//
//        subscription_ly.setBackgroundResource(R.drawable.click_shape);
//        schedule_ly.setBackgroundResource(R.drawable.shape);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new current_subs_detail_frag()).commit();
        }
//
//        schedule_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new new_subs_schedule_frag()).commit();
//                subscription_tv.setTextColor(Color.BLACK);
//                schedule_tv.setTextColor(Color.RED);
//                schedule_ly.setBackgroundResource(R.drawable.click_shape);
//                subscription_ly.setBackgroundResource(R.drawable.shape);
//            }
//        });

//        subscription_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new current_subs_detail_frag()).commit();
//                subscription_tv.setTextColor(Color.RED);
//                schedule_tv.setTextColor(Color.BLACK);
//                schedule_ly.setBackgroundResource(R.drawable.shape);
//                subscription_ly.setBackgroundResource(R.drawable.click_shape);
//            }
//        });

//        Intent in=getIntent();
//        orderId_=in.getStringExtra("orderId");
//
//        subsItems_recyclerView= findViewById(R.id.subsItems_recyclerView);
//        move_back_iv= findViewById(R.id.move_back_iv);
//        phonenumber= findViewById(R.id.phonenumber);
//        navigation= findViewById(R.id.navigation);
//        deliveryyboyimage= findViewById(R.id.deliveryyboyimage);
//        orderId= findViewById(R.id.orderId);
//        startdate= findViewById(R.id.startdate);
//        enddate= findViewById(R.id.enddate);
//        packing_status= findViewById(R.id.packing_status);
//        deliverycharge= findViewById(R.id.deliverycharge);
//        tax= findViewById(R.id.tax);
//        deliveryboy_name= findViewById(R.id.deliveryboy_name);
//        totalcost= findViewById(R.id.totalcost);
//        deliveryboy_arivingstatus= findViewById(R.id.deliveryboy_arivingstatus);
//        deliveryboy_vehicle= findViewById(R.id.deliveryboy_vehicle);
//        deliveryboy_otp= findViewById(R.id.deliveryboy_otp);
//        process_btn= findViewById(R.id.process_btn);
//
//        subsItems_recyclerView.setHasFixedSize(true);
//        subsItems_recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
//
//        String jsonGet = sharedPref.getString("accepted_subs", null);
//
//        if(jsonGet!=null)
//        {
//            try {
//                JSONArray arr= new JSONArray(jsonGet);
//
//                for(int i=0;i<arr.length();i++)
//                {
//                    JSONObject object=arr.getJSONObject(i);
//                    if(object.getString("order_id").equals(orderId_))
//                    {
//                        orderId.setText(object.getString("order_id"));
//                        startdate.setText(object.getString("startdate"));
//                        enddate.setText(object.getString("enddate"));
//                        packing_status.setText(object.getString("packing_status"));
//                        deliveryboy_arivingstatus.setText(object.getString("Deliveryboy_status"));
//                        deliveryboy_vehicle.setText(object.getString("vehicle_number"));
//                        deliveryboy_otp.setText(object.getString("Delivery_otp"));
//                        totalcost.setText(object.getString("total_price"));
//                        tax.setText(object.getString("tax"));
//                        deliverycharge.setText(object.getString("delivery_charge"));
//                        deliveryboy_name.setText(object.getString("deliveryboy_name"));
//
//                        //set image
//
//                        JSONArray array=object.getJSONArray("items");
//                        for(int j=0;j<array.length();j++)
//                        {
//                            JSONObject object1= array.getJSONObject(j);
//                            prod_name.add(object1.getString("prod_name"));
//                            prod_quantity.add(object1.getString("quantity"));
//                            int s= Integer.parseInt(object1.getString("quantity"))*Integer.parseInt(object1.getString("prod_price"));
//                            prod_price.add(String.valueOf(s));
//                        }
//
//                        for (int k=0;k<array.length();k++) {
//                            subscription_dataholder List = new subscription_dataholder(prod_name.get(k),prod_quantity.get(k),prod_price.get(k));
//                            Log.d("hello", String.valueOf(List));
//                            list.add(List);
//                        }
//                        adapter = new currentsub_detail_adapter(list,this);
//                        subsItems_recyclerView.setAdapter(adapter);
//                        adapter=null;
//                        list=new ArrayList<>();
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }
    public void saveOrderIdData(String orderId){
        SharedPreferences sharedPreferences = getSharedPreferences("shared_for_orderId_subDetailAct",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orderId",orderId);
        editor.apply();
    }

    private void loadOrderIdData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared_for_orderId_subDetailAct",MODE_PRIVATE);
        String orderID = sharedPreferences.getString("orderId",null);
        orderId_=orderID;
    }
}
