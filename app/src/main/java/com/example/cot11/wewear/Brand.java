package com.example.cot11.wewear;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by 이언우 on 2017-03-20.
 */

public class Brand extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private RecyclerView.LayoutManager	mLayoutManager;
    private ArrayList<Brandlist> Brandlist1 = new ArrayList<Brandlist>();
    private View v;

    public Brand() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // ((AvartaMain) getActivity()).ProgressRun();

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

                        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
                        BrandAdapter adapter = new BrandAdapter(Brandlist1, getActivity());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });

                        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(adapter);

                        // ...
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("DDD", "getUser:onCancelled", databaseError.toException());
                    }
                });

        // firebase Storage 읽기
        v =  inflater.inflate(R.layout.brand, container, false);



        ButterKnife.bind(v);

        return v;
    }
}
