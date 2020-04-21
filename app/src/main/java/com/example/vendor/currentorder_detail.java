package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Constants.ApiInterface;
import com.example.Models.DeleiveryBoy;
import com.example.Models.ItemSavingResponse;
import com.example.Models.order_dataholder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class currentorder_detail extends AppCompatActivity {

    public static String orderId_,order_Detail;
    LinearLayout layoutDeliveryBoyDetails;
    ArrayList<DeleiveryBoy> listDeliveryBoy;
    String delivery_boy_phone;
    public static boolean active = false;
    RecyclerView historyItems_recyclerView;

    RecyclerView.Adapter adapter=null;
    private List<order_dataholder> list= new ArrayList<>();

    public SharedPreferences sharedPref;
    private SharedPreferences vendorPref;

    ArrayList<HashMap> sharedList = new ArrayList<>();

    Gson gson=new Gson();

    String url_recieve = "https://gocoding.azurewebsites.net/vendor/ongoing/";
    ProgressDialog dialog;

    ImageView move_back_iv,phonenumber,navigation,deliveryyboyimage;

    TextView orderId,packing_status,date,time,deliverycharge,tax,totalcost,deliveryboy_name,deliveryboy_arivingstatus
            ,deliveryboy_vehicle,deliveryboy_otp;

    Button process_btn;

    ArrayList<String> prod_name = new ArrayList<String>();
    ArrayList<String> prod_quantity = new ArrayList<String>();
    ArrayList<String> prod_price = new ArrayList<String>();
    private boolean isDispatch = false;
    private String vendorPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentorder_detail);

        Intent intent = getIntent();
        orderId_ = intent.getStringExtra("orderId");
        if(orderId_!=null)
            saveOrderID(orderId_);
        else if(orderId_==null)
            loadOrderId();
