package com.example.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.Adapters.Subscription_Page_Adapter;
import com.example.Constants.ApiInterface;
import com.example.Constants.ApiUtils;
import com.example.Models.CompleteOrderListResponse;
import com.example.vendor.ProductsClient;
import com.example.vendor.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllItemsFragment extends Fragment {

    View v;
    private Button editItems;
    private Button saveItems;
    private SearchView searchView;
    private boolean switching = false;
    public ViewPager viewPagersub;
    public TabLayout tabLayoutsub;
    public Subscription_Page_Adapter subscription_page_adapter;
    public static int post=0;
    public List<Fragment> loadedFragment = new ArrayList<>();
    private FragmentManager fragmentManager;
    private ItemFragment itemFragment;
    private JuicesFragment juicesFragment;
    private RawFragment rawFragment;
    private int listCheck = 0;
    private int myItemListStatus = 0;
    private ApiInterface mAPIService;


    public AllItemsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.allitems, container, false);

        editItems = v.findViewById(R.id.editItems);
        saveItems = v.findViewById(R.id.saveItems);
        searchView = (SearchView) v.findViewById(R.id.searchItems);
        mAPIService = ApiUtils.getAPIService();

        fragmentManager=getChildFragmentManager();

        itemFragment = new ItemFragment();
        juicesFragment = new JuicesFragment();
        rawFragment = new RawFragment();

        loadedFragment.add(itemFragment);
        loadedFragment.add(juicesFragment);
        loadedFragment.add(rawFragment);
        listCheck = ((ProductsClient)getContext().getApplicationContext()).getCheck();


        viewPagersub = v.findViewById(R.id.viewpagersubscription);
        subscription_page_adapter = new Subscription_Page_Adapter(fragmentManager,loadedFragment);
        viewPagersub.setAdapter(subscription_page_adapter);

        tabLayoutsub=v.findViewById(R.id.tablayoutsubscription);
        tabLayoutsub.setupWithViewPager(viewPagersub);



        viewPagersub.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                post=viewPagersub.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {
                if(post == 0){
                    itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
                    itemFragment.filter(s);
                }
                else if(post == 1) {
                    juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(1));
                    juicesFragment.filter(s);
                }
                else{
                    rawFragment = ((RawFragment) getChildFragmentManager().getFragments().get(1));
                    rawFragment.filter(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(post == 0){
//                    itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
                    itemFragment.filter(s);
                }
                else if(post == 1) {
//                    juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(0));
                    juicesFragment.filter(s);
                }
                else{
//                    rawFragment = ((RawFragment) getChildFragmentManager().getFragments().get(0));
                    rawFragment.filter(s);
                }

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose() {
                if(post == 0){
                    itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
                    itemFragment.filter("");
                }
                else if(post == 1) {
                    juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(1));
                    juicesFragment.filter("");
                }
                else{
                    rawFragment = ((RawFragment) getChildFragmentManager().getFragments().get(1));
                    rawFragment.filter("");
                }
                return true;
            }
        });


        editItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(post == 0){
                    itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
                    itemFragment.editItems();
                }
                else if(post == 1) {
                    juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(1));
                    juicesFragment.editItems();
                }
                else{
                    rawFragment = ((RawFragment) getChildFragmentManager().getFragments().get(1));
                    rawFragment.editItems();
                }

//                itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
//                itemFragment.editItems();
            }
        });


        saveItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(post == 0){
                    List<Fragment> listFrag = getChildFragmentManager().getFragments();
//                    itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
                    itemFragment = ((ItemFragment) listFrag.get(0));
                    itemFragment.saveItems();
                }
                else if(post == 1) {
                    juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(1));
                    juicesFragment.saveItems();
                }
                else{
                    rawFragment = ((RawFragment) getChildFragmentManager().getFragments().get(1));
                    rawFragment.saveItems();
                }

//                itemFragment = ((ItemFragment) getChildFragmentManager().getFragments().get(0));
//                juicesFragment = ((JuicesFragment) getChildFragmentManager().getFragments().get(1));
//                itemFragment.saveItems();
//                Toast.makeText(getContext(), juicesFragment.myJuiceList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }
}
