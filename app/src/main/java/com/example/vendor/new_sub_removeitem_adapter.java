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

public class new_sub_removeitem_adapter extends RecyclerView.Adapter<new_sub_removeitem_adapter.sub_removeitem_ViewHolder> {


    List<subscription_dataholder> sub_Orderdetail_dataholderList;
    Context context;

    JSONObject obj, object;




    public HashMap<String, String> removedItem = new HashMap<>();
    public List<HashMap> removedItems = new ArrayList<>();
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    JSONArray a= new JSONArray();

    String temp;


    String removedJson, removedJson1;


    public new_sub_removeitem_adapter(List<subscription_dataholder> sub_Orderdetail_dataholderList, Context context) {
        this.sub_Orderdetail_dataholderList = sub_Orderdetail_dataholderList;
        this.context = context;
    }


    @NonNull
    @Override
    public new_sub_removeitem_adapter.sub_removeitem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_sub_removeditem_layout, parent, false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        orderid_ = new_subs_detail.orderId_;
        removedJson = new_sub_removeditems_frag.removedJson;

        temp = "removed_items" + orderid_;



        return new new_sub_removeitem_adapter.sub_removeitem_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final new_sub_removeitem_adapter.sub_removeitem_ViewHolder holder, int position) {

        final subscription_dataholder listItem = sub_Orderdetail_dataholderList.get(position);

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

                                sub_Orderdetail_dataholderList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), sub_Orderdetail_dataholderList.size());

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

                                removedJson1 = new_subs_detail_frag.order_Detail;
                                try {
                                    JSONObject o3=new JSONObject(removedJson1);
                                    a4=o3.getJSONArray("items");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object1 = array.getJSONObject(i);
                                        if (object1.get("product_name").equals(holder.product_name.getText())) {
                                            a4.put(object1);

                                        } else
                                            a5.put(object1);
                                    }

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
        return sub_Orderdetail_dataholderList.size();
    }

    public class sub_removeitem_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_price, restore_tv;

        public sub_removeitem_ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            restore_tv = itemView.findViewById(R.id.restore_tv);


        }
    }
}
