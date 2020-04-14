package com.example.vendor.MyIncome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vendor.MyIncome.Adapter_Income.MonthIncomeAdapter;
import com.example.vendor.MyIncome.Models_Income.MonthIncomeModel;
import com.example.vendor.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MonthIncomeFragment extends Fragment {

    private RecyclerView recyclerView;
    List<MonthIncomeModel> incomeList;
    MonthIncomeAdapter monthIncomeAdapter;

    public MonthIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_month_income, container, false);

        incomeList = new ArrayList<>();
        addDataToIncomeList();

        recyclerView = view.findViewById(R.id.monthIncome_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        monthIncomeAdapter = new MonthIncomeAdapter(getActivity(), incomeList);
        recyclerView.setAdapter(monthIncomeAdapter);

        return view;
    }

    private void addDataToIncomeList() {

        for (int i=0; i<15; i++){
            MonthIncomeModel monthIncomeModel = new MonthIncomeModel("A123" + i,"100" + i, "0" + 1 + "/12/2020");
            incomeList.add(monthIncomeModel);
        }
    }

}
