package com.example.vendor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class new_sub_schedule_adapter extends RecyclerView.Adapter<new_sub_schedule_adapter.sub_schedule_ViewHolder> {


    List<neworders_model> sub_Orderdetail_dataholderList;
    Context context;

    JSONObject obj, object;
    Gson gson = new Gson();

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;
    String orderid_;


    public new_sub_schedule_adapter(List<neworders_model> sub_Orderdetail_dataholderList, Context context) {
        this.sub_Orderdetail_dataholderList = sub_Orderdetail_dataholderList;
        this.context = context;
    }


    @NonNull
    @Override
    public new_sub_schedule_adapter.sub_schedule_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_sub_schedule_layout, parent, false);
        orderid_ = new_subs_detail.orderId_;


        return new new_sub_schedule_adapter.sub_schedule_ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final new_sub_schedule_adapter.sub_schedule_ViewHolder holder, int position) {

        final neworders_model listItem = sub_Orderdetail_dataholderList.get(position);

        holder.day.setText(listItem.getDay());
        holder.day.setText(listItem.getTime());

    }

    @Override
    public int getItemCount() {
        return sub_Orderdetail_dataholderList.size();
    }

    public class sub_schedule_ViewHolder extends RecyclerView.ViewHolder {

        TextView day, time;

        public sub_schedule_ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            day = itemView.findViewById(R.id.time);


        }
    }
}
