package com.example.thisweather;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabFragment1 extends Fragment {
    View view;
    TextView max, min, windspeed, rainfall, sensetemp, dust;
    String d_max, d_min, d_windspeed, d_rainfall, d_sensetemp, d_dust;

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("data"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver2,
                new IntentFilter("dust"));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1,container,false);

        max = view.findViewById(R.id.tv_maxtemp);
        min = view.findViewById(R.id.tv_mintemp);
        windspeed = view.findViewById(R.id.tv_windspeed);
        rainfall = view.findViewById(R.id.tv_rainfall);
        sensetemp = view.findViewById(R.id.tv_sensetemp);
        dust = view.findViewById(R.id.tv_pm10);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData(d_max, d_min, d_rainfall, d_windspeed, d_sensetemp);
        setDust(d_dust);
        Log.d("test", "resume: "+d_dust);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            d_max = intent.getStringExtra("max");
            d_min = intent.getStringExtra("min");
            d_rainfall = intent.getStringExtra("rainfall");
            d_windspeed = intent.getStringExtra("windspeed");
            d_sensetemp = intent.getStringExtra("sensetemp");
            setData(d_max, d_min, d_rainfall, d_windspeed, d_sensetemp);
        }
    };

    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            d_dust = intent.getStringExtra("dust");
            setDust(d_dust);
            Log.d("test", "broad: "+d_dust);
        }
    };

    private void setData(String data1, String data2, String data3, String data4, String data5) {
        max.setText(data1);
        min.setText(data2);
        rainfall.setText(data3);
        windspeed.setText(data4);
        sensetemp.setText(data5);
    }

    private void setDust(String data) {
        dust.setText(data);
    }
}