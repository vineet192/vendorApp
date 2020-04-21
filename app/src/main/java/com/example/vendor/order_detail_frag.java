package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Constants.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class order_detail_frag extends Fragment
{

    RecyclerView order_detail_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productquanlist = new ArrayList<>();
    private List<neworders_model> list = new ArrayList<>();
    private SharedPreferences vendorPref;

    public static JSONArray array, arr, namearr, quanarr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_, date_order, time_order, totalprice_order;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView date, time, totalcost, deliverycharge, tax, reject_tv;
    Button accept_btn;

    static String order_Detail;
    String url_sent = "https://gocoding.azurewebsites.net/vendorresponse/";

    Gson gson = new Gson();

    String response;

    ProgressDialog dialog;

    String temp2;
    private String vendorPhone;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.frag_order_details, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        order_detail_recycler = rootView.findViewById(R.id.order_detail_recycler);

        order_detail_recycler.setHasFixedSize(true);
        order_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        vendorPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        vendorPhone = vendorPref.getString(getString(R.string.vendor_phone_key),null);
        date = rootView.findViewById(R.id.date);
        time = rootView.findViewById(R.id.time);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        orderid_ = Order_detail.orderId_;

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        temp2 = "new_orders" + orderid_;

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                loadrecycler();
                dialog.dismiss();
            }
        }, 200);


        accept_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ProgressDialog pdialog;
                pdialog = new ProgressDialog(getContext()); // this = YourActivity
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setTitle("Loading");
                pdialog.setMessage("Loading. Please wait...");
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                JsonObject requestObject = new JsonObject();
                requestObject.addProperty("order_id", orderid_);
                requestObject.addProperty("vendor_phone", vendorPhone);
                JsonArray items = new JsonArray();
                JsonArray quantity = new JsonArray();
                for (neworders_model product : list)
                {

                    items.add(product.getProd_name());
                    quantity.add(product.getProd_quan());
                }
                requestObject.add("items", items);
                requestObject.add("quantity", quantity);
                System.out.println("Request object is" + requestObject.toString());
                Call<JsonObject> call = apiInterface.getSubscriptionResponse(requestObject);

                call.enqueue(new Callback<JsonObject>()
                {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response)
                    {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Accept successfull", Toast.LENGTH_SHORT).show();
                            pdialog.dismiss();
                        } else
                        {
                            Toast.makeText(getContext(), "Accept error", Toast.LENGTH_SHORT).show();
                            pdialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t)
                    {
                        Toast.makeText(getContext(), "Accept error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pdialog.dismiss();
                    }
                });
            }
        });

        reject_tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                ProgressDialog pdialog;
                pdialog = new ProgressDialog(getContext()); // this = YourActivity
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setTitle("Loading");
                pdialog.setMessage("Loading. Please wait...");
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
                Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gocoding.azurewebsites.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                JsonObject requestObject = new JsonObject();
                requestObject.addProperty("order_id", orderid_);
                requestObject.addProperty("vendor_phone", vendorPhone);
                JsonArray items = new JsonArray();
                JsonArray quantity = new JsonArray();
                requestObject.add("items", items);
                requestObject.add("quantity", quantity);
                System.out.println("Request object is" + requestObject.toString());
                Call<JsonObject> call = apiInterface.getSubscriptionResponse(requestObject);

                call.enqueue(new Callback<JsonObject>()
                {

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response)
                    {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Reject successfull", Toast.LENGTH_SHORT).show();
                            pdialog.dismiss();
                        } else
                        {
                            Toast.makeText(getContext(), "Reject error", Toast.LENGTH_SHORT).show();
                            pdialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t)
                    {
                        Toast.makeText(getContext(), "Accept error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pdialog.dismiss();
                    }
                });
            }
        });

        return rootView;

    }

    public void loadrecycler()
    {
        try
        {
            String jsonGet = sharedPref.getString("newordernotify", null);
            JSONObject ob = new JSONObject(jsonGet);

            Log.d("second", jsonGet);

            JSONArray array = ob.getJSONArray("list");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                if (orderid_.equals(object.getString("order_id"))) ;
                {
                    date.setText(object.getString("date"));
                    time.setText(object.getString("time"));
                    deliverycharge.setText(("delivery_charge"));
                    tax.setText(("tax"));
                    totalcost.setText(("total_price"));

                    namearr = object.getJSONArray("total_order");
                    quanarr = object.getJSONArray("quantity");

                    if (!sharedPref.contains(temp2))
                    {
                        HashMap<String, JSONArray> hashMap = new HashMap<>();
                        hashMap.put("name", namearr);
                        hashMap.put("quan", quanarr);

                        edit = sharedPref.edit();
                        edit.putString(temp2, String.valueOf(hashMap));
                        edit.commit();
                    }

                    String str = sharedPref.getString(temp2, null);
                    JSONObject object1 = new JSONObject(str);
                    arr = object1.getJSONArray("name");
                    for (int j = 0; j < arr.length(); j++)
                    {
                        productnamelist.add(arr.getString(j));
                    }
                    arr = object1.getJSONArray("quan");
                    for (int j = 0; j < arr.length(); j++)
                    {
                        productquanlist.add(arr.getString(j));
                    }
                }
            }
            date_order = date.getText().toString();
            time_order = time.getText().toString();
            totalprice_order = totalcost.getText().toString();

            for (int i = 0; i < arr.length(); i++)
            {
                neworders_model List = new neworders_model(productnamelist.get(i), productquanlist.get(i));
                list.add(List);
            }
            adapter = new orderdetail_adapter(list, getActivity());
            order_detail_recycler.setAdapter(adapter);
            adapter = null;
//            list = new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
}
