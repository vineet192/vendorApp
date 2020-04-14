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


    List<neworders_model> orderdetail_dataholderList;
    Context context;

    JSONArray array, arr;
    JSONObject obj, object;

    public HashMap<String, String> removedItem = new HashMap<>();
    public List<HashMap> removedItems = new ArrayList<>();
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    String temp,temp2;
    JSONArray namearr=new JSONArray();
    JSONArray quanarr=new JSONArray();

    JSONArray a=new JSONArray();
    JSONArray a2=new JSONArray();


    static String removedjson;

    static List<JSONObject> sharedList = new ArrayList<>();


    public orderdetail_adapter(List<neworders_model> order_dataholderList, Context context) {
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
        temp2="new_orders" + orderid_;


        if ((sharedPref.contains(temp))) {

            removedjson = sharedPref.getString(temp, null);
            try {
                JSONObject object3=new JSONObject(removedjson);
                namearr=object3.getJSONArray("name");
                quanarr=object3.getJSONArray("quan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new orderdetail_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final orderdetail_ViewHolder holder, int position) {

        final neworders_model listItem = orderdetail_dataholderList.get(position);

        holder.product_name.setText(listItem.getProd_name());
        holder.product_quan.setText(listItem.getProd_quan());


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
                                    String jsonGet=sharedPref.getString(temp2,null);
                                    obj = new JSONObject(jsonGet);

                                    JSONArray a1 = new JSONArray();
                                    JSONArray quanarr1=new JSONArray();
                                    JSONArray namearr1=new JSONArray();

                                    arr = obj.getJSONArray("name");
                                    a1=obj.getJSONArray("quan");
                                    JSONObject jsonObject1=new JSONObject();

                                    for (int j = 0; j < arr.length(); j++) {
                                        if (arr.getString(j).equals(holder.product_name.getText())) {

                                            namearr.put(arr.getString(j));

                                            quanarr.put(a1.getString(j));

                                            JSONObject jsonObject=new JSONObject();
                                            jsonObject.put("name",namearr);
                                            jsonObject.put("quan",quanarr);

                                            edit = sharedPref.edit();
                                            edit.putString(temp, String.valueOf(jsonObject));
                                            edit.commit();

                                        } else {
                                            namearr1.put(arr.getString(j));

                                            quanarr1.put(a1.getString(j));

                                        }

                                    }
                                    jsonObject1.put("name",namearr1);
                                    jsonObject1.put("quan",quanarr1);
                                    a2.put(jsonObject1);
                                    edit = sharedPref.edit();
                                    edit.putString(temp2, String.valueOf(jsonObject1));
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
        return orderdetail_dataholderList.size();
    }

    public class orderdetail_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_quan, remove_tv;

        public orderdetail_ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_quan = itemView.findViewById(R.id.product_quan);
            remove_tv = itemView.findViewById(R.id.remove_tv);


        }
    }
}
