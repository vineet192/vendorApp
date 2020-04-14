package com.example.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapters.NewOrderAdapter;
import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.MyHistoryItems;
import com.example.Models.NewOrders;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.OrderHistoryResponse;
import com.example.vendor.HomeActivity;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {


    View v;
//    private List<NewOrders> order_list;
//    private List<MyHistoryItems> myItemList1;
//    private List<MyHistoryItems> myItemList2;
//    private List<MyHistoryItems> myItemList3;
    private RecyclerView rv;
    private NewOrderAdapter iga;
    private ApiInterface mAPIService;
    private List<OrderDescriptionResponse> myOrders;

    public OrderHistoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_history_fragment, container, false);

//        order_list = new ArrayList<>();
        rv = v.findViewById(R.id.history_orders);
        mAPIService = ApiUtils.getAPIService();

        Call<OrderHistoryResponse> calldash = mAPIService.getMyHistory(((ProductsClient)getContext().getApplicationContext()).getPhone_no());
        calldash.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, final Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()) {
                    myOrders = response.body().getOrders();
                    loadhistoryItems();
                } else {
                    Toast.makeText(getContext(), "Response from server is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }


    public void loadhistoryItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new NewOrderAdapter(getContext(), myOrders);
        rv.setAdapter(iga);

        iga.setOnItemClickListener(new NewOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ((HomeActivity)getActivity()).loadHistoryfragment(myOrders.get(position));
            }
        });
    }

}
