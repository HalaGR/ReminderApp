package com.example.locationreminder;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class LocationAdapter extends FragmentStatePagerAdapter {
    private Context context;
    int totalTabs;


    public LocationAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;

    }
    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){

                MapsFragment MapsFragment=new MapsFragment();
                return MapsFragment;




    }
}