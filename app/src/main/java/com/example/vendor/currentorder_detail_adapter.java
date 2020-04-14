package com.example.vendor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.order_dataholder;

public class currentorder_detail_adapter extends RecyclerView.Adapter<currentorder_detail_adapter.currentorderdetail_ViewHolder> {


    List<order_dataholder> currentorderdetail_dataholderList;
    Activity context;


    public currentorder_detail_adapter(List<order_dataholder> currentorderdetail_dataholderList, Activity context) {
        this.currentorderdetail_dataholderList = currentorderdetail_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public currentorderdetail_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.current_orderdetail_layout,parent,false);

        return new currentorderdetail_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull currentorderdetail_ViewHolder holder, int position) {

        order_dataholder ListItem= currentorderdetail_dataholderList.get(position);

        holder.product_tprice.setText(ListItem.getProd_total_price());
        holder.product_quantity.setText(ListItem.getProd_quantity());
        holder.product_name.setText(ListItem.getProduct_name());


    }

    @Override
    public int getItemCount() {
        return (currentorderdetail_dataholderList.size());
    }



    public class currentorderdetail_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_quantity,product_name,product_tprice;

        public currentorderdetail_ViewHolder(@NonNull View itemView) {
            super(itemView);


            product_quantity=itemView.findViewById(R.id.product_quantity);
            product_name=itemView.findViewById(R.id.product_name);
            product_tprice=itemView.findViewById(R.id.product_tprice);

        }
    }
}
