package com.example.thisweather.view;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieImageAsset;
import com.example.thisweather.R;

public class InitialFirstFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_first, null);

//        LottieAnimationView animationView = view.findViewById(R.id.lav_tutorial1);
//        animationView.playAnimation();

        ImageView iv = view.findViewById(R.id.imageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv.getBackground();
        animationDrawable.start();

        return view;
    }

}