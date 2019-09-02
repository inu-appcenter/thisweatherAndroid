package com.inu.thisweather.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.inu.thisweather.R;
import com.inu.thisweather.adapter.InitialPagerAdapter;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

public class InitialActivity extends FragmentActivity {

    ViewPager viewPager;
    ViewPagerIndicator viewPagerIndicator;
    ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        nextButton = (ImageView) findViewById(R.id.iv_next);

        setViewpager();
        setNext();
    }

    private void setViewpager() {
        FragmentManager fm = getSupportFragmentManager();
        InitialPagerAdapter pagerAdapter = new InitialPagerAdapter(fm);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

        viewPagerIndicator.setupWithViewPager(viewPager);
        viewPagerIndicator.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                if(position == 1){
                    setFinish();
                }
                else{
                    setNext();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
    }

    private void setNext() {
        nextButton.setImageResource(R.drawable.btn_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
    }

    public void setFinish() {
        nextButton.setImageResource(R.drawable.btn_finish);
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        final Intent intent = new Intent(this, MainActivity.class);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                editor.putBoolean("init", true); //TODO true로
                editor.apply();
                finish();
            }
        });
    }
}