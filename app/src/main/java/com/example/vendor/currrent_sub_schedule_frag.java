package com.example.vendor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class currrent_sub_schedule_frag extends Fragment {

    RecyclerView subsItems_recyclerView;

    RecyclerView.Adapter adapter=null;
    private List<schedule_dataholder> list= new ArrayList<>();

    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_currrent_sub_schedule, container, false);


        subsItems_recyclerView= rootView.findViewById(R.id.subsItems_recyclerView);
        subsItems_recyclerView.setHasFixedSize(true);
        subsItems_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        for(int j=0;j<5;j++)
        {
            date.add("date");
            time.add("time");
        }

        for (int k=0;k<5;k++) {
            schedule_dataholder List = new schedule_dataholder(date.get(k),time.get(k));
            list.add(List);
        }
        adapter = new currrent_sub_schedule_adapter(list,getActivity());
        subsItems_recyclerView.setAdapter(adapter);
        adapter=null;
        list=new ArrayList<>();


        return rootView;
    }
}
