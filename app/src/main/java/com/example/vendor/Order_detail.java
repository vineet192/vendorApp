package com.example.vendor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Order_detail extends AppCompatActivity {

    public static String orderId_;
    Toolbar toolbar;

    LinearLayout remove_ly,order_ly;
    TextView remove_tv,order_tv,orderid;

    static String tempdata_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        orderid= findViewById(R.id.orderid);



        Intent intent=getIntent();
        orderId_=intent.getStringExtra("OrderId");


        orderid.setText(orderId_);

        remove_ly= findViewById(R.id.remove_ly);
        order_ly= findViewById(R.id.order_ly);
        remove_tv= findViewById(R.id.remove_tv);
        order_tv= findViewById(R.id.order_tv);
        orderid= findViewById(R.id.orderid);

        order_tv.setTextColor(Color.RED);
        remove_tv.setTextColor(Color.BLACK);
        remove_ly.setBackgroundResource(R.drawable.shape);
        order_ly.setBackgroundResource(R.drawable.click_shape);



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new order_detail_frag()).commit();
        }

        remove_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new remove_item_frag()).commit();
                order_tv.setTextColor(Color.BLACK);
                remove_tv.setTextColor(Color.RED);
                remove_ly.setBackgroundResource(R.drawable.click_shape);
                order_ly.setBackgroundResource(R.drawable.shape);
            }
        });

        order_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag, new order_detail_frag()).commit();
                order_tv.setTextColor(Color.RED);
                remove_tv.setTextColor(Color.BLACK);
                remove_ly.setBackgroundResource(R.drawable.shape);
                order_ly.setBackgroundResource(R.drawable.click_shape);
            }
        });

    }
}
