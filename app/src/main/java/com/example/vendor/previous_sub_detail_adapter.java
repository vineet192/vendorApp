package com.example.vendor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class previous_sub_detail_adapter extends RecyclerView.Adapter<previous_sub_detail_adapter.previoussubsorderdetail_ViewHolder> {


    List<subscription_dataholder> newsubsdetail_dataholderList;
    Activity context;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;

    JSONArray a= new JSONArray();

    JSONArray array, arr;
    JSONObject obj, object;


    static String removedjson,temp;



    public previous_sub_detail_adapter(List<subscription_dataholder> newsubsdetail_dataholderList, Activity context) {
        this.newsubsdetail_dataholderList = newsubsdetail_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public previoussubsorderdetail_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.previous_sub_order_detail_layout,parent,false);

//        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
//        orderid_ = new_subs_detail.orderId_;
//
//        temp = "removed_items" + orderid_;
//
//
//        if ((sharedPref.contains(temp))) {
//
//            removedjson = sharedPref.getString(temp, null);
//            try {
//                a = new JSONArray(removedjson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.d("on accept", String.valueOf(a));
//        }

        return new previous_sub_detail_adapter.previoussubsorderdetail_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull previous_sub_detail_adapter.previoussubsorderdetail_ViewHolder holder, int position) {

        subscription_dataholder ListItem= newsubsdetail_dataholderList.get(position);

        holder.product_price.setText(ListItem.getProduct_price());
        holder.product_name.setText(ListItem.getProduct_name());


    }

    @Override
    public int getItemCount() {
        return (newsubsdetail_dataholderList.size());
    }



    public class previoussubsorderdetail_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_price,product_name;

        public previoussubsorderdetail_ViewHolder(@NonNull View itemView) {
            super(itemView);


            product_price=itemView.findViewById(R.id.product_price);
            product_name=itemView.findViewById(R.id.product_name);

        }
    }
}
