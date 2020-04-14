package com.example.vendor.MyIncome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vendor.MyIncome.Adapter_Income.TodayIncomeAdapter;
import com.example.vendor.MyIncome.Models_Income.TodayIncomeModel;
import com.example.vendor.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodayIncomeFragment extends Fragment {

    private RecyclerView recyclerView;

    TodayIncomeAdapter todayIncomeAdapter;
    List<TodayIncomeModel> incomeList;

    public TodayIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today_income, container, false);

        incomeList = new ArrayList<>();
        addDataToIncomeList();

        recyclerView = view.findViewById(R.id.todayIncome_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        todayIncomeAdapter = new TodayIncomeAdapter(getActivity(), incomeList);
        recyclerView.setAdapter(todayIncomeAdapter);

        return view;
    }

    private void addDataToIncomeList() {

        for (int i=0; i<15; i++){
            TodayIncomeModel todayIncomeModel = new TodayIncomeModel("A123" + i, "100" + i);
            incomeList.add(todayIncomeModel);
        }
    }
}
