package com.example.thisweather;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieImageAsset;

public class InitialFirstFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_first, null);

        LottieAnimationView animationView = view.findViewById(R.id.animation_view1);
//        animationView.setImageAssetDelegate(new ImageAssetDelegate() {
//            @Override public Bitmap fetchBitmap(LottieImageAsset asset) {
//                if (downloadedBitmap == null) {
//                     We don't have it yet. Lottie will keep
//                     asking until we return a non-null bitmap.
//                    return null;
//                }
//                return downloadedBitmap;
//            }
//        });

        return view;
    }

}