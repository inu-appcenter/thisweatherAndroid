package com.inu.thisweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inu.thisweather.view.fragment.MainFirstFragment;
import com.inu.thisweather.view.fragment.MainSecondFragment;
import com.inu.thisweather.view.fragment.MainThirdFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MainPagerAdapter(FragmentManager fm, int NumOfTabs){
        super(fm);
        this.mNumOfTabs=NumOfTabs;
    }

    @Override
    public  Fragment getItem(int position){
        switch(position){
            case 0:
                MainFirstFragment tab1 = new MainFirstFragment();
                return tab1;
            case 1:
                MainSecondFragment tab2 = new MainSecondFragment();
                return tab2;
            case 2:
                MainThirdFragment tab3 = new MainThirdFragment();
                return tab3;
            default:
                    return null;
        }

    }
    @Override
    public int getCount(){
        return mNumOfTabs;
    }
}
