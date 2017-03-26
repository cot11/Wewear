package com.example.cot11.wewear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    @Override public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        circledicator indicator = (circledicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new PagerAdapater());
        indicator.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Button button = (Button)view.findViewById(R.id.button1);
                if(position+1 == 5)
                {
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((FirstActivity) getActivity()).SetFirestKey();
                        }
                    });
                }
                else
                {
                    button.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}