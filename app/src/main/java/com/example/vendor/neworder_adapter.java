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
//                                neworder_dataholderList.remove(holder.getAdapterPosition());
//                                notifyItemRemoved(holder.getAdapterPosition());
//                                notifyItemRangeChanged(holder.getAdapterPosition(), neworder_dataholderList.size());
//
//                                JSONArray a = new JSONArray();
//                                HashMap<String, String> op = new HashMap<>();
//                                op.put("order_Id", String.valueOf(holder.orderID.getText()));
//                                op.put("status", "not accepted");
//                                op.put("items", String.valueOf(a));
//                                String outputreq = gson.toJson(op);
//
//                                try {
//                                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_sent).openConnection()));
//                                    httpcon.setDoOutput(true);
//                                    httpcon.setRequestProperty("Content-Type", "application/json");
//                                    httpcon.setRequestProperty("Accept", "application/json");
//                                    httpcon.setRequestMethod("POST");
//                                    httpcon.connect();
//
//                                    OutputStream os = httpcon.getOutputStream();
//                                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                                    writer.write(outputreq);
//                                    writer.close();
//                                    os.close();
//
//                                    BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
//
//                                    String line = null;
//                                    StringBuilder sb = new StringBuilder();
//
//                                    while ((line = br.readLine()) != null) {
//                                        sb.append(line);
//                                    }
//
//                                    br.close();
//                                    Log.d("comingdata", sb.toString());
//                                    response = sb.toString();
//
//                                } catch (MalformedURLException e) {
//                                    e.printStackTrace();
//                                    Log.d("MalformedURLException", e.getMessage());
//                                } catch (ProtocolException e) {
//                                    e.printStackTrace();
//                                    Log.d("ProtocolException", e.getMessage());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    Log.d("IOException", e.getMessage());
//
//                                }
//
////                                holder.timer.cancel();
//
////                                try{
////                                    String jsonGet = sharedPref.getString("new_order", null);
////                                    if(!jsonGet.isEmpty())
////                                    {
////
////                                        JSONArray array = new JSONArray(jsonGet);
////                                        Log.d("neworder_", String.valueOf(array));
////
////                                        JSONObject arr=new JSONObject();
////
////                                        List<JSONObject> sharedList_=new ArrayList<JSONObject>();
////
////
////
////                                        for(int i=0;i<array.length();i++)
////                                        {
////                                            JSONObject obj=array.getJSONObject(i);
////                                            if(obj.getString("order_id").equals(holder.orderID.getText()))
////                                            {
////                                            }
////                                            else
////                                                arr= (JSONObject) array.get(i);
////                                            sharedList_.add(arr);
////                                        }
////                                        Log.d("list", String.valueOf(sharedList_));
////                                        edit = sharedPref.edit();
////                                        edit.putString("new_order", String.valueOf(sharedList_));
////                                        edit.apply();
////                                        Log.d("on reject",sharedPref.getString("new_order", null));
////                                    }
////                                }catch (JSONException e) {
////                                    e.printStackTrace();
////                                }
//                            }
//                        })
//
//                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });

//        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ProgressDialog pdialog;
//                pdialog = new ProgressDialog(context); // this = YourActivity
//                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                pdialog.setTitle("Loading");
//                pdialog.setMessage("Loading. Please wait...");
//                pdialog.setIndeterminate(true);
//                pdialog.setCanceledOnTouchOutside(false);
//                pdialog.show();
//
//                HashMap<String, String> op = new HashMap<>();
//                op.put("order_Id", String.valueOf(holder.orderID.getText()));
//                op.put("status", "accepted");
//
//                try {
//                    JSONObject o1 = new JSONObject(order_detail_frag.order_Detail);
//                    JSONArray a1 = o1.getJSONArray("items");
//                    op.put("items", String.valueOf(a1));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String outputreq = gson.toJson(op);
//
//                try {
//                    HttpURLConnection httpcon = (HttpURLConnection) ((new URL(url_sent).openConnection()));
//                    httpcon.setDoOutput(true);
//                    httpcon.setRequestProperty("Content-Type", "application/json");
//                    httpcon.setRequestProperty("Accept", "application/json");
//                    httpcon.setRequestMethod("POST");
//                    httpcon.connect();
//
//                    OutputStream os = httpcon.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                    writer.write(outputreq);
//                    writer.close();
//                    os.close();
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));
//
//                    String line = null;
//                    StringBuilder sb = new StringBuilder();
//
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line);
//                    }
//
//                    br.close();
//                    Log.d("comingdata", sb.toString());
//                    response = sb.toString();
//
//                } catch (MalformedURLException e) {
//                    pdialog.dismiss();
//                    e.printStackTrace();
//                    Log.d("MalformedURLException", e.getMessage());
//                } catch (ProtocolException e) {
//                    pdialog.dismiss();
//                    e.printStackTrace();
//                    Log.d("ProtocolException", e.getMessage());
//                } catch (IOException e) {
//                    pdialog.dismiss();
//                    e.printStackTrace();
//                    Log.d("IOException", e.getMessage());
//
//                }
//                try {
//                    JSONObject o9 = new JSONObject(response);
//                    String check = o9.getString("");
//                    if (check.equals("")) {
//                        neworder_dataholderList.remove(holder.getAdapterPosition());
//                        notifyItemRemoved(holder.getAdapterPosition());
//                        notifyItemRangeChanged(holder.getAdapterPosition(), neworder_dataholderList.size());
//                    } else {
//                        Toast.makeText(context, "not accepted", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    Toast.makeText(context, "not accepted", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//
//            }
//        });

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
