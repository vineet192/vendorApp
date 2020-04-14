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

public class previous_removed_item_adapter  extends RecyclerView.Adapter<previous_removed_item_adapter.previousorderremoved_ViewHolder> {


    List<order_dataholder> previousorremoved_dataholderList;
    Activity context;


    public previous_removed_item_adapter(List<order_dataholder> previousorremoved_dataholderList, Activity context) {
        this.previousorremoved_dataholderList = previousorremoved_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public previous_removed_item_adapter.previousorderremoved_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.previous_order_removed_layout,parent,false);

        return new previous_removed_item_adapter.previousorderremoved_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull previous_removed_item_adapter.previousorderremoved_ViewHolder holder, int position) {

        order_dataholder ListItem= previousorremoved_dataholderList.get(position);

        holder.product_tprice.setText(ListItem.getProd_total_price());
        holder.product_quantity.setText(ListItem.getProd_quantity());
        holder.product_name.setText(ListItem.getProduct_name());


    }

    @Override
    public int getItemCount() {
        return (previousorremoved_dataholderList.size());
    }



    public class previousorderremoved_ViewHolder extends RecyclerView.ViewHolder {

        TextView product_quantity,product_name,product_tprice;

        public previousorderremoved_ViewHolder(@NonNull View itemView) {
            super(itemView);


            product_quantity=itemView.findViewById(R.id.product_quantity);
            product_name=itemView.findViewById(R.id.product_name);
            product_tprice=itemView.findViewById(R.id.product_tprice);

        }
    }
}
