package com.example.thisweather.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thisweather.R;

public class InitialFirstFragment  extends Fragment {
    AnimationDrawable animationDrawable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_first, null);

        ImageView iv = view.findViewById(R.id.iv_tutorial1);
        animationDrawable = (AnimationDrawable) iv.getBackground();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        animationDrawable.stop();
    }
}