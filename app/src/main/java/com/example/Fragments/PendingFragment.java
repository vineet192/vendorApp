package com.example.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Adapters.NewItemAdapter;
import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.DeleiveryBoy;
import com.example.Models.ItemSavingResponse;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.OrderHistoryResponse;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.HomeActivity;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {

    View v;
    private SearchView searchNewItems;
    private RecyclerView rv;
    private List<ProductDescriptionResponse> myItemList;
    private NewItemAdapter iga;
    private Button accept;
    FragmentManager manager;
    private FragmentActivity myContext;
    private Button decline;
    private OrderDescriptionResponse order;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private ApiInterface mAPIService;


    public PendingFragment(){}

    public static PendingFragment newInstance(OrderDescriptionResponse orders) {
        PendingFragment fragment = new PendingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, orders);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pendingfragmentlayout, container, false);

        manager = myContext.getSupportFragmentManager();
        order = (OrderDescriptionResponse) getArguments().getSerializable(DESCRIBABLE_KEY);
        mAPIService = ApiUtils.getAPIService();

        searchNewItems = (SearchView) v.findViewById(R.id.searchNewItems);
        accept = (Button) v.findViewById(R.id.acceptNew);
        decline = (Button) v.findViewById(R.id.declineNew);

        myItemList = order.getItems();

        rv = v.findViewById(R.id.newItems);
        loadItems();

        searchNewItems.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return true;
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<ProductDescriptionResponse> newItemList = new ArrayList<>();
                for(ProductDescriptionResponse item : myItemList){
                    if(item.isCheck()){
                        newItemList.add(item);
                    }
                }
                String s = "";
                for(ProductDescriptionResponse selectedItem : newItemList){
                    s += selectedItem.getProd_name()+" ";
                }
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

                Call<DeleiveryBoy> calldash = mAPIService.getDeleiveryBoy(((ProductsClient)getContext().getApplicationContext()).getPhone_no(), order.getOrder_id());
                calldash.enqueue(new Callback<DeleiveryBoy>() {
                    @Override
                    public void onResponse(Call<DeleiveryBoy> call, final Response<DeleiveryBoy> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getDel_boy_name().equals("NoName")){
                                Toast.makeText(getContext(), "No deleivery boy assigned yet !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ((HomeActivity) getActivity()).loadDeleiveryBoyDetails(response.body());
                            }
                        } else {
                            Toast.makeText(getContext(), "Response from server is not successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleiveryBoy> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<ItemSavingResponse> calldash = mAPIService.dispatchOrder(((ProductsClient)getContext().getApplicationContext()).getPhone_no(), order.getOrder_id());
                calldash.enqueue(new Callback<ItemSavingResponse>() {
                    @Override
                    public void onResponse(Call<ItemSavingResponse> call, final Response<ItemSavingResponse> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getSuccess().equals("true")){
                                Toast.makeText(getContext(), "Details Saved !", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Unable to save details !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Response from server is not successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ItemSavingResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        iga.setOnItemClickListener(new NewItemAdapter.OnItemClickListener() {

            @Override
            public void OnItemChecked(int position) {
                myItemList.get(position).setCheck(!myItemList.get(position).isCheck());
                iga.notifyDataSetChanged();
            }
        });

        return v;
    }

    public void loadItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new NewItemAdapter(getContext(), myItemList);
        rv.setAdapter(iga);
    }

    private void filter(String text){
        List<ProductDescriptionResponse> newItemList = new ArrayList<>();

        for(ProductDescriptionResponse item : myItemList){
            if(item.getProd_name().toLowerCase().contains(text.toLowerCase())){
                newItemList.add(item);
            }
        }

        iga.filteredlists((ArrayList<ProductDescriptionResponse>) newItemList);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

}
