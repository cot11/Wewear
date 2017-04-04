package com.example.cot11.wewear;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

/**
 * Created by 이언우 on 2017-03-13.
 */

public class AvartaMain extends AppCompatActivity{
    private boolean isFragmentB = true ;
    Thread thread;
    private FragmentManager fm;
    private ProgressDialog dialog;
    private int FragmentHeight = 0;
    private int FragmentWidth = 0;
    private int Current_Code = 0;
    private ProductAdapter productAdapter;
    LinearLayout avarta_button;
    LinearLayout shopping_button;
    LinearLayout ranking_button;


    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    boolean[] code_bool = new boolean[4];
    String[] code_String = new String[4];
    private ArrayList<productList> productListArrayList1 = new ArrayList<productList>();

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

    public ProductAdapter test(final String Brandname, final View view)
    {
        final Context context = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Storage 이미지 다운로드 경로
        mDatabase.child("Clothes").child(Brandname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren() ) {
                    productList productList = new productList();
                    productList.setName(post.getKey());
                    for(DataSnapshot post2 : post.getChildren())
                    {
                        if(post2.getKey().equals("link"))
                        {
                            productList.setLink(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("code"))
                        {
                            productList.setCode(post2.getValue().toString());
                            int code = Integer.valueOf(post2.getValue().toString());
                            code_bool[code-1] = true;
                        }
                        else if(post2.getKey().equals("img"))
                        {
                            productList.setImg(post2.getValue().toString());
                            //System.out.println("count11 : " + post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("like"))
                        {
                            productList.setLike(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("price"))
                        {
                            productList.setPrice(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("split"))
                        {
                            productList.setSplit(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("color"))
                        {
                            int k = 0;
                            productList.newColor((int)post2.getChildrenCount());
                            for(DataSnapshot post3 : post2.getChildren())
                            {
                                productList.setColor(k,post3.getKey().toString());
                                System.out.println("count11 : " + productList.getColor(k));
                                k++;
                            }
                        }
                        else
                        {
                            int k = 0;
                            productList.newSize((int)post2.getChildrenCount());
                            for(DataSnapshot post3 : post2.getChildren())
                            {

                                productList.setSize(k,post3.getKey(),post3.getValue().toString());
                                //System.out.println("count11 : " + productList.getSize(k));
                                k++;
                            }
                        }
                    }
                    productListArrayList1.add(productList);
                }

                System.out.println(productListArrayList1.size());
                mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
                productAdapter = new ProductAdapter(Brandname, productListArrayList1, context);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                    }
                });
                RecyclerView.LayoutManager	mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                System.out.println("적용");
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(productAdapter);
                System.out.println(productAdapter.getItemCount());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return productAdapter;
    }

    public void backtoFrag(String brand)
    {
        brandSet(brand, false);
    }

    public void setCurrent_Code(int code)
    {
        Current_Code = code;
    }

    public int getCurrent_Code()
    {
        return Current_Code;
    }

    public void ProgressRun()
    {
        dialog = ProgressDialog.show(this, "", "Please wait....", true);
    }

    public void ProgressStop()
    {
        dialog.dismiss();
    }

    public int getHeight()
    {
        return FragmentHeight/3;
    }

    public int getWidth()
    {
        return FragmentWidth/2;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Fragment_change);
        FragmentHeight = Integer.valueOf(linearLayout.getHeight());
        FragmentWidth = Integer.valueOf(linearLayout.getWidth());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ... 코드 계속 [STE]
        MenuSet[0] = "아바타 만들기";
        MenuSet[1] = "아바타 만들기2";
        MenuSet[2] = "아바타 만들기3";
        MenuSet[3] = "아바타 만들기4";

        code_String[0] = "상의";
        code_String[1] = "하의";
        code_String[2] = "아우터";
        code_String[3] = "원피스";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        avarta_button = (LinearLayout)findViewById(R.id.line1);
        shopping_button = (LinearLayout)findViewById(R.id.line2);
        ranking_button = (LinearLayout)findViewById(R.id.line3);
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

        avarta_button.setBackgroundColor(Color.BLACK);

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
                                // finish();
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
                Animation animation = new AlphaAnimation(0,1);
                animation.setDuration(1000);
                avarta_button.setAnimation(animation);
                avarta_button.setBackgroundColor(Color.BLACK);
                shopping_button.setBackgroundColor(Color.WHITE);
                ranking_button.setBackgroundColor(Color.WHITE);
                fragmentTransaction.replace(R.id.Fragment_change, new Avarta());
                fragmentTransaction.commit();
                break;
            case R.id.shopping:
                avarta_button.setBackgroundColor(Color.WHITE);
                shopping_button.setBackgroundColor(Color.BLACK);
                ranking_button.setBackgroundColor(Color.WHITE);
                fragmentTransaction.replace(R.id.Fragment_change, new Brand());
                fragmentTransaction.commit();
                break;
            case R.id.ranking:
                avarta_button.setElevation(10);
                avarta_button.setBackgroundColor(Color.WHITE);
                shopping_button.setBackgroundColor(Color.WHITE);
                ranking_button.setBackgroundColor(Color.BLACK);
                fragmentTransaction.replace(R.id.Fragment_change, new Ranking());
                fragmentTransaction.commit();
                break;
        }
    }

}
