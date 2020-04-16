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

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class new_sub_detail_adapter extends RecyclerView.Adapter<new_sub_detail_adapter.newsubsorderdetail_ViewHolder> {


    List<neworders_model> newsubsdetail_dataholderList;
    Activity context;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    JSONArray a=new JSONArray();
    JSONArray a2=new JSONArray();
    JSONArray array, arr;
    JSONObject obj, object;
    JSONArray namearr=new JSONArray();
    JSONArray quanarr=new JSONArray();


    static String removedjson,temp,temp2;



    public new_sub_detail_adapter(List<neworders_model> newsubsdetail_dataholderList, Activity context) {
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

        return new new_sub_detail_adapter.newsubsorderdetail_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull new_sub_detail_adapter.newsubsorderdetail_ViewHolder holder, int position) {

        neworders_model ListItem= newsubsdetail_dataholderList.get(position);

        holder.product_price.setText(ListItem.getProd_quan());
        holder.product_name.setText(ListItem.getProd_name());

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
                                    String jsonGet=sharedPref.getString(temp2,null);
                                    obj = new JSONObject(jsonGet);

                                    JSONArray a1 = new JSONArray();
                                    JSONArray quanarr1=new JSONArray();
                                    JSONArray namearr1=new JSONArray();

                                    arr = obj.getJSONArray("name");
                                    a1=obj.getJSONArray("quan");

                                    for (int j = 0; j < arr.length(); j++) {
                                        if (arr.getString(j).equals(holder.product_name.getText())) {

                                            namearr.put(arr.getString(j));

                                            quanarr.put(a1.getString(j));

                                            HashMap<String,JSONArray> jsonObject=new HashMap<>();
                                            jsonObject.put("name",namearr);
                                            jsonObject.put("quan",quanarr);

                                            edit = sharedPref.edit();
                                            edit.remove(temp);
                                            edit.putString(temp, String.valueOf(jsonObject));
                                            edit.commit();

                                        } else {
                                            namearr1.put(arr.getString(j));

                                            quanarr1.put(a1.getString(j));

                                        }

                                    }
                                    HashMap<String,JSONArray> jsonObject1=new HashMap<>();
                                    jsonObject1.put("name",namearr1);
                                    jsonObject1.put("quan",quanarr1);
                                    edit = sharedPref.edit();
                                    edit.remove(temp2);
                                    edit.putString(temp2, String.valueOf(jsonObject1));
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
