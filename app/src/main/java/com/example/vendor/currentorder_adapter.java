package com.example.vendor;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.order_dataholder;


public class currentorder_adapter extends RecyclerView.Adapter<currentorder_adapter.current_order_ViewHolder> {


    List<order_dataholder> currentorder_dataholderList;
    Activity context;


    public currentorder_adapter(List<order_dataholder> currentorder_dataholderList, Activity context) {
        this.currentorder_dataholderList = currentorder_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public current_order_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_orders,parent,false);

        return new current_order_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull currentorder_adapter.current_order_ViewHolder holder, int position) {

        order_dataholder ListItem= currentorder_dataholderList.get(position);

        holder.time.setText(ListItem.getTime());
        holder.date.setText(ListItem.getDate());
        holder.orderID.setText(ListItem.getOrderID());
//        holder.deliveryboy_arivingtime.setText(ListItem.deliveryboy_arivingtime);
//        holder.deliveryboy_otp.setText(ListItem.getDeliveryboy_otp());
//        holder.deliveryboy_phone.setText(ListItem.getDeliveryboy_phone());
        holder.total_price.setText(ListItem.getTotal_price());


        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context, currentorder_detail.class);
                i.putExtra("orderId",holder.orderID.getText().toString());
                i.putExtra("orderDetail", current_order_frag.order_Detail);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (currentorder_dataholderList.size());
    }

    public class current_order_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID,time,date,deliveryboy_arivingtime,total_price,deliveryboy_otp,deliveryboy_phone;
        Button process_btn;
        LinearLayout view_detail_tv;

        public current_order_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            total_price=itemView.findViewById(R.id.total_price);
//            deliveryboy_arivingtime=itemView.findViewById(R.id.deliveryboy_arivingtime);
//            deliveryboy_otp=itemView.findViewById(R.id.deliveryboy_otp);
//            deliveryboy_phone=itemView.findViewById(R.id.deliveryboy_phone);
            process_btn=itemView.findViewById(R.id.process_btn);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);



        }
    }
}
