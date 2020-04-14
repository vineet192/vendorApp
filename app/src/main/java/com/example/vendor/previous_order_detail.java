package com.example.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class previous_order_detail extends AppCompatActivity {


    Toolbar toolbar;

    LinearLayout orders_ly,removed_ly;
    TextView orders_tv,removed_tv,orderId,packing_status;
    ImageView move_back_iv;

    static String orderId_,order_Detail;
    String Packing_status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order_detail);

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        orderId_ = intent.getStringExtra("orderId");
        Packing_status = intent.getStringExtra("Packing_status");
        order_Detail = intent.getStringExtra("full_data");



        orders_ly= findViewById(R.id.orders_ly);
        removed_ly= findViewById(R.id.remove_ly);
        orders_tv= findViewById(R.id.orders_tv);
        removed_tv= findViewById(R.id.remove_tv);
        orderId= findViewById(R.id.orderId);
//        packing_status= findViewById(R.id.packing_status);
        move_back_iv= findViewById(R.id.move_back_iv);

//        packing_status.setText(Packing_status);

        orderId.setText(orderId_);


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new previous_order_detail_frag()).commit();

            removed_tv.setTextColor(Color.BLACK);
            orders_tv.setTextColor(Color.RED);
            orders_ly.setBackgroundResource(R.drawable.click_shape);
            removed_ly.setBackgroundResource(R.drawable.shape);
        }

        orders_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new previous_order_detail_frag()).commit();
                removed_tv.setTextColor(Color.BLACK);
                orders_tv.setTextColor(Color.RED);
                orders_ly.setBackgroundResource(R.drawable.click_shape);
                removed_ly.setBackgroundResource(R.drawable.shape);
            }
        });

        removed_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new previous_removed_item_frag()).commit();
                removed_tv.setTextColor(Color.RED);
                orders_tv.setTextColor(Color.BLACK);
                orders_ly.setBackgroundResource(R.drawable.shape);
                removed_ly.setBackgroundResource(R.drawable.click_shape);
            }
        });
    }
}
