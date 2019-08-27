package com.example.thisweather.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thisweather.R;

import retrofit2.Retrofit;

public class MainSecondFragment extends Fragment {
    Retrofit retrofit;

    public static MainSecondFragment newInstance(){return new MainSecondFragment();}
    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_second,container,false);
//        retrofit = new Retrofit.Builder()
//                .baseUrl("http://117.16.231.66:7002/")
//                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
//                .build();

        return view;
    }

}
