package com.example.vendor;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class current_subscription_frag_adapter extends RecyclerView.Adapter<current_subscription_frag_adapter.current_subs_ViewHolder> {


    List<subscription_dataholder> currentsubs_dataholderList;
    Activity context;


    public current_subscription_frag_adapter(List<subscription_dataholder> currentsubs_dataholderList, Activity context) {
        this.currentsubs_dataholderList = currentsubs_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public current_subscription_frag_adapter.current_subs_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_subscription,parent,false);

        return new current_subscription_frag_adapter.current_subs_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull current_subscription_frag_adapter.current_subs_ViewHolder holder, int position) {

        subscription_dataholder ListItem= currentsubs_dataholderList.get(position);

        holder.enddate.setText(ListItem.getEnddate());
        holder.startdate.setText(ListItem.getStartdate());
        holder.orderID.setText(ListItem.getOrderID());
//        holder.deliveryboy_arivingtime.setText(ListItem.deliveryboy_arivingtime);
//        holder.deliveryboy_otp.setText(ListItem.getDeliveryboy_otp());
//        holder.deliveryboy_phone.setText(ListItem.getDeliveryboy_phone());
        holder.total_price.setText(ListItem.getTotal_price());
        holder.typeof_subscription.setText(ListItem.getSubscription_type());



        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context, currentsubs_detail.class);
                i.putExtra("orderId",holder.orderID.getText().toString());
                i.putExtra("orderDetail", current_subscription_frag.order_Detail);
                Log.d("orderIDAdapter",""+holder.orderID.getText().toString());
                Log.d("order_DetailAdapter",""+current_subscription_frag.order_Detail);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (currentsubs_dataholderList.size());
    }

    public class current_subs_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID,enddate,startdate,deliveryboy_arivingtime,total_price,deliveryboy_otp,deliveryboy_phone,typeof_subscription;
        Button process_btn;
        LinearLayout view_detail_tv;

        public current_subs_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            startdate=itemView.findViewById(R.id.startdate);
            enddate=itemView.findViewById(R.id.enddate);
            total_price=itemView.findViewById(R.id.total_price);
//            deliveryboy_arivingtime=itemView.findViewById(R.id.deliveryboy_arivingtime);
//            deliveryboy_otp=itemView.findViewById(R.id.deliveryboy_otp);
//            deliveryboy_phone=itemView.findViewById(R.id.deliveryboy_phone);
            process_btn=itemView.findViewById(R.id.process_btn);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);
            typeof_subscription=itemView.findViewById(R.id.typeof_subscription);



        }
    }
}
