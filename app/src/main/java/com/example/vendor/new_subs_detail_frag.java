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

public class new_subs_detail_frag extends Fragment
{

    RecyclerView new_subs_detail_recycler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> productnamelist = new ArrayList<>();
    ArrayList<String> productquanlist = new ArrayList<>();
    private List<neworders_model> list = new ArrayList<>();

    public static JSONArray array, arr, namearr, quanarr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_, StartDate, EndDate, TotalPrice;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView startDate, endDate, totalcost, deliverycharge, tax, reject_tv;
    Button accept_btn;

    static String order_Detail;

    Gson gson = new Gson();

    ProgressDialog dialog;

    String url_sent = "https://gocoding.azurewebsites.net/suscriptionresponse/";

    String temp2, temp;

    String response;
    private SharedPreferences vendorPref;
    private String vendorPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.frag_new_subs_detail, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        vendorPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        vendorPhone = vendorPref.getString(getString(R.string.vendor_phone_key),null);
        new_subs_detail_recycler = rootView.findViewById(R.id.new_subs_detail_recycler);

        new_subs_detail_recycler.setHasFixedSize(true);
        new_subs_detail_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        startDate = rootView.findViewById(R.id.startDate);
        endDate = rootView.findViewById(R.id.endDate);
        tax = rootView.findViewById(R.id.tax);
        totalcost = rootView.findViewById(R.id.totalcost);
        deliverycharge = rootView.findViewById(R.id.deliverycharge);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);

        orderid_ = getActivity().getIntent().getStringExtra("OrderId");

        dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        temp = "removed_items" + orderid_;
        temp2 = "new_orders" + orderid_;

        Toast.makeText(getContext(), "not stored", Toast.LENGTH_LONG).show();
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
                requestObject.addProperty("sorder_id", orderid_);
                requestObject.addProperty("vendor_phone", vendorPhone);
                JsonArray items = new JsonArray();
                for (neworders_model product : list)
                {
                    items.add(product.getProd_name());
                }
                requestObject.add("items", items);
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
                requestObject.addProperty("sorder_id", orderid_);
                requestObject.addProperty("vendor_phone", vendorPhone);
                JsonArray items = new JsonArray();
                requestObject.add("items", items);
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
            String jsonGet = sharedPref.getString("newsubnotify", null);
            JSONObject ob = new JSONObject(jsonGet);

            Log.d("second", jsonGet);

            JSONArray array = ob.getJSONArray("list");
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                if (orderid_.equals(object.getString("order_id"))) ;
                {
                    startDate.setText(object.getString("date"));
                    endDate.setText(object.getString("time"));
                    deliverycharge.setText(("delivery_charge"));
                    tax.setText(("tax"));
                    totalcost.setText(("total_price"));


                    namearr = object.getJSONArray("total_order");
                    quanarr = object.getJSONArray("quantities");

                    if (!sharedPref.contains(temp2))
                    {
                        HashMap<String, JSONArray> object2 = new HashMap<>();
                        object2.put("name", namearr);
                        object2.put("quan", quanarr);

                        edit = sharedPref.edit();
                        edit.putString(temp2, String.valueOf(object2));
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
            StartDate = startDate.getText().toString();
            EndDate = endDate.getText().toString();
            TotalPrice = totalcost.getText().toString();

            for (int i = 0; i < arr.length(); i++)
            {
                neworders_model List = new neworders_model(productnamelist.get(i), productquanlist.get(i));
                list.add(List);
            }
            adapter = new new_sub_detail_adapter(list, getActivity());
            new_subs_detail_recycler.setAdapter(adapter);
            adapter = null;
//            list = new ArrayList<>();
            dialog.dismiss();


        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }

}
