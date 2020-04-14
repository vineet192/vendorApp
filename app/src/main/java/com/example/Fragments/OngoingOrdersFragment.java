package com.example.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.TaskExecutor;
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

public class OngoingOrdersFragment extends Fragment {

    View v;
    private TextView heading;
    private RecyclerView rv;
    private NewOrderAdapter iga;
    private ApiInterface mAPIService;
    private List<OrderDescriptionResponse> myOrders;
    private Pusher pusher;
    private String PhoneNumber;

    public OngoingOrdersFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_history_fragment, container, false);

//        order_list = new ArrayList<>();
        rv = v.findViewById(R.id.history_orders);
        heading = v.findViewById(R.id.heading);
        heading.setText("Ongoing Orders");
        mAPIService = ApiUtils.getAPIService();
        PhoneNumber = ((ProductsClient)getContext().getApplicationContext()).getPhone_no();

        Call<OrderHistoryResponse> calldash = mAPIService.getNewOrders(PhoneNumber);
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


        PusherOptions options = new PusherOptions();
        options.setCluster("ap2");
        pusher = new Pusher("7c495f369f4053064877", options);

        Channel channel = pusher.subscribe("vendor" + PhoneNumber.toString());

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(final PusherEvent event) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject object = new JSONObject(event.getData());
                            Toast.makeText(getContext(), object.toString(), Toast.LENGTH_SHORT).show();
                            OrderDescriptionResponse data = new Gson().fromJson(object.toString(), OrderDescriptionResponse.class);
                            myOrders.add(data);
                            iga.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        pusher.connect();

        return v;
    }


    public void loadhistoryItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new NewOrderAdapter(getContext(), myOrders);
        rv.setAdapter(iga);

        iga.setOnItemClickListener(new NewOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ((HomeActivity)getActivity()).loadOngoingfragment(myOrders.get(position));
            }
        });
    }

}
