package com.example.vendor;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class order_history_adapter extends RecyclerView.Adapter<order_history_adapter.history_order_ViewHolder> {


    List<order_dataholder> orderhistory_dataholderList;
    Activity context;

//    private String AUTH_URL = "http://192.168.43.55:3000/auth";


    public order_history_adapter(List<order_dataholder> orderhistory_dataholderList, Activity context) {
        this.orderhistory_dataholderList = orderhistory_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public history_order_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout,parent,false);

        return new history_order_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull order_history_adapter.history_order_ViewHolder holder, int position) {

        order_dataholder ListItem= orderhistory_dataholderList.get(position);

        holder.time.setText(ListItem.getTime());
        holder.date.setText(ListItem.getDate());
        holder.orderID.setText(ListItem.getOrderID());
        holder.total_price.setText(ListItem.getTotal_price());
        holder.order_status.setText(ListItem.getPackage_status());

        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, previous_order_detail.class);
                intent.putExtra("orderId",holder.orderID.getText());
                intent.putExtra("Packing_status",holder.order_status.getText());
                intent.putExtra("full_data", order_history_frag.previousorder_jsonData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (orderhistory_dataholderList.size());
    }


    public class history_order_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID,time,date,total_price,order_status;
        LinearLayout view_detail_tv;

        public history_order_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            total_price=itemView.findViewById(R.id.total_price);
            order_status=itemView.findViewById(R.id.order_status);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);



        }
    }
}
