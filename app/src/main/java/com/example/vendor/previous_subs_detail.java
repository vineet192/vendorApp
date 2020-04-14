package com.example.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class previous_subs_detail extends AppCompatActivity {

        public static String orderId_,order_Detail;
        Toolbar toolbar;

        LinearLayout schedule_ly,subscription_ly,remove_ly;
        TextView schedule_tv,subscription_tv,orderid,remove_tv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_previous_subs_detail);

            toolbar= findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            orderid= findViewById(R.id.orderid);



            Intent intent=getIntent();
            orderId_=intent.getStringExtra("orderId");
            order_Detail=intent.getStringExtra("full_json_data");


            orderid.setText(orderId_);

            schedule_ly= findViewById(R.id.schedule_ly);
            subscription_ly= findViewById(R.id.subscription_ly);
            schedule_tv= findViewById(R.id.schedule_tv);
            subscription_tv= findViewById(R.id.subscription_tv);
            orderid= findViewById(R.id.orderid);
            remove_ly= findViewById(R.id.remove_ly);
            remove_tv= findViewById(R.id.remove_tv);


            subscription_tv.setTextColor(Color.RED);
            schedule_tv.setTextColor(Color.BLACK);
            remove_tv.setTextColor(Color.BLACK);

            subscription_ly.setBackgroundResource(R.drawable.click_shape);
            schedule_ly.setBackgroundResource(R.drawable.shape);
            remove_ly.setBackgroundResource(R.drawable.shape);




            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new previous_subs_detail_frag()).commit();
            }
//
//            schedule_ly.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.frag, new previous_subs_schedule_frag()).commit();
//                    subscription_tv.setTextColor(Color.BLACK);
//                    schedule_tv.setTextColor(Color.RED);
//                    schedule_ly.setBackgroundResource(R.drawable.click_shape);
//                    subscription_ly.setBackgroundResource(R.drawable.shape);
//                    remove_tv.setTextColor(Color.BLACK);
//                    remove_ly.setBackgroundResource(R.drawable.shape);
//                }
//            });

            subscription_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag, new previous_subs_detail_frag()).commit();
                    subscription_tv.setTextColor(Color.RED);
                    schedule_tv.setTextColor(Color.BLACK);
                    schedule_ly.setBackgroundResource(R.drawable.shape);
                    subscription_ly.setBackgroundResource(R.drawable.click_shape);
                    remove_tv.setTextColor(Color.BLACK);
                    remove_ly.setBackgroundResource(R.drawable.shape);
                }
            });

            remove_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag, new previous_sub_removeditems_frag()).commit();
                    remove_tv.setTextColor(Color.RED);
                    remove_ly.setBackgroundResource(R.drawable.click_shape);
                    subscription_tv.setTextColor(Color.BLACK);
                    schedule_tv.setTextColor(Color.BLACK);
                    schedule_ly.setBackgroundResource(R.drawable.shape);
                    subscription_ly.setBackgroundResource(R.drawable.shape);
                }
            });

        }
}
