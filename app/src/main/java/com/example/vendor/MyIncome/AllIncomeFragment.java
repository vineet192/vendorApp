package com.example.vendor.MyIncome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vendor.MyIncome.Adapter_Income.TotalIncomeAdapter;
import com.example.vendor.MyIncome.Models_Income.TotalIncomeModel;
import com.example.vendor.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllIncomeFragment extends Fragment {

    private RecyclerView recyclerView;

    TotalIncomeAdapter totalIncomeAdapter;
    List<TotalIncomeModel> incomeList;

    public AllIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_income, container, false);

        incomeList = new ArrayList<>();
        addDataToIncomeList();

        recyclerView = view.findViewById(R.id.allIncome_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        totalIncomeAdapter = new TotalIncomeAdapter(getActivity(), incomeList);
        recyclerView.setAdapter(totalIncomeAdapter);

        return view;
    }

    private void addDataToIncomeList(){

        incomeList.add(new TotalIncomeModel("January, 2020", "2000"));
        incomeList.add(new TotalIncomeModel("December, 2019", "3000"));
        incomeList.add(new TotalIncomeModel("November, 2019", "5000"));
        incomeList.add(new TotalIncomeModel("October, 2019", "3500"));
        incomeList.add(new TotalIncomeModel("September, 2019", "2000"));
        incomeList.add(new TotalIncomeModel("August, 2019", "4000"));

    }

}
