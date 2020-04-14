package com.example.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Adapters.OngoingAdapter;
import com.example.Models.MyHistoryItems;
import com.example.Models.NewOrders;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;
import java.util.List;

public class OngoingFragment extends Fragment {
    View v;
    private List<ProductDescriptionResponse> myItemList;
    private RecyclerView rv;
    private OngoingAdapter iga;
    FragmentManager manager;
    private FragmentActivity myContext;
    private OrderDescriptionResponse order;
    private static final String DESCRIBABLE_KEY = "describable_key";

    public OngoingFragment(){}

    public static OngoingFragment newInstance(OrderDescriptionResponse orders) {
        OngoingFragment fragment = new OngoingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, orders);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.ongoingfragmentlayout, container, false);

        manager = myContext.getSupportFragmentManager();
        order = (OrderDescriptionResponse) getArguments().getSerializable(DESCRIBABLE_KEY);

        myItemList = order.getItems();

        rv = v.findViewById(R.id.ongoingItems);
        loadItems();

        return v;
    }

    public void loadItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new OngoingAdapter(getContext(), myItemList);
        rv.setAdapter(iga);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
}
