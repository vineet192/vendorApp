package com.example.vendor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class remove_item_adapter extends RecyclerView.Adapter<remove_item_adapter.removeitem_ViewHolder> {


    List<order_dataholder> Orderdetail_dataholderList;
    Context context;

    JSONObject obj, object;



    public HashMap<String, String> removedItem = new HashMap<>();
    public List<HashMap> removedItems = new ArrayList<>();
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    String removedJson, removedJson1,temp,temp2;


    public remove_item_adapter(List<order_dataholder> order_dataholderList, Context context) {
        this.Orderdetail_dataholderList = order_dataholderList;
        this.context = context;
    }


    @NonNull
    @Override
    public remove_item_adapter.removeitem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_item_layout, parent, false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        orderid_ = Order_detail.orderId_;
        removedJson = remove_item_frag.removedJson;
        temp= "removed_items"+orderid_;
        temp2="new_orders"+orderid_;


        return new remove_item_adapter.removeitem_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final remove_item_adapter.removeitem_ViewHolder holder, int position) {

        final order_dataholder listItem = Orderdetail_dataholderList.get(position);

        holder.product_name.setText(listItem.getProduct_name());
        holder.product_price.setText(listItem.getProduct_price());


        holder.restore_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Cancel order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Orderdetail_dataholderList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), Orderdetail_dataholderList.size());

                                JSONArray a4 = new JSONArray();
                                JSONArray a5 = new JSONArray();
                                JSONArray array = new JSONArray();


                                if ((sharedPref.contains(temp))) {

                                    removedJson = sharedPref.getString(temp, null);
                                    try {
                                        array = new JSONArray(removedJson);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("on accept", String.valueOf(removedJson));
                                }

                                removedJson1 = sharedPref.getString(temp2, null);
                                try {
                                    JSONObject o3=new JSONObject(removedJson1);
                                    a4=o3.getJSONArray("items");

//                                    for (int i = 0; i < a3.length(); i++) {
//                                        object = a3.getJSONObject(i);
//                                        if (object.getString("order_id").equals(orderid_)) {
//                                            a4 = object.getJSONArray("items");
//                                        }
//
//                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object1 = array.getJSONObject(i);
                                        if (object1.get("product_name").equals(holder.product_name.getText())) {
                                            a4.put(object1);
//                                            for(int j=0;j<array1.length();j++)
//                                            {
//                                                JSONObject ob=array1.getJSONObject(j);
//                                                if(ob.get("order_id").equals(orderid_))
//                                                {
//                                                    JSONArray a=ob.getJSONArray("items");
//                                                    a.put(object1);
//                                                    ob.getJSONArray("items").put(object1);
////                                                    for(int k=0;k<a.length();k++)
////                                                    {
////                                                        JSONObject o=a.getJSONObject(k);
////                                                        if(o.get("prod_name").equals(holder.product_name.getText()))
////                                                        {
////                                                            a.put(o);
////                                                        }
////                                                    }
//                                                }
//                                            }

                                        } else
                                            a5.put(object1);
                                    }

//                                    Log.d("a4", String.valueOf(a4));
//                                    Log.d("a5", String.valueOf(a5));

//                                    for (int i = 0; i < a3.length(); i++) {
//                                        object = a3.getJSONObject(i);
//                                        if (object.getString("order_id").equals(orderid_)) {
//                                            object.remove("items");
//                                            object.put("items", a4);
//                                        }
//
//                                    }

                                    object = new JSONObject(removedJson1);
                                    object.remove("items");
                                    object.put("items", a4);



                                    Log.d("checkobj", String.valueOf(object));
                                    Log.d("a5check", String.valueOf(a5));

                                    String temp5="new_orders"+orderid_;
                                    edit = sharedPref.edit();
                                    edit.putString(temp5, String.valueOf(object));
                                    edit.commit();

                                    edit = sharedPref.edit();
                                    edit.remove(temp);
                                    edit.putString(temp, String.valueOf(a5));
                                    edit.apply();

//                                    edit = sharedPref.edit();
//                                    edit.putString("new_order", String.valueOf(a3));
//                                    edit.apply();

//                                    Log.d("orderstr", sharedPref.getString("new_order", null));
//                                    Log.d("removedstr", sharedPref.getString("removed_items", null));

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

//                                try {
//                                    String jsonGet = sharedPref.getString("new_order", null);
//                                    array = new JSONArray(jsonGet);
//                                    for(int i=0;i<array.length();i++)
//                                    {
//                                        JSONObject ob= array.getJSONObject(i);
//                                        if(ob.getString("order_id").equals(orderid_))
//                                        {
//                                            arr=ob.getJSONArray("items");
//                                            for(int j=0;j<arr.length();j++)
//                                            {
//                                                JSONObject object1= arr.getJSONObject(j);
//                                                sharedList_.add(object1);
//                                            }
//                                            array.getJSONObject(i).remove("items");
//                                            array.getJSONObject(i).put("items",sharedList_);
//                                        }
//                                    }
//                                    edit = sharedPref.edit();
//                                    edit.putString("new_order", String.valueOf(array));
//                                    edit.apply();
//                                } catch (JSONException ex) {
//                                    ex.printStackTrace();
//                                }
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
        return Orderdetail_dataholderList.size();
    }

    public class removeitem_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price, restore_tv;

        public removeitem_ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            restore_tv = itemView.findViewById(R.id.restore_tv);


        }
    }
}
