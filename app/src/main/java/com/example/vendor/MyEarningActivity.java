package com.example.vendor;

import android.os.Bundle;
import android.view.View;

import com.example.vendor.MyIncome.AllIncomeFragment;
import com.example.vendor.MyIncome.MonthIncomeFragment;
import com.example.vendor.MyIncome.TodayIncomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MyEarningActivity extends AppCompatActivity {

    View todayIncomeLy;
    View monthIncomeLy;
    View allIncomeLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earning);

        if (savedInstanceState == null) {
            TodayIncomeFragment todayIncomeFragment = new TodayIncomeFragment();
            FragmentTransaction today_fragmentTransaction = getSupportFragmentManager().beginTransaction();
            today_fragmentTransaction.replace(R.id.myIncome_fragmentContainer, todayIncomeFragment, "");
            today_fragmentTransaction.commit();
        }

        todayIncomeLy = findViewById(R.id.income_todayLy);
        todayIncomeLy.setOnClickListener(view -> {
            TodayIncomeFragment todayIncomeFragment = new TodayIncomeFragment();
            FragmentTransaction today_fragmentTransaction = getSupportFragmentManager().beginTransaction();
            today_fragmentTransaction.replace(R.id.myIncome_fragmentContainer, todayIncomeFragment, "");
            today_fragmentTransaction.commit();
        });

        monthIncomeLy = findViewById(R.id.income_monthLy);
        monthIncomeLy.setOnClickListener(view -> {
            MonthIncomeFragment monthIncomeFragment = new MonthIncomeFragment();
            FragmentTransaction month_fragmentTransaction = getSupportFragmentManager().beginTransaction();
            month_fragmentTransaction.replace(R.id.myIncome_fragmentContainer, monthIncomeFragment, "");
            month_fragmentTransaction.commit();
        });

        allIncomeLy = findViewById(R.id.income_allLy);
        allIncomeLy.setOnClickListener(view -> {
            AllIncomeFragment allIncomeFragment = new AllIncomeFragment();
            FragmentTransaction all_fragmentTransaction = getSupportFragmentManager().beginTransaction();
            all_fragmentTransaction.replace(R.id.myIncome_fragmentContainer, allIncomeFragment, "");
            all_fragmentTransaction.commit();
        });

    }
}
