package com.example.vendor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class new_sub_detail_adapter extends RecyclerView.Adapter<new_sub_detail_adapter.newsubsorderdetail_ViewHolder> {


    List<subscription_dataholder> newsubsdetail_dataholderList;
    Activity context;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    JSONArray a= new JSONArray();

    JSONArray array, arr;
    JSONObject obj, object;


    static String removedjson,temp;



    public new_sub_detail_adapter(List<subscription_dataholder> newsubsdetail_dataholderList, Activity context) {
        this.newsubsdetail_dataholderList = newsubsdetail_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public new_sub_detail_adapter.newsubsorderdetail_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.new_sub_order_detail_layout,parent,false);

        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        orderid_ = new_subs_detail.orderId_;

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

        return new new_sub_detail_adapter.newsubsorderdetail_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull new_sub_detail_adapter.newsubsorderdetail_ViewHolder holder, int position) {

        subscription_dataholder ListItem= newsubsdetail_dataholderList.get(position);

        holder.product_price.setText(ListItem.getProduct_price());
        holder.product_name.setText(ListItem.getProduct_name());

        holder.remove_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Cancel order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                newsubsdetail_dataholderList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), newsubsdetail_dataholderList.size());


                                try {
                                    String jsonGet = new_subs_detail_frag.order_Detail;
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
                                        } else {

                                            a1.put(object1);

                                        }

                                    }
                                    obj.put("items", a1);
                                    String temp2 = "new_orders" + orderid_;
                                    edit = sharedPref.edit();
                                    edit.putString(temp2, String.valueOf(obj));
                                    edit.commit();

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

//                                try {
//                                    String jsonGet = sharedPref.getString("new_subscription", null);
////                                    obj=new JSONObject(jsonGet);
////                                    array=obj.getJSONArray("");
//                                    array = new JSONArray(jsonGet);
//                                    JSONArray a1= new JSONArray();
//
//                                    for (int i = 0; i < array.length(); i++) {
//                                        JSONObject ob = array.getJSONObject(i);
//                                        if (ob.getString("order_id").equals(orderid_)) {
//                                            arr = ob.getJSONArray("items");
//                                            Log.d("without removing", String.valueOf(arr));
//
//                                            for (int j = 0; j < arr.length(); j++) {
//                                                JSONObject object1 = arr.getJSONObject(j);
//                                                if (object1.get("prod_name").equals(holder.product_name.getText())) {
////                                                    HashMap<String, JSONObject> t = new HashMap<>();
////                                                    t.put("category_name", (JSONObject) object1.get("category_name"));
////                                                    t.put("prod_name", (JSONObject) object1.get("prod_name"));
////                                                    t.put("prod_id", (JSONObject) object1.get("prod_id"));
////                                                    t.put("prod_price", (JSONObject) object1.get("prod_price"));
////                                                    t.put("prod_rating", (JSONObject) object1.get("prod_rating"));
////                                                    t.put("prod_desc", (JSONObject) object1.get("prod_desc"));
////                                                    t.put("prod_img", (JSONObject) object1.get("prod_img"));
////                                                    t.put("quantity", (JSONObject) object1.get("quantity"));
////                                                    t.put("check", (JSONObject) object1.get("check"));
//                                                    a.put(object1);
//
//                                                    edit = sharedPref.edit();
//                                                    edit.putString("sub_removed_items", String.valueOf(a));
//                                                    edit.commit();
//
////                                                    sharedList.add(object1);
//                                                } else {
////                                                    JSONObject t = new JSONObject();
//
////                                                    t.put("category_name", object1.get("category_name"));
////                                                    t.put("prod_name", object1.get("prod_name"));
////                                                    t.put("prod_id", object1.get("prod_id"));
////                                                    t.put("prod_price", object1.get("prod_price"));
////                                                    t.put("prod_rating", object1.get("prod_rating"));
////                                                    t.put("prod_desc", object1.get("prod_desc"));
////                                                    t.put("prod_img", object1.get("prod_img"));
////                                                    t.put("quantity", object1.get("quantity"));
////                                                    t.put("check", object1.get("check"));
//
//                                                    a1.put(object1);
//
//                                                }
//
//                                            }
//                                            ob.put("items",a1);
//                                            edit = sharedPref.edit();
//                                            edit.putString("new_subscription", String.valueOf(array));
//                                            edit.commit();
//
////                                            array.getJSONObject(i).remove("items");
////                                            array.getJSONObject(i).put("items", _sharedList_);
////                                            Log.d("shared list", String.valueOf(_sharedList_));
////                                            Log.d("modified array", String.valueOf(array));
////                                            Log.d("shared list remove", String.valueOf(sharedList));
//                                        }
//                                        Log.d("array data", String.valueOf(array));
//                                    }
////                                    edit = sharedPref.edit();
////                                    edit.putString("new_order", String.valueOf(array));
////                                    edit.apply();
//
////                                    Log.d("modified data", sharedPref.getString("new_order", null));
////                                    Log.d("removed_list", sharedPref.getString("removed_items", null));
//
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
        return (newsubsdetail_dataholderList.size());
    }



    public class newsubsorderdetail_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_price,product_name,remove_tv;

        public newsubsorderdetail_ViewHolder(@NonNull View itemView) {
            super(itemView);


            product_price=itemView.findViewById(R.id.product_price);
            product_name=itemView.findViewById(R.id.product_name);
            remove_tv=itemView.findViewById(R.id.remove_tv);

        }
    }
}
