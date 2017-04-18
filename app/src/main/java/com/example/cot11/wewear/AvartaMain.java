package com.example.cot11.wewear;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import java.util.List;

/**
 * Created by 이언우 on 2017-03-13.
 */

public class AvartaMain extends AppCompatActivity{

    private int FragmentHeight = 0;
    private int FragmentWidth = 0;
    private float init_put_Y = 0;
    private float recycle_height = 0;
    private float current_put_Y = 0;
    private float percent_Y = 0;
    private int Current_Code = 0;
    private String BrandName;
    private Context context;
    private FragmentManager fm;
    private DatabaseReference mDatabase;
    private ProductAdapter productAdapter;
    private putAdapter putAdapter;
    private Animation animation_Right;
    private float first_touch = 0;
    private float last_touch = 0;
    private Bitmap face, body, leg, ankle, arm;
    private Bitmap Notarm, CurrentBody;
    private SendMassgeHandler mMainHandler = null;
    private boolean Ready = true;
    private product_split[] aleady = new product_split[4];


    private LinearLayout avarta_button;
    private LinearLayout shopping_button;
    private LinearLayout ranking_button;

    private RecyclerView mainRecy;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView1;
    private RecyclerView bRecyclerView;
    private View import_view;
    private ImageView setBody;

    boolean[] code_bool = new boolean[4];
    private String[] code_String = new String[4];
    private String[] MenuSet = new String[4];
    private ArrayList<String> putDataset = new ArrayList<>();
    private ArrayList<productList> productListArrayList1 = new ArrayList<productList>();
    private ArrayList<productList> productListArrayList2 = new ArrayList<productList>();
    private ArrayList<productList> MainProduct = new ArrayList<productList>();
    private ArrayList<product_split> split_product = new ArrayList<product_split>();
    private ArrayList<Brandlist> Brandlist1 = new ArrayList<Brandlist>();



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


    public void Product(String cate)
    {
        int cate_int = 0;
        for(int i = 0; i < code_String.length; i++)
        {
            if(code_String[i].equals(cate))
            {
                cate_int = i+1;
                break;
            }
        }
        productListArrayList1.clear();
        for(int i = 0; i < productListArrayList2.size(); i++)
        {
            if(Integer.valueOf(productListArrayList2.get(i).getCode()) == cate_int)
            {
                productListArrayList1.add(productListArrayList2.get(i));
            }
        }
        System.out.println("size : " + productListArrayList1.size());
        productAdapter.Clear(productListArrayList1);
    }


