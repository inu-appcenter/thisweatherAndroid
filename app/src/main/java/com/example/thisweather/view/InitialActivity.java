package com.example.thisweather.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dpro.widgets.WeekdaysPicker;
import com.example.thisweather.R;
import com.example.thisweather.adapter.TabPagerAdapter;
import com.example.thisweather.util.AlarmDBHandler;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InitialActivity extends FragmentActivity {

    ViewPager viewPager;
    ViewPagerIndicator viewPagerIndicator;
    ImageView nextButton;
    ArrayList<Integer> days;
    String day, ampm, hour;
    AlarmDBHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        nextButton = (ImageView) findViewById(R.id.iv_next);

        setViewpager();
        setNext();

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("init"));

        mHandler = AlarmDBHandler.open(this);
    }

    private void setViewpager() {
        FragmentManager fm = getSupportFragmentManager();
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(fm);

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
                if(position == 2){
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
                if (day != null && ampm != null && hour != null)
                    mHandler.insert(day, ampm, hour, 1);
                startActivity(intent);
                editor.putBoolean("init", false); //TODO
                editor.apply();
                finish();
            }
        });
    }

    private void setDay(List<Integer> selectedDays) {
//        List<Integer> selectedDays = weekdaysPicker.getSelectedDays();
        String temp = "";
        for (int i = 0; i < selectedDays.size(); i++){
            switch (selectedDays.get(i)){
                case Calendar
                        .SUNDAY:
                    temp += "일 ";
                    break;
                case Calendar
                        .MONDAY:
                    temp += "월 ";
                    break;
                case Calendar
                        .TUESDAY:
                    temp += "화 ";
                    break;
                case Calendar
                        .WEDNESDAY:
                    temp += "수 ";
                    break;
                case Calendar
                        .THURSDAY:
                    temp += "목 ";
                    break;
                case Calendar
                        .FRIDAY:
                    temp += "금 ";
                    break;
                case Calendar
                        .SATURDAY:
                    temp += "토 ";
                    break;
            }
        }
        day = temp;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            days = intent.getIntegerArrayListExtra("days");
            ampm = intent.getStringExtra("ampm");
            hour = intent.getStringExtra("hour");
            if (days != null){
                setDay(days);
            }
//            Log.d("day", "days: " + days + " ampm: " + ampm + " hour: " + hour);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        mHandler.close();
    }
}