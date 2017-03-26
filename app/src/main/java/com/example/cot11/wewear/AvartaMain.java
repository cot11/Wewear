package com.example.cot11.wewear;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

/**
 * Created by 이언우 on 2017-03-13.
 */

public class AvartaMain extends AppCompatActivity{
    private boolean isFragmentB = true ;
    Thread thread;
    private FragmentManager fm;
    private ProgressDialog dialog;
    Button button;

    String[] MenuSet = new String[4];



    public void brandSet(String brand, boolean back)
    {

        Shopping shopping = new Shopping();
        Bundle args = new Bundle();
        args.putString("Brand",brand);
        args.putBoolean("Back", back);
        shopping.setArguments(args);

        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Fragment_change,shopping);
        fragmentTransaction.commit();
    }

    public void openWeb(String Link, String Brand)
    {
        webView webView = new webView();
        Bundle args = new Bundle();
        args.putString("Link",Link);
        args.putString("Brand",Brand);
        webView.setArguments(args);

        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Fragment_change,webView);
        fragmentTransaction.commit();
    }
    public void backtoFrag(String brand)
    {
        brandSet(brand, false);
    }

    public void ProgressRun()
    {
        dialog = ProgressDialog.show(this, "", "Please wait....", true);
    }

    public void ProgressStop()
    {
        dialog.dismiss();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ... 코드 계속 [STE]
        MenuSet[0] = "아바타 만들기";
        MenuSet[1] = "아바타 만들기2";
        MenuSet[2] = "아바타 만들기3";
        MenuSet[3] = "아바타 만들기4";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button)findViewById(R.id.avarta);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Fragment_change, new Avarta());
        fragmentTransaction.commit();

        //Action Bar code start

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("wewear");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);

        BoomMenuButton rightBmb = (BoomMenuButton) actionBar.findViewById(R.id.action_bar_right_bmb);
        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
        rightBmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);

        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++)
        {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.peacock)
                    .normalColor(Color.BLACK)
                    .normalText(MenuSet[i])
                    .subNormalText(MenuSet[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Toast.makeText(AvartaMain.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                            if(index == 0)
                            {
                                Intent intent = new Intent(AvartaMain.this, SuccessActivity.class);
                                intent.putExtra("userprofile","10");
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
            rightBmb.addBuilder(builder);
            //rightBmb.addBuilder(BuilderManager.getHamButtonBuilderWithDifferentPieceColor());
        }


    }

    public void tab_change(View v)
    {
        fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId())
        {
            case R.id.avarta:
                fragmentTransaction.replace(R.id.Fragment_change, new Avarta());
                fragmentTransaction.commit();
                break;
            case R.id.shopping:
                fragmentTransaction.replace(R.id.Fragment_change, new Brand());
                fragmentTransaction.commit();
                break;
            case R.id.ranking:
                fragmentTransaction.replace(R.id.Fragment_change, new Ranking());
                fragmentTransaction.commit();
                break;
        }
    }

}