//        order_Detail = intent.getStringExtra("orderDetail");

        Log.d("orderid",orderId_);

        vendorPref = this.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        vendorPhone = vendorPref.getString(getString(R.string.vendor_phone_key),null);
        layoutDeliveryBoyDetails = findViewById(R.id.deliveryBoyDetails);
        historyItems_recyclerView= findViewById(R.id.historyItems_recyclerView);
        move_back_iv= findViewById(R.id.move_back_iv);
        phonenumber= findViewById(R.id.phonenumber);
        navigation= findViewById(R.id.navigation);
        deliveryyboyimage= findViewById(R.id.deliveryyboyimage);
        orderId= findViewById(R.id.orderId);
        date= findViewById(R.id.date);
        time= findViewById(R.id.time);
        packing_status= findViewById(R.id.packing_status);
        deliverycharge= findViewById(R.id.deliverycharge);
        tax= findViewById(R.id.tax);
        deliveryboy_name= findViewById(R.id.deliveryboy_name);
        totalcost= findViewById(R.id.totalcost);
        deliveryboy_arivingstatus= findViewById(R.id.deliveryboy_arivingstatus);
        deliveryboy_vehicle= findViewById(R.id.deliveryboy_vehicle);
        deliveryboy_otp= findViewById(R.id.deliveryboy_otp);
        process_btn= findViewById(R.id.process_btn);

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 200);

        historyItems_recyclerView.setHasFixedSize(true);
        historyItems_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(currentorder_detail.this,current_order_detail_tracking.class);
                startActivity(intent1);
            }
        });
        loadNormalDeliveryData();

        process_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!isDispatch)
                {
                    callVendorPrepared();
                }
                else{
                    callVendorDispatch();
                }
            }
        });
    }

    private void callVendorDispatch()
    {
        dialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("vendor_phone",vendorPhone);
        requestObject.addProperty("order_id",orderId.getText().toString());
        requestObject.addProperty("order_type","N");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ItemSavingResponse> call = apiInterface.dispatchVendorOrder(requestObject);

        call.enqueue(new Callback<ItemSavingResponse>()
        {
            @Override
            public void onResponse(Call<ItemSavingResponse> call, Response<ItemSavingResponse> response)
            {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(currentorder_detail.this, "Dispatch successfull " +
                            response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(currentorder_detail.this, "Dispatch failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemSavingResponse> call, Throwable t)
            {
                dialog.dismiss();
                Toast.makeText(currentorder_detail.this, "Dispatch Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callVendorPrepared()
    {
        dialog.show();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("vendor_phone",vendorPhone);
        requestObject.addProperty("order_id","4bcb7126-4480-4a3c-a973-ade1ef7afc5b");
        requestObject.addProperty("order_type","N");

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<JsonObject> call = apiInterface.prepareOrder(requestObject);
            System.out.println(requestObject.toString());
        call.enqueue(new Callback<JsonObject>()
        {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response)
            {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    process_btn.setText("Dispatch");
                    isDispatch = true;
                    Toast.makeText(currentorder_detail.this, "Prepare order success", Toast.LENGTH_SHORT).show();
                }
                else{
                    dialog.dismiss();
                    Toast.makeText(currentorder_detail.this, "Order prepared failure", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t)
            {
                dialog.dismiss();
                Toast.makeText(currentorder_detail.this, "Order prepared failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveOrderID(String orderId_) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_for_orderId",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orderId",orderId_);
        editor.apply();
    }

    private void loadOrderId(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared_for_orderId",MODE_PRIVATE);
        String orderID = sharedPreferences.getString("orderId",null);
        orderId_.equals(orderID);
    }

//    public void  loadDeliveryBoyDetailsData(){
//        Log.d("loadDeliveryBoy","called");
//            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for deliveryBoyDetails", MODE_PRIVATE);
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<DeleiveryBoy>>() {}.getType();
//                String json = sharedPreferences.getString("listNormalDeliveryBoy", "pagal");
//                listDeliveryBoy = gson.fromJson(json, type);
//                Log.d("listDeliveryBoy",""+listDeliveryBoy);
//                if (listDeliveryBoy == null) {
//                    listDeliveryBoy = new ArrayList<>();
//                }
//
//                for(int i=0;i<listDeliveryBoy.size();i++){
//                    DeleiveryBoy currentBoy = listDeliveryBoy.get(i);
//                    Log.d("DeliveryBoys ",currentBoy.getDel_boy_name()+"*"+currentBoy.getDel_boy_phone()+"*"+currentBoy
//                    .getOrder_id());
//                    if(currentBoy.getOrder_id().equals(orderId_)){
//                        Log.d("currentBoy ",currentBoy.getDel_boy_name()+"*"+currentBoy.getDel_boy_phone()+"*"+currentBoy
//                                .getOrder_id());
//                        layoutDeliveryBoyDetails.setVisibility(View.VISIBLE);
//                        deliveryboy_name.setText(currentBoy.getDel_boy_name());
//                        deliveryboy_vehicle.setText(currentBoy.getDel_boy_phone());
//                        deliveryyboyimage.setImageURI(Uri.parse(currentBoy.getPhotoUrl()));
//                        if(currentBoy.getArrived())
//                            deliveryboy_arivingstatus.setVisibility(View.VISIBLE);
//                    }
//                }
//        }

    public void  loadNormalDeliveryData(){
        Log.d("loadDeliveryBoy","called");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for deliveryBoyDetailsOrder", MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DeleiveryBoy>>() {}.getType();
        String json = sharedPreferences.getString("list", null);
        listDeliveryBoy = gson.fromJson(json, type);
        Log.d("listDeliveryBoy",""+listDeliveryBoy);
        if (listDeliveryBoy == null) {
            listDeliveryBoy = new ArrayList<>();
        }
        for(int i=0;i<listDeliveryBoy.size();i++){
            DeleiveryBoy currentBoy = listDeliveryBoy.get(i);
            if(currentBoy.getOrder_id().equals(orderId_)){
                layoutDeliveryBoyDetails.setVisibility(View.VISIBLE);
                deliveryboy_name.setText(currentBoy.getDel_boy_name());
                deliveryboy_vehicle.setText(currentBoy.getDel_boy_phone());
                deliveryyboyimage.setImageURI(Uri.parse(currentBoy.getPhotoUrl()));
                if(currentBoy.getArrived())
                    deliveryboy_arivingstatus.setVisibility(View.VISIBLE);
            }
        }
    }

    public void loadData() {
        HashMap<String, String> op = new HashMap<>();
        op.put("vendor_phone", "10");
        String outputreq = gson.toJson(op);

        Log.d("input", outputreq);

        try {
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_recieve).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(outputreq);
            writer.close();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            Log.d("comingdata", sb.toString());
            String o = sb.toString();
            order_Detail = sb.toString();

            loadrecycler();
            dialog.dismiss();

        } catch (MalformedURLException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("MalformedURLException", e.getMessage());
        } catch (ProtocolException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("ProtocolException", e.getMessage());
        } catch (IOException e) {
            dialog.dismiss();
            e.printStackTrace();
            Log.d("IOException", e.getMessage());

        }
    }

    private void loadrecycler() {

        String jsonGet=order_Detail;

        if(jsonGet!=null)
        {
            try {

                JSONObject object1=new JSONObject(jsonGet);
                JSONArray arr= object1.getJSONArray("myorders");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject object=arr.getJSONObject(i);
                    if(object.getString("order_id").equals(orderId_))
                    {
                        orderId.setText(object.getString("order_id"));
                        date.setText(object.getString("date"));
                        time.setText(object.getString("time"));
                        packing_status.setText(("waiting"));
                        deliveryboy_arivingstatus.setText(("waiting"));
                        deliveryboy_vehicle.setText(("waiting"));
                        deliveryboy_otp.setText(object.getString("otp"));
                        totalcost.setText(object.getString("price"));
                        tax.setText(("tax"));
                        deliverycharge.setText(("delivery_charge"));
                        deliveryboy_name.setText(("waiting"));
                        delivery_boy_phone=object.getString("delivery_boy_phone");

                        //set image

                        JSONArray array=object.getJSONArray("items");
                        Log.d("vishnurai", String.valueOf(array));
                        for(int j=0;j<array.length();j++)
                        {
                            JSONObject object2= array.getJSONObject(j);
                            prod_name.add(object2.getString("prod_name"));
                            prod_quantity.add(object2.getString("prod_quan"));
                            int s= Integer.parseInt(object2.getString("prod_quan"))*Integer.parseInt(object2.getString("prod_price"));
                            prod_price.add(String.valueOf(s));
                        }

                        for (int k=0;k<array.length();k++) {
                            order_dataholder List = new order_dataholder(prod_name.get(k),prod_quantity.get(k),prod_price.get(k));
                            Log.d("hello", String.valueOf(List));
                            list.add(List);
                        }
                        adapter = new currentorder_detail_adapter(list,this);
                        historyItems_recyclerView.setAdapter(adapter);
                        adapter=null;
//                        list=new ArrayList<>();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HI","start");
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HI","stop");
        active = false;
    }

}
