package com.example.thisweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thisweather.view.InitialFirstFragment;
import com.example.thisweather.view.InitialSecondFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount = 3;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                InitialFirstFragment tabFragment1 = new InitialFirstFragment();
                return tabFragment1;
            case 1:
                InitialSecondFragment tabFragment2 = new InitialSecondFragment();
                return tabFragment2;
            case 2:
                InitialThirdFragment tabFragment3 = new InitialThirdFragment();
                return tabFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}