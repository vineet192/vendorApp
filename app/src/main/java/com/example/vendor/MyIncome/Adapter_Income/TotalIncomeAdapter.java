package com.example.vendor.MyIncome.Adapter_Income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vendor.MyIncome.Models_Income.TotalIncomeModel;
import com.example.vendor.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TotalIncomeAdapter extends RecyclerView.Adapter<TotalIncomeAdapter.myHolder> {

    Context context;
    List<TotalIncomeModel> incomeList;

    public TotalIncomeAdapter(Context context, List<TotalIncomeModel> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.income_item_all, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {

        //get Data
        String month = incomeList.get(position).getMonth();
        String amount = incomeList.get(position).getAmount();

        //set Data
        holder.monthTv.setText(month);
        holder.amountTv.setText(amount);
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView monthTv;
        TextView amountTv;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            monthTv = itemView.findViewById(R.id.incomeItem_all_month);
            amountTv = itemView.findViewById(R.id.incomeItem_all_amount);

        }
    }

}
