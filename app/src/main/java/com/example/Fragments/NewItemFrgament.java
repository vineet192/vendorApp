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
import com.example.Models.MyItem;
import com.example.Models.NewOrders;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.OrderHistoryResponse;
import com.example.vendor.HomeActivity;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;
import com.pusher.client.Pusher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewItemFrgament extends Fragment{

    View v;
    private List<NewOrders> order_list;
    private List<MyItem> myItemList1;
    private List<MyItem> myItemList2;
    private List<MyItem> myItemList3;
    private RecyclerView rv;
    private NewOrderAdapter iga;
    private Pusher pusher;
    private ApiInterface mAPIService;
    private List<OrderDescriptionResponse> allOrderList;

    public NewItemFrgament() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.new_main_fragment, container, false);

        order_list = new ArrayList<>();
        rv = v.findViewById(R.id.new_orders);
        mAPIService = ApiUtils.getAPIService();
        allOrderList = new ArrayList<>();

//        MyItem objA1 = new MyItem("Pizza", "Chinese", "Rs 360", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objA2 = new MyItem("Fried RIce", "Non-Veg", "Rs 250", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRD7_-c04UGtD0pxnAjaAK38LMX43vwIbyXpq2Nd4yzr4DRm5zlsg&s", false);
//        MyItem objA3 = new MyItem("Burger", "Chinese", "Rs 60", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objA4 = new MyItem("Pasta", "Chinese", "Rs 70", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRD7_-c04UGtD0pxnAjaAK38LMX43vwIbyXpq2Nd4yzr4DRm5zlsg&s", false);
//
//        myItemList1 = new ArrayList<>();
//
//        myItemList1.add(objA1);
//        myItemList1.add(objA2);
//        myItemList1.add(objA3);
//        myItemList1.add(objA4);
//
//        MyItem objB1 = new MyItem("Pizza", "Chinese", "Rs 360", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objB3 = new MyItem("Burger", "Chinese", "Rs 60", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objB4 = new MyItem("Pasta", "Chinese", "Rs 70", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRD7_-c04UGtD0pxnAjaAK38LMX43vwIbyXpq2Nd4yzr4DRm5zlsg&s", false);
//
//        myItemList2 = new ArrayList<>();
//
//        myItemList2.add(objB1);
//        myItemList2.add(objB3);
//        myItemList2.add(objB4);
//
//        MyItem objC1 = new MyItem("Pasta", "Chinese", "Rs 30", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objC3 = new MyItem("Burger", "Chinese", "Rs 60", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpBV-gqYE2q30LAzIwFnv6A4jOpRTxkm0P9Kwdy9-V2mw8SExr&s", false);
//        MyItem objC4 = new MyItem("Maggi", "Chinese", "Rs 70", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRD7_-c04UGtD0pxnAjaAK38LMX43vwIbyXpq2Nd4yzr4DRm5zlsg&s", false);
//
//        myItemList3 = new ArrayList<>();
//
//        myItemList3.add(objC1);
//        myItemList3.add(objC3);
//        myItemList3.add(objC4);
//
//        NewOrders o1 = new NewOrders("A63jochn273", 1000, myItemList1);
//        NewOrders o2 = new NewOrders("Bjhd67gw8dd", 300, myItemList2);
//        NewOrders o3 = new NewOrders("Chudb7w892y", 500, myItemList3);
//
//        order_list.add(o1);
//        order_list.add(o2);
//        order_list.add(o3);

        Call<OrderHistoryResponse> calldash = mAPIService.getNewOrders(((ProductsClient)getContext().getApplicationContext()).getPhone_no());
        calldash.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, final Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()) {
                    allOrderList = response.body().getOrders();
                    loadItems();
                } else {
                    Toast.makeText(getContext(), "Response from server is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        PusherOptions options = new PusherOptions();
//        options.setCluster("ap2");
//        pusher = new Pusher("7c495f369f4053064877", options);
//
//        Channel channel = pusher.subscribe("my-channel");
//
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(final PusherEvent event) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            JSONObject object = new JSONObject(event.getData());
////                            textView.setText(object.getJSONArray("orders").get(0));
////                            Toast.makeText(getContext(), object.getJSONArray("orders").get(0).toString(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), object.toString(), Toast.LENGTH_SHORT).show();
//                            OrderHistoryResponse data = new Gson().fromJson(object.toString(), OrderHistoryResponse.class);
//                            allOrderList = data.getOrders();
//                            iga.notifyDataSetChanged();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//
//        pusher.connect();

//        loadItems();

        return v;
    }


    public void loadItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new NewOrderAdapter(getContext(), allOrderList);
        rv.setAdapter(iga);
        iga.setOnItemClickListener(new NewOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ((HomeActivity) Objects.requireNonNull(getActivity())).loadOrderDescription(allOrderList.get(position));
            }
        });
    }

}
