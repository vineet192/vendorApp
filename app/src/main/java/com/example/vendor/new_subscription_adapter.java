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

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class new_subscription_adapter extends RecyclerView.Adapter<new_subscription_adapter.new_subscription_ViewHolder> {

    List<subscription_dataholder> newsubs_dataholderList;
    Context context;

    public SharedPreferences sharedPref;

    Gson gson = new Gson();
    SharedPreferences.Editor edit;

    public static String subs_type;

    public new_subscription_adapter(List<subscription_dataholder> newsubs_dataholderList, Context context) {
        this.newsubs_dataholderList = newsubs_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public new_subscription_adapter.new_subscription_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_subscription,parent,false);
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);

        return new new_subscription_adapter.new_subscription_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final new_subscription_adapter.new_subscription_ViewHolder holder, final int position) {

        final subscription_dataholder listItem= newsubs_dataholderList.get(position);


        holder.start_date.setText(listItem.getStartdate());
        holder.end_date.setText(listItem.getEnddate());
        holder.orderID.setText(listItem.getOrderID());
        holder.total_price.setText(listItem.getTotal_price());

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

//                holder.timer.setText( minutes + " M: " + seconds + " S  ");
            }

            @Override
            public void onFinish() {
//                holder.timer.setText("Finish!");
            }
        };
        cdt.start();

        subs_type=listItem.getSubscription_type();


        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(context.getApplicationContext(), new_subs_detail.class);
                intent.putExtra("OrderId",holder.orderID.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (newsubs_dataholderList.size());
    }

    public class new_subscription_ViewHolder extends RecyclerView.ViewHolder {

        TextView orderID;
        TextView end_date;
        TextView start_date;
        TextView timer;
        TextView total_price;
        LinearLayout view_detail_tv;


        public new_subscription_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            start_date=itemView.findViewById(R.id.start_date);
            end_date=itemView.findViewById(R.id.end_date);
            total_price=itemView.findViewById(R.id.total_price);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);
        }
    }
}