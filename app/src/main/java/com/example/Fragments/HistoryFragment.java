package com.example.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.Adapters.HistoryAdapter;
import com.example.Models.OrderDescriptionResponse;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.R;

import java.util.List;

public class HistoryFragment extends Fragment {

    View v;
    private List<ProductDescriptionResponse> myItemList;
    private RecyclerView rv;
    private HistoryAdapter iga;
    private OrderDescriptionResponse order;
    private static final String DESCRIBABLE_KEY = "describable_key";
    FragmentManager manager;
    private FragmentActivity myContext;
    int pos;

    public HistoryFragment(){}

    public static HistoryFragment newInstance(OrderDescriptionResponse orders) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, orders);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_history_frag, container, false);

        manager = myContext.getSupportFragmentManager();
        order = (OrderDescriptionResponse) getArguments().getSerializable(DESCRIBABLE_KEY);

        myItemList = order.getItems();

//        rv = v.findViewById(R.id.historyItems);
//        loadItems();
        return v;
    }

    public void loadItems(){
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        iga = new HistoryAdapter(getContext(), myItemList);
        Log.v("jumanji","Gotcha");
        rv.setAdapter(iga);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

}
