package com.example.vendor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class orderdetail_adapter extends RecyclerView.Adapter<orderdetail_adapter.orderdetail_ViewHolder> {


    List<order_dataholder> orderdetail_dataholderList;
    Context context;

    JSONArray array, arr;
    JSONObject obj, object;

    public HashMap<String, String> removedItem = new HashMap<>();
    public List<HashMap> removedItems = new ArrayList<>();
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    JSONArray a = new JSONArray();

    String temp;


    static String removedjson;

    static List<JSONObject> sharedList = new ArrayList<>();


    public orderdetail_adapter(List<order_dataholder> order_dataholderList, Context context) {
        this.orderdetail_dataholderList = order_dataholderList;
        this.context = context;
    }


    @NonNull
    @Override
    public orderdetail_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout, parent, false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        orderid_ = Order_detail.orderId_;

        temp = "removed_items" + orderid_;


        if ((sharedPref.contains(temp))) {

            removedjson = sharedPref.getString(temp, null);
            try {
                a = new JSONArray(removedjson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("on accept", String.valueOf(a));
        }

//                JSONArray array = new JSONArray(jsonGet);
//
//
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject obj = array.getJSONObject(i);
//
//                    HashMap<String, Object> t = new HashMap<>();
//                    t.put("category_name",  obj.get("category_name"));
//                    t.put("prod_name",  obj.get("prod_name"));
//                    t.put("prod_id",  obj.get("prod_id"));
//                    t.put("prod_price",  obj.get("prod_price"));
//                    t.put("prod_rating",  obj.get("prod_rating"));
//                    t.put("prod_desc",  obj.get("prod_desc"));
//                    t.put("prod_img",  obj.get("prod_img"));
//                    t.put("quantity",  obj.get("quantity"));
//                    t.put("check",  obj.get("check"));
//
//                    sharedList.add(obj);
//
//
//                    Log.d("list", String.valueOf(sharedList));


        return new orderdetail_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final orderdetail_ViewHolder holder, int position) {

        final order_dataholder listItem = orderdetail_dataholderList.get(position);

        holder.product_name.setText(listItem.getProduct_name());
        holder.product_price.setText(listItem.getProduct_price());


        holder.remove_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Cancel order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                orderdetail_dataholderList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), orderdetail_dataholderList.size());


                                try {
                                    String jsonGet = order_detail_frag.order_Detail;
                                    obj = new JSONObject(jsonGet);

                                    JSONArray a1 = new JSONArray();

                                    arr = obj.getJSONArray("items");
                                    Log.d("without removing", String.valueOf(arr));

                                    for (int j = 0; j < arr.length(); j++) {
                                        JSONObject object1 = arr.getJSONObject(j);
                                        if (object1.get("product_name").equals(holder.product_name.getText())) {

                                            a.put(object1);

                                            edit = sharedPref.edit();
                                            edit.putString(temp, String.valueOf(a));
                                            edit.commit();

                                            Log.d("checkme", String.valueOf(a));
                                        } else {

                                            a1.put(object1);

                                        }

                                    }
                                    obj.put("items", a1);
                                    Log.d("123", String.valueOf(object));

                                    String temp2 = "new_orders" + orderid_;
                                    edit = sharedPref.edit();
                                    edit.putString(temp2, String.valueOf(obj));
                                    edit.commit();

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return orderdetail_dataholderList.size();
    }

    public class orderdetail_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price, remove_tv;

        public orderdetail_ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            remove_tv = itemView.findViewById(R.id.remove_tv);


        }
    }
}
