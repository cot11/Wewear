package com.example.cot11.wewear;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
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

    public void SetFirestKey()
    {
        int infoFirst = 1;
        SharedPreferences sharedPreferences = getSharedPreferences("A",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("First",infoFirst);
        editor.commit();
        Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_LONG).show();
        finish();
    }
}
