package com.example.thisweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class InitialSecondFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_second, null);

        LottieAnimationView animationView = view.findViewById(R.id.lav_tutorial2);
        animationView.playAnimation();

        return view;
    }

}