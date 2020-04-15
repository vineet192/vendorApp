package com.example.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class new_subs_schedule_frag extends Fragment {


    RecyclerView new_subs_schedule_recycler;
    RecyclerView duration_recyler;
    RecyclerView.Adapter adapter = null;

    ArrayList<String> datelist = new ArrayList<>();
    ArrayList<String> timelist = new ArrayList<>();

    private List<neworders_model> list = new ArrayList<>();

    public JSONArray array, arr;
    public JSONObject obj, object;

    public static String json;

    public static String orderid_;

    public SharedPreferences sharedPref;
    SharedPreferences.Editor edit;

    TextView startDate, endDate, totalcost, deliverycharge, tax, reject_tv,month;
    Button accept_btn;

    String temp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_new_subs_schedule, container, false);
        sharedPref = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);


        duration_recyler = rootView.findViewById(R.id.duration_recyler);

        duration_recyler.setHasFixedSize(true);
        duration_recyler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));


        startDate = rootView.findViewById(R.id.startDate);
        endDate = rootView.findViewById(R.id.endDate);
        totalcost = rootView.findViewById(R.id.totalcost);
        reject_tv = rootView.findViewById(R.id.reject_tv);
        accept_btn = rootView.findViewById(R.id.accept_btn);
        month = rootView.findViewById(R.id.month);


        startDate.setText(new_subs_detail_frag.StartDate);
        endDate.setText(new_subs_detail_frag.EndDate);
        totalcost.setText(new_subs_detail_frag.TotalPrice);

        orderid_ = new_subs_detail.orderId_;

        temp = "new_orders" + orderid_;

        try {
            String jsonGet = sharedPref.getString("newsubnotify", null);
            JSONObject ob = new JSONObject(jsonGet);

            Log.d("second",jsonGet);

            String day="",time="";

            String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
            JSONArray array = ob.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (orderid_.equals(object.getString("order_id"))) ;
                {
                    deliverycharge.setText(("delivery_charge"));
                    tax.setText(("tax"));
                    month.setText(object.getString("duration"));
                    day=object.getString("days");
                    time=object.getString("delivery_time");

                }
            }
            int c=0;
            for(int i=0;i<day.length();i++)
            {
                if(day.charAt(i)=='1')
                {
                    datelist.add(days[i]);
                    timelist.add(time.substring(c,c+5));
                }
                c=c+5;
            }


            for (int i = 0; i < arr.length(); i++) {
                neworders_model List = new neworders_model(datelist.get(i), timelist.get(i),"null");
                list.add(List);
            }
            this.adapter = new new_sub_schedule_adapter(list, getActivity());
            duration_recyler.setAdapter(this.adapter);
            this.adapter = null;
            list = new ArrayList<>();


        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<JSONObject> orders = new ArrayList<JSONObject>();
                JSONObject orders_ = new JSONObject();


                try {
                    String jsonGet = sharedPref.getString("accepted_subs", null);
                    if (!(jsonGet == null)) {

                        JSONArray array = new JSONArray(jsonGet);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            orders_ = (JSONObject) array.get(i);
                            orders.add(orders_);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    String jsonGet = sharedPref.getString("new_subscription", null);
                    if (!(jsonGet == null)) {
                        JSONArray array = new JSONArray(jsonGet);

                        JSONObject arr = new JSONObject();

                        List<JSONObject> sharedList_ = new ArrayList<JSONObject>();


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            if (obj.getString("order_id").equals(orderid_)) {
                                JSONObject o1 = new JSONObject();
                                o1 = (JSONObject) array.get(i);
                                o1.put("Delivery_otp", "otp");
                                o1.put("Deliveryboy_phone", "phone");
                                o1.put("Deliveryboy_status", "arrived");
                                o1.put("vehicle_number", "12345");
                                o1.put("deliveryboy_name", "rajat");
                                o1.put("packing_status", "packed");


                                orders.add(o1);
                            } else
                                arr = (JSONObject) array.get(i);
                            sharedList_.add(arr);
                        }
                        Log.d("list", String.valueOf(sharedList_));
                        edit = sharedPref.edit();
                        edit.putString("new_subscription", String.valueOf(sharedList_));
                        edit.apply();
                        Log.d("on accept", sharedPref.getString("new_subscription", null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                edit = sharedPref.edit();
                edit.putString("accepted_subs", String.valueOf(orders));
                edit.commit();

                Log.d("accepted order", sharedPref.getString("accepted_subs", null));

                Intent i = new Intent(getContext(), MainActivity_.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        reject_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String jsonGet = sharedPref.getString("new_subscription", null);
                    if (!jsonGet.isEmpty()) {

                        JSONArray array = new JSONArray(jsonGet);
                        Log.d("neworder_", String.valueOf(array));

                        JSONObject arr = new JSONObject();

                        List<JSONObject> sharedList_ = new ArrayList<JSONObject>();


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            if (obj.getString("order_id").equals(orderid_)) {
                            } else
                                arr = (JSONObject) array.get(i);
                            sharedList_.add(arr);
                        }
                        Log.d("list", String.valueOf(sharedList_));
                        edit = sharedPref.edit();
                        edit.putString("new_subscription", String.valueOf(sharedList_));
                        edit.apply();
                        Log.d("on reject", sharedPref.getString("new_subscription", null));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(getContext(), MainActivity_.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });



        return rootView;

    }

    //        try {
//            String jsonGet = sharedPref.getString("temp", null);
//            JSONObject object = new JSONObject(jsonGet);
//            array = object.getJSONArray("items");
//            JSONObject ob1 = array.getJSONObject(0);
//            startDate.setText(ob1.getString("orderdate"));
//
//
//
////            String startDate=ob1.getString("startdate");
//            String startDate="2020-04-05";
//
//
//            int year= Integer.parseInt(startDate.substring(0,4));
//            int month= Integer.parseInt(startDate.substring(5,7));
//            int date= Integer.parseInt(startDate.substring(7));
//            date=date+3;
//            int duration= Integer.parseInt(ob1.getString("duration"));
//            //from previous class
//            endDate.setText(("enddate"));
//            totalcost.setText(("total_price"));
//
//            String str=ob1.getString("delivery_dates");
//            for(int j=0;j<str.length();j++)
//            {
//                char ch=str.charAt(j);
//                if(j==0&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//                    }
//                }
//
//                if(j==1&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//                    }
//                }
//
//                if(j==2&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//
//                    }
//                }
//
//                if(j==3&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//                    }
//                }
//
//                if(j==4&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//                    }
//                }
//
//                if(j==5&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//                    }
//                }
//
//                if(j==6&&ch=='1')
//                {
//                    for(int k=0;k<=duration;k++)
//                    {
//                        Calendar date1 = Calendar.getInstance();
//                        date1.set(year, month, date);
//
//                        while (date1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
//
//                            date1.add(Calendar.DATE, 1);
//                        }
//
//                        String s= String.valueOf(date1.getTime());
//                        String str1="";
//                        str1=str1+s.substring(4,7)+" "+s.substring(8,10)+" "+s.substring(24);
//                        datelist.add(str1);
//
//                    }
//                }
//
//            }
//            for (int k = 0; k < array.length(); k++) {
//                subscription_dataholder List = new subscription_dataholder(datelist.get(k), timelist.get(k));
//                list.add(List);
//            }
//
//            adapter = new new_sub_detail_adapter(list, getActivity());
//            new_subs_schedule_recycler.setAdapter(adapter);
//            adapter = null;
//            list = new ArrayList<>();
//
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }

}

