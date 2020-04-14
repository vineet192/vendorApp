package com.example.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapters.ItemsAdapter;
import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.ItemSavingResponse;
import com.example.Models.MyItem;
import com.example.Models.ProductDescriptionResponse;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuicesFragment extends Fragment {

    private boolean switching = false;
    public List<MyItem> myJuiceList;
    public List<ProductDescriptionResponse> myNewJuiceList;
    private RecyclerView rv;
    private ItemsAdapter iga;
    private ApiInterface mAPIService;

    View v;

    public JuicesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.juicesfragmentlayout, container, false);

        mAPIService = ApiUtils.getAPIService();
        myNewJuiceList = ((ProductsClient)getContext().getApplicationContext()).getMyJuicesList();


        rv = v.findViewById(R.id.servingjuices);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadItems();
        SearchView sv = getParentFragment().getView().findViewById(R.id.searchItems);
        if(!TextUtils.isEmpty(sv.getQuery())) {
            filter(sv.getQuery().toString());
        }
        return v;
    }


    public void loadItems(){

        iga = new ItemsAdapter(getContext(), myNewJuiceList, switching);
        rv.setAdapter(iga);

        iga.setOnStateChangeListner(new ItemsAdapter.OnStateChangeListner() {
            @Override
            public void newstateChanged(final int position) {
//                Toast.makeText(getContext(), Integer.toString(position)+" "+myJuiceList.get(position).isCheck(), Toast.LENGTH_SHORT).show();
                myNewJuiceList.get(position).setCheck(!myNewJuiceList.get(position).isCheck());
//                Toast.makeText(getContext(), Integer.toString(position)+" "+myNewJuiceList.get(position).isCheck(), Toast.LENGTH_SHORT).show();
                rv.post(new Runnable() {
                    @Override
                    public void run() {
                iga.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void filter(String text){
        List<ProductDescriptionResponse> newItemList = new ArrayList<>();

        for(ProductDescriptionResponse item : myNewJuiceList){
            if(item.getProd_name().toLowerCase().contains(text.toLowerCase())){
                newItemList.add(item);
            }
        }

        iga.filteredlists((ArrayList<ProductDescriptionResponse>) myNewJuiceList);
    }

    public void saveItems(){
        switching = false;
        iga.state = false;

        List<ProductDescriptionResponse> newItemList = new ArrayList<>();
        for(ProductDescriptionResponse item : myNewJuiceList){
            if(item.isCheck()){
                newItemList.add(item);
            }
        }

        ((ProductsClient)getContext().getApplicationContext()).setMyJuicesList(newItemList);
        myNewJuiceList = newItemList;

        String phone_no = ((ProductsClient)getContext().getApplicationContext()).getPhone_no();
        List<ProductDescriptionResponse> vegetable = new ArrayList<>();
        List<ProductDescriptionResponse> raw = new ArrayList<>();;
        vegetable = ((ProductsClient)getContext().getApplicationContext()).getMyVegetableList();
        raw = ((ProductsClient)getContext().getApplicationContext()).getMyRawList();
        ArrayList<Integer> productId = new ArrayList<>();
        for(ProductDescriptionResponse item : myNewJuiceList){
            productId.add(item.getProd_id());
        }
        for(ProductDescriptionResponse item : vegetable){
            productId.add(item.getProd_id());
        }
        for(ProductDescriptionResponse item : raw){
            productId.add(item.getProd_id());
        }

        Call<ItemSavingResponse> calldash = mAPIService.saveMyItems(phone_no, productId);
        calldash.enqueue(new Callback<ItemSavingResponse>() {
            @Override
            public void onResponse(Call<ItemSavingResponse> call, final Response<ItemSavingResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess().equals("true")){
                        Toast.makeText(getContext(), "Items Saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Items Not Saved", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Response from server is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemSavingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        String s = "";
        for(Integer selectedItem : productId){
            s += selectedItem+" ";
        }
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        loadItems();
        iga.notifyDataSetChanged();
    }

    public void editItems(){
        myNewJuiceList = ((ProductsClient)getContext().getApplicationContext()).getEditableJuicesList();
        switching = true;
        iga.state = true;
        loadItems();
        iga.notifyDataSetChanged();
    }

}
