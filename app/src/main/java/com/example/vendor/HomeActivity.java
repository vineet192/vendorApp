package com.example.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Fragments.AllItemsFragment;
import com.example.Fragments.Deleivery_Boy_Details;
import com.example.Fragments.DeliveryBoyTrackingfragment;
import com.example.Fragments.HistoryFragment;
import com.example.Fragments.ItemFragment;
import com.example.Fragments.NewItemFrgament;
import com.example.Fragments.OngoingFragment;
import com.example.Fragments.OngoingOrdersFragment;
import com.example.Fragments.OrderHistoryFragment;
import com.example.Fragments.PendingFragment;
import com.example.Models.CompleteOrderListResponse;
import com.example.Models.DeleiveryBoy;
import com.example.Models.ItemSavingResponse;
import com.example.Models.NewOrders;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.ProductDescriptionResponse;
import com.example.Models.StatusResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String vendorPhone;
    private String vendorIdentity;
    private String vendorName;
    private ApiInterface mAPIService;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private Menu menu;
    private TextView name;
    private TextView phone;
    private TextView identity;
    private final String PREFERENCE_FILE_KEY = "myAppPreference";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;
    private String status;
    private String descriptionkeystatus = "Status";
    private String defaultStatus = "inactive";
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView =findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(HomeActivity.this);

        vendorName = getIntent().getStringExtra("vendorName");
        vendorPhone = getIntent().getStringExtra("vendorPhone");
        vendorIdentity = getIntent().getStringExtra("vendorIdentity");
        fragmentManager=getSupportFragmentManager();

        sharedPref = HomeActivity.this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
//        Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
        dl = (DrawerLayout)findViewById(R.id.dl);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);

        menu = nv.getMenu();
        final MenuItem statusitem = menu.findItem(R.id.activeInactive);
        String oldstatus = sharedPref.getString(descriptionkeystatus, defaultStatus);
