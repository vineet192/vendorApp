package com.example.vendor.MyIncome.Adapter_Income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vendor.MyIncome.Models_Income.MonthIncomeModel;
import com.example.vendor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthIncomeAdapter extends RecyclerView.Adapter<MonthIncomeAdapter.myHolder> {

    Context context;
    List<MonthIncomeModel> incomeList;

    public MonthIncomeAdapter(Context context, List<MonthIncomeModel> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.income_item_month, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {

        // get data
        String orderId = incomeList.get(position).getOrderId();
        String amount = incomeList.get(position).getAmount();
        String date = incomeList.get(position).getDate();

        //set data
        holder.dateTv.setText(date);
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
        TextView dateTv;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.incomeItem_month_orderId);
            amountTv = itemView.findViewById(R.id.incomeItem_month_amount);
            dateTv = itemView.findViewById(R.id.incomeItem_month_date);

        }
    }

}
