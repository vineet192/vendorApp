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


    List<neworders_model> Orderdetail_dataholderList;
    Context context;


    public HashMap<String, String> removedItem = new HashMap<>();
    public List<HashMap> removedItems = new ArrayList<>();
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    String removedjson, removedJson1,temp,temp2;

    JSONArray namearr=new JSONArray();
    JSONArray quanarr=new JSONArray();
    JSONArray namearr1=new JSONArray();
    JSONArray quanarr1=new JSONArray();
    JSONArray namearr2=new JSONArray();
    JSONArray quanarr2=new JSONArray();


    public remove_item_adapter(List<neworders_model> order_dataholderList, Context context) {
        this.Orderdetail_dataholderList = order_dataholderList;
        this.context = context;
    }


    @NonNull
    @Override
    public remove_item_adapter.removeitem_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_item_layout, parent, false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        orderid_ = Order_detail.orderId_;
        temp= "removed_items"+orderid_;
        temp2="new_orders"+orderid_;

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

        if ((sharedPref.contains(temp2))) {

            removedjson = sharedPref.getString(temp, null);
            try {
                JSONObject object3=new JSONObject(removedjson);
                namearr1=object3.getJSONArray("name");
                quanarr1=object3.getJSONArray("quan");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return new remove_item_adapter.removeitem_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final remove_item_adapter.removeitem_ViewHolder holder, int position) {

        final neworders_model listItem = Orderdetail_dataholderList.get(position);

        holder.product_name.setText(listItem.getProd_name());
        holder.product_quan.setText(listItem.getProd_quan());


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


                                try {
                                    for (int i = 0; i < namearr.length(); i++) {
                                        if (namearr.getString(i).equals(holder.product_name.getText())) {
                                            namearr1.put(namearr.getString(i));
                                            quanarr1.put(quanarr.getString(i));
                                            JSONObject object=new JSONObject();
                                            object.put("name",namearr1);
                                            object.put("quan",quanarr1);
                                            edit = sharedPref.edit();
                                            edit.putString(temp2, String.valueOf(object));
                                            edit.commit();

                                        } else {
                                            namearr2.put(namearr.getString(i));
                                            quanarr2.put(quanarr.getString(i));
                                        }
                                    }

                                    JSONObject jsonObject=new JSONObject();
                                    jsonObject.put("name",namearr2);
                                    jsonObject.put("quan",quanarr2);
                                    edit = sharedPref.edit();
                                    edit.remove(temp);
                                    edit.putString(temp, String.valueOf(jsonObject));
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
        return Orderdetail_dataholderList.size();
    }

    public class removeitem_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, product_quan, restore_tv;

        public removeitem_ViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_quan = itemView.findViewById(R.id.product_quan);
            restore_tv = itemView.findViewById(R.id.restore_tv);


        }
    }
}
