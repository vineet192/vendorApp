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

public class subscription_history_adapter extends RecyclerView.Adapter<subscription_history_adapter.history_subs_ViewHolder> {


    List<subscription_dataholder> subshistory_dataholderList;
    Activity context;

//    private String AUTH_URL = "http://192.168.43.55:3000/auth";


    public subscription_history_adapter(List<subscription_dataholder> subshistory_dataholderList, Activity context) {
        this.subshistory_dataholderList = subshistory_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public subscription_history_adapter.history_subs_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_subs_layout,parent,false);

        return new subscription_history_adapter.history_subs_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull subscription_history_adapter.history_subs_ViewHolder holder, int position) {

        subscription_dataholder ListItem= subshistory_dataholderList.get(position);

        holder.enddate.setText(ListItem.getEnddate());
        holder.startdate.setText(ListItem.getStartdate());
        holder.orderID.setText(ListItem.getOrderID());
        holder.total_price.setText(ListItem.getTotal_price());
        holder.order_status.setText(ListItem.getPackage_status());
        holder.subs_type.setText(ListItem.getSubscription_type());


        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, previous_subs_detail.class);
                intent.putExtra("orderId",holder.orderID.getText());
                intent.putExtra("Packing_status",holder.order_status.getText());
                intent.putExtra("full_json_data", subscription_history_frag.previousorder_jsonData);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (subshistory_dataholderList.size());
    }


    public class history_subs_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID,enddate,startdate,total_price,order_status,subs_type;
        LinearLayout view_detail_tv;

        public history_subs_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            startdate=itemView.findViewById(R.id.startdate);
            enddate=itemView.findViewById(R.id.enddate);
            total_price=itemView.findViewById(R.id.total_price);
            order_status=itemView.findViewById(R.id.order_status);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);
            subs_type=itemView.findViewById(R.id.subs_type);

        }
    }
}
