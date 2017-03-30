package com.example.cot11.wewear;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;



public class Shopping extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private String[] dataSet1;
    private String[] linkSet1;
    private String[] dataSet2;
    private String[] linkSet2;

    private long timer;
    private long timerend;

    private ArrayList<productList> productListArrayList1 = new ArrayList<productList>();
    private ArrayList<productList> productListArrayList2 = new ArrayList<productList>();

    private View v;
    private boolean back = false;
    public String BrandName = "";
    int count1 = 0;
    int count2 = 0;

    public Shopping() {

    }

    @Override
    public void onDestroy() {
        System.out.println("destroy : shopping 프래그먼트 멈춤");
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //((AvartaMain) getActivity()).ProgressRun();

        if(getArguments() != null)
        {
            BrandName = getArguments().getString("Brand");
            System.out.println("BrandName : " + BrandName);
            back = getArguments().getBoolean("Back");
        }
        else
        {
            Toast.makeText(getActivity(), "불러오기 실패", Toast.LENGTH_SHORT).show();
        }

        timer = System.currentTimeMillis();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Storage 이미지 다운로드 경로
        mDatabase.child("Clothes").child(BrandName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int i = 0;

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
                                        //System.out.println("count11 : " + productList.getColor(k));
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
                            if(i % 2 == 0)
                            {
                                productListArrayList1.add(productList);
                            }
                            else
                            {
                                productListArrayList2.add(productList);
                            }
                            i++;
                        }

                        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
                        ProductAdapter adapter = new ProductAdapter(BrandName,productListArrayList1, productListArrayList2, getActivity());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                        timerend = System.currentTimeMillis();
                        System.out.println("count11_s : " + (timerend - timer));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DDD", "getUser:onCancelled", databaseError.toException());
                    }


                });

        v =  inflater.inflate(R.layout.shopping_itemlist, container, false);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && back)
                {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.Fragment_change, new Brand());
                    fragmentTransaction.commit();
                    System.out.println("shopping back");
                    return  true;
                }
                else if(!back)
                {
                    back = true;
                    return true;
                }
                else
                {
                    return false;
                }

            }
        });

        return v;

    }
}
