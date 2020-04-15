package com.example.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Models.order_dataholder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class neworder_adapter extends RecyclerView.Adapter<neworder_adapter.new_order_ViewHolder> {

    ArrayList<order_dataholder> neworder_dataholderList;
    Context context;

    public SharedPreferences sharedPref;

    Gson gson = new Gson();
    SharedPreferences.Editor edit;

    String url_sent, response;

    public neworder_adapter(ArrayList<order_dataholder> order_dataholderList, Context context) {
        this.neworder_dataholderList = order_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public new_order_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order, parent, false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);

        return new new_order_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final new_order_ViewHolder holder, final int position) {

        final order_dataholder listItem = neworder_dataholderList.get(position);

        holder.time.setText(listItem.getTime());
        holder.date.setText(listItem.getDate());
        holder.orderID.setText(listItem.getOrderID());
//        holder.timer.setText(listItem.getTimer());
//        holder.total_price.setText(listItem.getTotal_price());

        int total_millis=180000;

        CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                holder.timer.setText( minutes + " M: " + seconds + " S  ");
            }

            @Override
            public void onFinish() {
                holder.timer.setText("Finish!");
            }
        };
        cdt.start();



        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Order_detail.class);
                intent.putExtra("OrderId",listItem.getOrderID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (neworder_dataholderList.size());
    }

    public class new_order_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID;
        TextView time;
        TextView date;
        TextView timer;
        TextView total_price;
        TextView reject_tv;
        Button accept_btn;
        LinearLayout view_detail_tv;

        public new_order_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            total_price = itemView.findViewById(R.id.total_price);
            timer=itemView.findViewById(R.id.timer);
//            reject_tv = itemView.findViewById(R.id.reject_tv);
//            accept_btn = itemView.findViewById(R.id.accept_btn);
            view_detail_tv = itemView.findViewById(R.id.view_detail_tv);
        }
    }
}
