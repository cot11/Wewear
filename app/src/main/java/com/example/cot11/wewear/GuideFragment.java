package com.example.cot11.wewear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.circledicator.circledicator;

/**
 * Created by 이언우 on 2017-03-10.
 */

public class GuideFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guidefragment, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("ZXC");
                return false;
            }
        });
        circledicator indicator = (circledicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new PagerAdapater());
        indicator.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }
}