//        Toast.makeText(this, oldstatus, Toast.LENGTH_SHORT).show();
        ((ProductsClient)HomeActivity.this.getApplicationContext()).setStatus(oldstatus);
        statusitem.setTitle("Status : "+oldstatus);
        if(oldstatus.equals("inactive"))
            statusitem.setChecked(false);
        else
            statusitem.setChecked(true);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.activeInactive:
                        Toast.makeText(HomeActivity.this, "Request sent. Waiting for response...", Toast.LENGTH_SHORT).show();
                        status = ((ProductsClient)HomeActivity.this.getApplicationContext()).getStatus();
                        if(status.equals("active"))
                            status = "inactive";
                        else
                            status = "active";

                        Call<StatusResponse> calldash = mAPIService.setStatus(vendorPhone, status);
                        calldash.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, final Response<StatusResponse> response) {
                                if (response.isSuccessful()) {
                                    if(response.body().getSuccess().equals("true")){
                                        Toast.makeText(HomeActivity.this, "Status Saved", Toast.LENGTH_SHORT).show();
                                        if(response.body().getStatus().equals("inactive")){
                                            ((ProductsClient)HomeActivity.this.getApplicationContext()).setStatus("inactive");
//                                            Toast.makeText(HomeActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                            editor.putString(descriptionkeystatus, "inactive");
                                            statusitem.setChecked(false);
                                        }
                                        else {
                                            ((ProductsClient)HomeActivity.this.getApplicationContext()).setStatus("active");
//                                            Toast.makeText(HomeActivity.this,response.body().getStatus() , Toast.LENGTH_SHORT).show();
                                            editor.putString(descriptionkeystatus, "active");
                                            statusitem.setChecked(true);
                                        }
                                        editor.commit();
                                        statusitem.setTitle("Status : "+((ProductsClient)HomeActivity.this.getApplicationContext()).getStatus());
                                    }
                                else {
                                        Toast.makeText(HomeActivity.this, "Unable to save status", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(HomeActivity.this, "Response from server is not successful", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                Toast.makeText(HomeActivity.this, "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case R.id.logout:
                        editor.clear();
                        editor.apply();
                        ((ProductsClient)HomeActivity.this.getApplicationContext()).clearAllLists();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        return true;
                }

                return true;

            }
        });

        View headerLayout = nv.getHeaderView(0);
        name = headerLayout.findViewById(R.id.vendorname);
        phone = headerLayout.findViewById(R.id.vendorphone);
        identity = headerLayout.findViewById(R.id.vendorIdentity);
        name.setText("Name : " + vendorName);
        phone.setText("Phone : " + vendorPhone);
        identity.setText("Id : " + vendorIdentity);

        mAPIService = ApiUtils.getAPIService();
        fillMyLists();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch(menuItem.getItemId())
//        {
//            case R.id.itemmenu:
//                loadfragment(new AllItemsFragment());
//                break;
////            case R.id.ongoingmenu:
////                loadfragment(new OngoingOrdersFragment());
////                break;
//            case R.id.pendingmenu:
//                loadfragment(new OngoingOrdersFragment());
//                break;
//            case R.id.historymenu:
//                loadfragment(new OrderHistoryFragment());
//                break;
//        }
//        return false;
//    }

    public void loadOrderDescription(OrderDescriptionResponse orders){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = PendingFragment.newInstance(orders);
        ft.replace(R.id.fragmentcontainer, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }

    public void loadDeleiveryBoyDetails(DeleiveryBoy details){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = Deleivery_Boy_Details.newInstance(details);
        ft.replace(R.id.fragmentcontainer, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }

    public void loadHistoryfragment(OrderDescriptionResponse orders){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = HistoryFragment.newInstance(orders);
        ft.replace(R.id.fragmentcontainer, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }

    public void loadOngoingfragment(OrderDescriptionResponse orders){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = PendingFragment.newInstance(orders);
        ft.replace(R.id.fragmentcontainer, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }



    public void loaddeliveryboytrackingfragment(String name,String phoneno)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DeliveryBoyTrackingfragment fragment = new DeliveryBoyTrackingfragment();
        Bundle bundle = new Bundle();
        bundle.putString("deliveryboyname",name);
        bundle.putString("deliveryboyphoneno",phoneno);
        fragment.setArguments(bundle);
        ft.replace(R.id.fragmentcontainer, fragment);
        ft.commit();
        ft.addToBackStack(null);
    }


    public  boolean loadfragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentcontainer,fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public void onPrepareOptionsMenu(Menu menu, int id, String text) {
        menu.findItem(id).setTitle(text);
    }


    private void fillMyLists(){
        if(((ProductsClient) HomeActivity.this.getApplicationContext()).getCompleteList().isEmpty()) {
            Call<CompleteOrderListResponse> calldash = mAPIService.getallProducts();
            calldash.enqueue(new Callback<CompleteOrderListResponse>() {
                @Override
                public void onResponse(Call<CompleteOrderListResponse> call, final Response<CompleteOrderListResponse> response) {
                    if (response.isSuccessful()) {
//                        Toast.makeText(HomeActivity.this, Integer.toString(response.body().getNo_prod()), Toast.LENGTH_LONG).show();
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).setCompleteList(response.body().getProducts());
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).completeAllList();
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).setCheck(1);
                        fillMyItemList();
                    } else {
                        Toast.makeText(HomeActivity.this, "Response from server is not successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CompleteOrderListResponse> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            fillMyItemList();
        }
    }


    private void fillMyItemList(){
        if(((ProductsClient) HomeActivity.this.getApplicationContext()).getServingList().isEmpty()) {
            Call<CompleteOrderListResponse> calldash = mAPIService.getservingProducts(((ProductsClient) HomeActivity.this.getApplicationContext()).getPhone_no());
            calldash.enqueue(new Callback<CompleteOrderListResponse>() {
                @Override
                public void onResponse(Call<CompleteOrderListResponse> call, final Response<CompleteOrderListResponse> response) {
                    if (response.isSuccessful()) {
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).setServingList(response.body().getProducts());
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).completeServingList();
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).completeEditingList();
                        ((ProductsClient) HomeActivity.this.getApplicationContext()).setServingStatus(1);

                        loadfragment(new AllItemsFragment());

                    } else {
                        Toast.makeText(HomeActivity.this, "Response from server is not successful", Toast.LENGTH_LONG).show();

                        loadfragment(new AllItemsFragment());
                    }
                }

                @Override
                public void onFailure(Call<CompleteOrderListResponse> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            loadfragment(new AllItemsFragment());
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