    public void init_Product(final String Brandname, final View view)
    {
        import_view = view;
        BrandName = Brandname;
        productListArrayList1.clear();
        productListArrayList2.clear();
        for(int i = 0; i < code_bool.length; i++)
        {
            code_bool[i] = false;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Storage 이미지 다운로드 경로
        mDatabase.child("Clothes").child(Brandname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren() ) {
                    productList productList = new productList();
                    productList.setName(post.getKey());
                    int code = 0;
                    for(DataSnapshot post2 : post.getChildren())
                    {
                        if(post2.getKey().equals("link"))
                        {
                            productList.setLink(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("code"))
                        {
                            productList.setCode(post2.getValue().toString());
                            code = Integer.valueOf(post2.getValue().toString());
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
                    productListArrayList2.add(productList);
                    if(1 == code)
                    {
                        productListArrayList1.add(productList);
                    }
                }
                System.out.println(productListArrayList1.size());
                mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
                mRecyclerView1 = (RecyclerView)view.findViewById(R.id.recycler_view1);
                //mRecyclerView2 = (RecyclerView)view.findViewById(R.id.recycler_view2);

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
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(productAdapter);
                mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN :
                                first_touch = event.getY();
                                System.out.println("Down : " + first_touch);
                            case MotionEvent.ACTION_MOVE :
                                last_touch = event.getY();
                                float dis = last_touch - first_touch;

                                if(dis > 1 )
                                {
                                    if(current_put_Y == init_put_Y)
                                    {
                                        return false;
                                    }
                                    current_put_Y = current_put_Y - percent_Y;
                                    if(current_put_Y <= init_put_Y)
                                    {
                                        current_put_Y = init_put_Y;
                                    }
                                    mainRecy.setY(current_put_Y);
                                }
                                else if(dis < -1)
                                {
                                    current_put_Y = current_put_Y + percent_Y;

                                    if(current_put_Y >= init_put_Y+recycle_height)
                                    {
                                        current_put_Y = init_put_Y+recycle_height;
                                    }
                                    mainRecy.setY(current_put_Y);
                                }
                                first_touch = last_touch;
                        }
                        return false;
                    }
                });

                mRecyclerView1.setLayoutManager(new LinearLayoutManager(context));
                RecyclerView.LayoutManager	mLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                RecyclerView.LayoutManager	mLayoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                List<String> mDataset= new ArrayList<String>();
                for(int i = 0; i < code_bool.length; i++)
                {
                    if(code_bool[i])
                    {
                        mDataset.add(code_String[i]);
                    }
                }
                CodeApdater mAdapter = new CodeApdater(mDataset, context, productListArrayList1, 1);
                mRecyclerView1.setLayoutManager(mLayoutManager1);
                mRecyclerView1.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void init_Brand(final View view)
    {
        Brandlist1.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Brand").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                for(DataSnapshot post : dataSnapshot.getChildren() ) {
                    Brandlist brandlist = new Brandlist();
                    brandlist.setName(post.getKey());
                    for(DataSnapshot post2 : post.getChildren())
                    {
                        if(post2.getKey().equals("logo"))
                        {
                            System.out.println("count11 : " + post2.getValue());
                            brandlist.setRogo(post2.getValue().toString());
                        }
                        else if(post2.getKey().equals("Link"))
                        {
                            brandlist.settLink(post2.getValue().toString());
                            //System.out.println("count11 : " + post2.getValue());
                        }
                    }
                    Brandlist1.add(brandlist);
                }

                bRecyclerView = (RecyclerView)view.findViewById(R.id.brecycler_view);
                BrandAdapter adapter = new BrandAdapter(Brandlist1, context);
                bRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                bRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                    }
                });
                bRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch(event.getAction()) {
                            case MotionEvent.ACTION_DOWN :
                                first_touch = event.getY();
                                System.out.println("Down : " + first_touch);
                            case MotionEvent.ACTION_MOVE :
                                last_touch = event.getY();
                                float dis = last_touch - first_touch;

                                if(dis > 1 )
                                {
                                    if(current_put_Y == init_put_Y)
                                    {
                                        return false;
                                    }
                                    current_put_Y = current_put_Y - percent_Y;
                                    if(current_put_Y <= init_put_Y)
                                    {
                                        current_put_Y = init_put_Y;
                                    }
                                    mainRecy.setY(current_put_Y);
                                }
                                else if(dis < -1)
                                {
                                    current_put_Y = current_put_Y + percent_Y;

                                    if(current_put_Y >= init_put_Y+recycle_height)
                                    {
                                        current_put_Y = init_put_Y+recycle_height;
                                    }
                                    mainRecy.setY(current_put_Y);
                                }
                                first_touch = last_touch;
                        }
                        return false;
                    }
                });

                RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                RecyclerView.LayoutManager	mLayoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                bRecyclerView.setLayoutManager(mLayoutManager);
                bRecyclerView.setAdapter(adapter);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DDD", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    public void AvartaSet(View v)
    {
        setBody = (ImageView) v.findViewById(R.id.Mainbody);
    }


    public void itemApply(int position)
    {
        boolean rr = false;
        System.out.println("sizecc : " + MainProduct.get(position).getName());
        System.out.println("sizecc : " + split_product.get(position).getSize());
        System.out.println("sizecc : " + MainProduct.get(position).getCode());
        System.out.println("sizecc : " + MainProduct.get(position).getPrice());
        System.out.println("sizecc size : " + split_product.get(position).getImage(0).getWidth());
        System.out.println("sizecc size : " + split_product.get(position).getImage(0).getHeight());
        int code = Integer.valueOf(MainProduct.get(position).getCode());

        for(int i = 0; i < 4; i++)
        {
            if(aleady[i] != null)
            {
                System.out.println("sisisi : " + i);
                rr= true;
            }
        }

        if(rr)
        {
            CurrentBody = Notarm;
        }


        aleady[code-1] = split_product.get(position);
        System.out.println(aleady[code-1].getSize());
        if(aleady[1] == null)
        {
            for(int i = 0; i < 4; i++)
            {
                if(aleady[i] != null)
                {
                    for(int j = 0; j < aleady[i].getSize(); j++)
                    {
                        CurrentBody = overlayMark(CurrentBody,aleady[i].getImage(j));
                    }
                }
            }
            setBody.setImageBitmap(CurrentBody);
            // 상의먼저
        }
        else
        {
            System.out.println("하의가 있네?");
            for(int i = 3; i >= 0; i--)
            {
                if(aleady[i] != null)
                {
                    for(int j = 0; j < aleady[i].getSize(); j++)
                    {
                        CurrentBody = overlayMark(CurrentBody,aleady[i].getImage(j));
                    }
                }
            }
            setBody.setImageBitmap(CurrentBody);
        }


    }
    public void putAdditem(String item, productList productList)
    {
        MainProduct.add(productList);
        putAdapter.Add(item, Integer.valueOf(productList.getSplit()));
        mainRecy.setVisibility(View.VISIBLE);
        mainRecy.setY(init_put_Y);
        //System.out.println("sizecc main: " + MainProduct.size());
    }

    public Bitmap setPutBitmap(ArrayList<Bitmap> bitmap)
    {
        product_split product_split = new product_split();
        int num = bitmap.size();
        Bitmap bitmap1 = bitmap.get(0);
        product_split.setImage(bitmap1);
        if(num == 1)
        {
            split_product.add(product_split);
           return bitmap1;
        }
        for(int i = 1; i < num; i++)
        {
            if(i < num)
            {
                product_split.setImage(bitmap.get(i));
                bitmap1 = overlayMark(bitmap1,bitmap.get(i));
            }

        }
        split_product.add(product_split);
        //System.out.println("sizecc split: " + split_product.size());
        return bitmap1;
    }

    public void removePutBitmap(int position)
    {
        System.out.println("remove : " + split_product.size());
        split_product.remove(position);
        System.out.println("remove : " + split_product.size());
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
        RecyclerView recyclerViewk = (RecyclerView) findViewById(R.id.Mainrecycler_view);
        ImageView imageView = (ImageView)findViewById(R.id.Mainbody);
        FragmentHeight = Integer.valueOf(linearLayout.getHeight());
        FragmentWidth = Integer.valueOf(linearLayout.getWidth());
        init_put_Y = Float.valueOf(recyclerViewk.getY());
        current_put_Y = init_put_Y;
        percent_Y = Float.valueOf(recyclerViewk.getHeight()) * 0.05f;
        recycle_height = Float.valueOf(recyclerViewk.getHeight());

        System.out.println("sizesize : " + imageView.getWidth());
        System.out.println("sizesize : " + imageView.getHeight());

        if(Ready)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            // Calculate inSampleSize
            options.inScaled = false;

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;




            face = BitmapFactory.decodeResource(this.getResources(), R.drawable.face, options);
            face = Bitmap.createScaledBitmap(face,imageView.getWidth(),imageView.getHeight(),true);

            body = BitmapFactory.decodeResource(this.getResources(), R.drawable.body, options);
            body = Bitmap.createScaledBitmap(body,imageView.getWidth(),imageView.getHeight(),true);

            arm = BitmapFactory.decodeResource(this.getResources(), R.drawable.arm, options);
            arm = Bitmap.createScaledBitmap(arm,imageView.getWidth(),imageView.getHeight(),true);

            leg = BitmapFactory.decodeResource(this.getResources(), R.drawable.leg,options );
            leg = Bitmap.createScaledBitmap(leg,imageView.getWidth(),imageView.getHeight(),true);

            ankle = BitmapFactory.decodeResource(this.getResources(), R.drawable.ankle, options);
            ankle = Bitmap.createScaledBitmap(ankle,imageView.getWidth(),imageView.getHeight(),true);

            setBody.setImageBitmap(face);




            System.out.println("sizesize a: " + body.getWidth());
            System.out.println("sizesize a: " + body.getHeight());
            System.out.println("sizesize a: " + face.getWidth());
            System.out.println("sizesize a: " + face.getHeight());

            /*
            mMainHandler = new SendMassgeHandler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    face = overlayMark(face, body);
                    face = overlayMark(face, leg);
                    face = overlayMark(face, ankle);
                    Notarm = body;
                    face = overlayMark(face, arm);
                    CurrentBody = body;
                    Message msg = mMainHandler.obtainMessage();
                    msg.what = 1;
                    mMainHandler.sendMessage(msg);

                }
            }).start();
            Ready = false;
            */

        }
        else
        {
            System.out.println("what?");
            setBody.setImageBitmap(CurrentBody);
        }


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
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        animation_Right = AnimationUtils.loadAnimation(this,R.anim.anim_right);


        mainRecy = (RecyclerView)findViewById(R.id.Mainrecycler_view);
        mainRecy.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.LayoutManager	mLayoutManager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        mainRecy.setLayoutManager(mLayoutManager2);
        putAdapter = new putAdapter(context);
        mainRecy.setAdapter(putAdapter);
        mainRecy.setVisibility(View.INVISIBLE);
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

    private Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2)
    {
        Bitmap temp = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(temp);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return temp;
    }

    class SendMassgeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setBody.setImageDrawable(new BitmapDrawable(getResources(), face));
                    System.out.println("sizesize a: " + face.getHeight());
                    break;
                default:
                    break;
            }
        }

    };

}
