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



//        String jsonGet = sharedPref.getString("accepted_order", null);
//
//        if(jsonGet!=null)
//        {
//            Type type= new TypeToken<ArrayList<HashMap>>() {}.getType();
//            sharedList=gson.fromJson(jsonGet,type);
//        }
//
//        String jsonShow = gson.toJson(sharedList);
//
//        Log.d("JSON",jsonShow);

        holder.start_date.setText(listItem.getStartdate());
        holder.end_date.setText(listItem.getEnddate());
        holder.orderID.setText(listItem.getOrderID());
        holder.typeof_subscription.setText(listItem.getSubscription_type());
        holder.total_price.setText(listItem.getTotal_price());

        subs_type=listItem.getSubscription_type();

//        if (holder.timer != null) {
//            holder.timer.cancel();
//        }
//        holder.timer = new CountDownTimer(7000, 1000) {
//            @Override
//            public void onTick(long l) {
//            }
//
//            @Override
//            public void onFinish() {
//
//                neworder_dataholderList.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
//                notifyItemRangeChanged(holder.getAdapterPosition(), neworder_dataholderList.size());
//
//                String json=new_order_frag.json;
//                try {
//                    JSONObject object = new JSONObject(json);
//
//                    JSONArray array = object.getJSONArray("orders");
//
//                    array.remove(holder.getAdapterPosition());
//
//
//
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();

//        holder.reject_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Cancel order")
//                        .setMessage("Are you sure you want to cancel this order?")
//
//                        // Specifying a listener allows you to take an action before dismissing the dialog.
//                        // The dialog is automatically dismissed when a dialog button is clicked.
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                newsubs_dataholderList.remove(holder.getAdapterPosition());
//                                notifyItemRemoved(holder.getAdapterPosition());
//                                notifyItemRangeChanged(holder.getAdapterPosition(), newsubs_dataholderList.size());
//
////                                holder.timer.cancel();
//
//                                try{
//                                    String jsonGet = sharedPref.getString("new_subscription", null);
//                                    if(!jsonGet.isEmpty())
//                                    {
//
//                                        JSONArray array = new JSONArray(jsonGet);
//                                        Log.d("neworder_", String.valueOf(array));
//
//                                        JSONObject arr=new JSONObject();
//
//                                        List<JSONObject> sharedList_=new ArrayList<JSONObject>();
//
//
//
//                                        for(int i=0;i<array.length();i++)
//                                        {
//                                            JSONObject obj=array.getJSONObject(i);
//                                            if(obj.getString("order_id").equals(holder.orderID.getText()))
//                                            {
//                                            }
//                                            else
//                                                arr= (JSONObject) array.get(i);
//                                            sharedList_.add(arr);
//                                        }
//                                        Log.d("list", String.valueOf(sharedList_));
//                                        edit = sharedPref.edit();
//                                        edit.putString("new_subscription", String.valueOf(sharedList_));
//                                        edit.apply();
//                                        Log.d("on reject",sharedPref.getString("new_subscription", null));
//                                    }
//                                }catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        })
//
//                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });
//
//        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                HashMap<String, String> testHashMap = new HashMap<>();
////                testHashMap.put("orderID", listItem.getOrderID());
////                testHashMap.put("time", listItem.getTime());
////                testHashMap.put("date", listItem.getDate());
////                testHashMap.put("total_price", listItem.getTotal_price());
//                ArrayList<JSONObject> orders = new ArrayList<JSONObject>();
//                JSONObject orders_=new JSONObject();
//
//                Log.d("check123",sharedPref.getString("new_subscription", null));
//
//
//
//                try{
//                    String jsonGet = sharedPref.getString("accepted_subs", null);
//                    if(!(jsonGet==null))
//                    {
//
//                        JSONArray array = new JSONArray(jsonGet);
//                        for(int i=0;i<array.length();i++)
//                        {
//                            JSONObject obj=array.getJSONObject(i);
//                            orders_= (JSONObject) array.get(i);
//                            orders.add(orders_);
//                        }
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                try{
//                    String jsonGet = sharedPref.getString("new_subscription", null);
//                    if(!(jsonGet==null))
//                    {
//                        JSONObject object=new JSONObject(jsonGet);
//                        JSONObject arr=new JSONObject();
//                        JSONArray array = object.getJSONArray("sorders");
//
//
//                        List<JSONObject> sharedList_=new ArrayList<JSONObject>();
//
//
//                        for(int i=0;i<array.length();i++)
//                        {
//                            JSONObject obj=array.getJSONObject(i);
//                            if(obj.getString("order_id").equals(holder.orderID.getText()))
//                            {
//                                JSONObject o1=new JSONObject();
//                                o1=(JSONObject)array.get(i);
//                                o1.put("Delivery_otp","otp");
//                                o1.put("Deliveryboy_phone","phone");
//                                o1.put("Deliveryboy_status","arrived");
//                                o1.put("vehicle_number","12345");
//                                o1.put("deliveryboy_name","rajat");
//                                o1.put("packing_status","packed");
//                                o1.put("subs_type",listItem.getSubscription_type());
//
//
//
//                                orders.add(o1);
//                            }
//                            else
//                                arr= (JSONObject) array.get(i);
//                            sharedList_.add(arr);
//                        }
//                        Log.d("list", String.valueOf(sharedList_));
//                        edit = sharedPref.edit();
//                        edit.putString("new_subscription", String.valueOf(sharedList_));
//                        edit.apply();
//                        Log.d("on accept",sharedPref.getString("new_subscription", null));
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                Log.d("check", String.valueOf(orders));
//
//                edit = sharedPref.edit();
//                edit.putString("accepted_subs", String.valueOf(orders));
//                edit.commit();
//
//                Log.d("accepted order",sharedPref.getString("accepted_subs", null));
//
//
//                newsubs_dataholderList.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
//                notifyItemRangeChanged(holder.getAdapterPosition(), newsubs_dataholderList.size());
//                Log.d("acceptedorder",sharedPref.getString("accepted_subs", null));
//
//
//            }
//        });

        holder.view_detail_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(context.getApplicationContext(), new_subs_detail.class);
                intent.putExtra("Order", new_subscription_frag.JsonData);
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
        CountDownTimer timer;
        TextView total_price;
        TextView reject_tv;
        Button accept_btn;
        LinearLayout view_detail_tv;

        TextView typeof_subscription;

        public new_subscription_ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID=itemView.findViewById(R.id.orderID);
            start_date=itemView.findViewById(R.id.start_date);
            end_date=itemView.findViewById(R.id.end_date);
            total_price=itemView.findViewById(R.id.total_price);
            typeof_subscription=itemView.findViewById(R.id.typeof_subscription);
//            reject_tv=itemView.findViewById(R.id.reject_tv);
//            accept_btn=itemView.findViewById(R.id.accept_btn);
            view_detail_tv=itemView.findViewById(R.id.view_detail_tv);
        }
    }
}