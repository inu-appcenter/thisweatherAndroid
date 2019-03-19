package com.example.thisweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabFragment2 extends Fragment {
    Retrofit retrofit;

    public static TabFragment2 newInstance(){return new TabFragment2();}
    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_fragment_2,container,false);
        retrofit=new Retrofit.Builder()
                .baseUrl("http://172.30.121.166:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return view;
    }

}
