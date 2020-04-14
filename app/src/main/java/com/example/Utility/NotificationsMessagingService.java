package com.example.Utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.vendor.MainActivity_;
import com.example.vendor.R;
import com.example.Models.order_dataholder;
import com.example.vendor.subscription_dataholder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class NotificationsMessagingService extends FirebaseMessagingService {
    String TAG = "NotificationCheck";
    private static final String CHANNEL_ID = "notification_liveTask";
    private static final String CHANNEL_NAME = "CURRENT OFFERS";
    private static final String CHANNEL_DESCRIPTION = "Notifications for new offer";
    ArrayList<order_dataholder> listOrders;
    ArrayList<subscription_dataholder> listSubscription;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title;
        String message;
        Map<String,String> data = remoteMessage.getData();
        Log.d("notify","remoteMessage.getData() : "+remoteMessage.getData());


        Toast.makeText(this,"notify",Toast.LENGTH_LONG).show();
        JSONObject jsnobject = new JSONObject(data);
        JSONObject new_order = null;
        JSONObject new_subscription = null;
        JSONObject delivery_boy_details = null;

        try {
            new_order = new JSONObject(jsnobject.getString("new_order"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            new_subscription = new JSONObject(jsnobject.getString("new_subscription"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            delivery_boy_details = new JSONObject(jsnobject.getString("delivery_boy_details" ));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG,"newOrder = "+String.valueOf(new_order)+ " newSubs = "+String.valueOf(new_subscription)+ " delivery = "+String.valueOf(delivery_boy_details));

        if(!String.valueOf(new_order).equals("null")){
            try {
                Log.d(TAG + "  newOrder","order_id : "+new_order.get("order_id")+ " * date : "
                        +new_order.get("date")+" * time : "+new_order.get("time")+" * total_orders : "+new_order.get("total_order")
                +" * quantity : "+new_order.get("quantity"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            title = "New Order";
            message = "There is a new Order  for you";
            Log.d(TAG,"Title : "+title+" * Message : "+message);
            sendNotification(title,message);

            order_dataholder order_dataholder = new order_dataholder();
            try {
                order_dataholder.setOrderID("" + new_order.get("order_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                order_dataholder.setDate("" + new_order.get("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                order_dataholder.setTime("" + new_order.get("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                order_dataholder.setQuantityArray(new JSONArray(jsnobject.getString("quantity")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                order_dataholder.setTotalOrderArray( new JSONArray(jsnobject.getString("total_order")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            loadNewOrderData();
            listOrders.add(order_dataholder);
            saveNewOrderData(listOrders);
            Intent intent=new Intent(this,MainActivity_.class);
            intent.putExtra("check","trueorder");
            startActivity(intent);
        }


        if(!String.valueOf(new_subscription).equals("null")){
            try {
                Log.d(TAG + "  newSubscription","orderId : "+new_subscription.get("orderId")+ " * date : "+" * subscriptionType : "+new_subscription.get("subscriptionType")
                        +new_subscription.get("date")+" * time : "+new_subscription.get("time")+" * price : "+new_subscription.get("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            title = "New Subscription";
            message = "There is a new Subscription  for you";
            Log.d(TAG,"Title : "+title+" * Message : "+message);
            sendNotification(title,message);

            subscription_dataholder subscription_dataholder = new subscription_dataholder();
            try {
                subscription_dataholder.setOrderID("" + new_subscription.get("orderId"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                subscription_dataholder.setTimer("" + new_subscription.get("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                subscription_dataholder.setTotal_price("" + new_subscription.get("price"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                subscription_dataholder.setSubscription_type("" + new_subscription.get("subscriptionType"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            saveNewSubscriptionData(subscription_dataholder);
        }

        if(!String.valueOf(delivery_boy_details).equals("null")){
            try {
                Log.d(TAG + "  deliveryBoyDetails","name : "+delivery_boy_details.get("name")+ " * phoneNo : "
                        +delivery_boy_details.get("phoneNo")+" * url : "+delivery_boy_details.get("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            title = "Delivery boy has been selected";
            message = "Ready the order";
            Log.d(TAG,"Title : "+title+" * Message : "+message);
            sendNotification(title,message);

            String name = null;
            String phoneNo = null;
            String url = null;
            try {
                name = ""+delivery_boy_details.get("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                phoneNo = ""+delivery_boy_details.get("phoneNo");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                url = ""+delivery_boy_details.get("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            saveDeliveryBoyDetails(name,phoneNo,url);

        }

    }

    public void sendNotification(String messageTitle,String messageBody){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity_.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(contentIntent);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(this);

        // Since android Oreo notification channel is needed.

        int unique_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManagerCompat.notify(unique_id, notificationBuilder.build());
        Log.d(TAG,"sendNotification = run");
    }

    public void saveNewOrderData(ArrayList<order_dataholder> list) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for newOrder", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("list", json);
        editor.apply();
        Log.d(TAG,""+list.toString());
    }

    public void saveNewSubscriptionData(subscription_dataholder order) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for newSubscription", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(order);
        editor.putString("order", json);
        editor.apply();
        Log.d(TAG,""+order.toString());
    }

    public void saveDeliveryBoyDetails(String name , String phoneNo, String url) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for newOrder", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phoneNo", phoneNo);
        editor.putString("url", url);
        editor.apply();
        Log.d(TAG,name +"*"+phoneNo+"*"+url);
    }

    public void  loadNewOrderData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences for newOrder", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<order_dataholder>>() {}.getType();
        listOrders = gson.fromJson(json, type);

        if (listOrders == null) {
            listOrders = new ArrayList<>();
        }
    }
}