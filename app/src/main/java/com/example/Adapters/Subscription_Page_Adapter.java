package com.example.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.Fragments.ItemFragment;

import java.util.List;

public class Subscription_Page_Adapter extends FragmentPagerAdapter {

    private List<Fragment> myList;

    public Subscription_Page_Adapter(FragmentManager fm, List<Fragment> myList) {

        super(fm);
        this.myList = myList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myList.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
        {
            return "Vegetables";
        }
        else if (position==1){
            return  "Juices";
        }
        else {
            return "Raw";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
