package com.example.cot11.wewear;

import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class Shopping extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private RecyclerView.LayoutManager	mLayoutManager;
    private RecyclerView.LayoutManager	mLayoutManager1;
    private RecyclerView.LayoutManager	mLayoutManager2;
    private DatabaseReference mDatabase;

    private ArrayList<productList> productListArrayList1 = new ArrayList<productList>();
    private ArrayList<productList> productListArrayList = new ArrayList<productList>();

    private View v;
    private boolean back = false;
    public String BrandName = "";

    boolean[] code_bool = new boolean[4];
    String[] code_String = new String[4];


    public Shopping() {

    }

    @Override
    public void onDestroy() {
        System.out.println("destroy : shopping 프래그먼트 멈춤");
        super.onDestroy();
    }

    public void Systemm()
    {
        System.out.println("KKKKKKKKKKKKk");
        System.out.println("count11 : " + BrandName);
        //LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.linear1);
        //linearLayout.setVisibility(View.INVISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //((AvartaMain) getActivity()).ProgressRun();

        code_String[0] = "상의";
        code_String[1] = "하의";
        code_String[2] = "아우터";
        code_String[3] = "원피스";

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
        v =  inflater.inflate(R.layout.shopping_itemlist, container, false);
        ProductAdapter adapter = ((AvartaMain) getActivity()).test(BrandName,v);


        // Storage 이미지 다운로드 경로
        /*
        mDatabase.child("Clothes").child(BrandName).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            productListArrayList1.add(productList);
                        }

                        for(int i = 0; i < productListArrayList1.size(); i++)
                        {
                            if(productListArrayList1.get(i).getCode().equals("1"))
                            {
                                productList productList = new productList();
                                productList = productListArrayList1.get(i);
                                productListArrayList.add(productList);
                            }
                        }

                        mRecyclerView1 = (RecyclerView)v.findViewById(R.id.recycler_view1);
                        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
                        List<String> mDataset= new ArrayList<String>();
                        for(int i = 0; i < code_bool.length; i++)
                        {
                            if(code_bool[i])
                            {
                                mDataset.add(code_String[i]);
                            }
                        }
                        CodeApdater mAdapter = new CodeApdater(mDataset, getActivity(), productListArrayList1);
                        mLayoutManager1  = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
                        mRecyclerView1.setLayoutManager(mLayoutManager1);
                        mRecyclerView1.setAdapter(mAdapter);

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DDD", "getUser:onCancelled", databaseError.toException());
                    }


                });
        */
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
