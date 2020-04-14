package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.NewOrders;
import com.example.Models.OrderDescriptionResponse;
import com.example.vendor.R;


import java.util.List;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.ViewHolder>  {

    private LayoutInflater mInflater;
    private List<OrderDescriptionResponse> myOrderList;
    private Context context;
    private OnItemClickListener mListener;

    public NewOrderAdapter(Context context, List myOrderList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.myOrderList=myOrderList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId;
        TextView orderTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderTotal = itemView.findViewById(R.id.orderTotal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_new_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderId.setText("Order Id : "+myOrderList.get(position).getOrder_id());
        holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

}
