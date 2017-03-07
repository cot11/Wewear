package com.example.cot11.wewear;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by cot11 on 2017-03-04.
 */

public class FirstActivity extends Activity{

    private ViewPager mPager;

    @Override
    protected  void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.fitst_activity);

        mPager = (ViewPager)findViewById(R.id.Pager);
        mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));



    }

    private View.OnClickListener mCloseButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int infoFirst = 1;
            SharedPreferences sharedPreferences = getSharedPreferences("A",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("First",infoFirst);
            editor.commit();
            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    private class PagerAdapterClass extends PagerAdapter
    {
        private LayoutInflater mInflater;
        public PagerAdapterClass(Context C)
        {
            super();
            mInflater = LayoutInflater.from(C);
        }


        @Override
        public int getCount()
        {
            return 4;
        }

        @Override
        public Object instantiateItem(View pager, int position)
        {
            View v = null;
            if(position == 0)
            {
                v = mInflater.inflate(R.layout.guide1,null);
                v.findViewById(R.id.guide1);
            }
            else if(position == 1)
            {
                v = mInflater.inflate(R.layout.guide2,null);
                v.findViewById(R.id.guide2);

            }
            else if(position == 2)
            {
                v = mInflater.inflate(R.layout.guide3,null);
                v.findViewById(R.id.guide3);
            }
            else  if(position == 3)
            {
                v = mInflater.inflate(R.layout.guide4,null);
                v.findViewById(R.id.guide4);
                v.findViewById(R.id.close).setOnClickListener(mCloseButton);
            }
            ((ViewPager)pager).addView(v,0);
            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view)
        {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj)
        {
            return pager == obj;
        }

        @Override public void restoreState(Parcelable arg0, ClassLoader arg1){}
        @Override public Parcelable saveState(){return null;}
        @Override public void startUpdate(View arg0){}
        @Override public void finishUpdate(View arg0){}

    }

}
