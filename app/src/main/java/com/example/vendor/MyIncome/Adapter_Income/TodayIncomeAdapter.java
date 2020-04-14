package com.example.vendor.MyIncome.Adapter_Income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vendor.MyIncome.Models_Income.TodayIncomeModel;
import com.example.vendor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodayIncomeAdapter extends RecyclerView.Adapter<TodayIncomeAdapter.myHolder> {

    Context context;
    List<TodayIncomeModel> incomeList;

    public TodayIncomeAdapter(Context context, List<TodayIncomeModel> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }


    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.income_item_today, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {

        // get data
        String orderId = incomeList.get(position).getOrderId();
        String amount = incomeList.get(position).getAmount();

        //set data
        holder.orderIdTv.setText(orderId);
        holder.amountTv.setText(amount);

    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    // view holder class
    class myHolder extends RecyclerView.ViewHolder{

        TextView orderIdTv;
        TextView amountTv;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.incomeItem_today_orderId);
            amountTv = itemView.findViewById(R.id.incomeItem_today_amount);

        }
    }
}
