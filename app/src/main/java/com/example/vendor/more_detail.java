package com.example.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class more_detail extends AppCompatActivity {

    private TextView name,id,city,address,phone;
    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);
        setTitle("Profile");
        sharedPreferences = this.getSharedPreferences(
                getString(R.string.shared_preference_key), Context.MODE_PRIVATE);

        name = (TextView)findViewById(R.id.ven_name);
        id = (TextView)findViewById(R.id.ven_id);
        phone = (TextView)findViewById(R.id.ven_phone);
        city = (TextView)findViewById(R.id.ven_city);
        address = (TextView)findViewById(R.id.ven_address);
        profileImage = (ImageView)findViewById(R.id.ven_image);

        name.setText("Name : " + sharedPreferences.getString(getString(R.string.vendor_name_key),"defaultName"));
        id.setText("ID : " + sharedPreferences.getString(getString(R.string.vendor_id_key),"defaultId"));
        phone.setText("Phone number : " + sharedPreferences.getString(getString(R.string.vendor_phone_key),"defaultPhone"));
        city.setText("City : " + sharedPreferences.getString(getString(R.string.vendor_city_key),"defaultCity"));
        address.setText("Address : " + sharedPreferences.getString(getString(R.string.vendor_address_key),"defaultAddress"));

        Picasso.get()
                .load("https://gocoding.azurewebsites.net" +
                        sharedPreferences.getString(getString(R.string.vendor_image_key),"null"))
                .placeholder(R.drawable.default_fruit)
                .into(profileImage);

    }
}
