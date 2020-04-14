package com.example.vendor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class currrent_sub_schedule_adapter extends RecyclerView.Adapter<currrent_sub_schedule_adapter.currentsubschedule_ViewHolder> {


    List<schedule_dataholder> currentsubschedule_dataholderList;
    Activity context;


    public currrent_sub_schedule_adapter(List<schedule_dataholder> currentsubschedule_dataholderList, Activity context) {
        this.currentsubschedule_dataholderList = currentsubschedule_dataholderList;
        this.context = context;
    }

    @NonNull
    @Override
    public currrent_sub_schedule_adapter.currentsubschedule_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sub_schedule_layout,parent,false);

        return new currrent_sub_schedule_adapter.currentsubschedule_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull currrent_sub_schedule_adapter.currentsubschedule_ViewHolder holder, int position) {

        schedule_dataholder ListItem= currentsubschedule_dataholderList.get(position);

        holder.date.setText(ListItem.getDate());
        holder.time.setText(ListItem.getTime());

    }

    @Override
    public int getItemCount() {
        return (currentsubschedule_dataholderList.size());
    }



    public class currentsubschedule_ViewHolder extends RecyclerView.ViewHolder {

        TextView date,time;

        public currentsubschedule_ViewHolder(@NonNull View itemView) {
            super(itemView);


            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
        }
    }
}
