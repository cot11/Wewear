package com.example.cot11.wewear;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by cot11 on 2017-03-04.
 */

public class FirstActivity extends AppCompatActivity {

    private ViewPager mPager;

    @Override
    protected  void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.fitst_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Fragment demoFragment = Fragment.instantiate(this, GuideFragment.class.getName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, demoFragment);
        fragmentTransaction.addToBackStack(GuideFragment.class.getName());
        fragmentTransaction.commit();


        //mPager = (ViewPager)findViewById(R.id.Pager);
        //mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));


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


}
