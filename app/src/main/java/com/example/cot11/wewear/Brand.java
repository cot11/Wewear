package com.example.cot11.wewear;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;

/**
 * Created by 이언우 on 2017-03-20.
 */

public class Brand extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private String[] dataSet1;
    private String[] linkSet1;
    private String[] dataSet2;
    private String[] linkSet2;
    private View v;
    int count1 = 0;
    int count2 = 0;

    public Brand() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Storage 이미지 다운로드 경로
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("Brand").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value

                        dataSet1 = new String[(int)dataSnapshot.getChildrenCount()/2 + 1];
                        linkSet1 = new String[(int)dataSnapshot.getChildrenCount()/2 + 1];
                        dataSet2 = new String[(int)dataSnapshot.getChildrenCount()/2];
                        linkSet2 = new String[(int)dataSnapshot.getChildrenCount()/2];
                        int i = 0;

                        for(DataSnapshot post : dataSnapshot.getChildren() ){
                            String name = post.getKey();

                            for(DataSnapshot post2 : post.getChildren())
                            {

                                if(i % 2 == 0)
                                {
                                    dataSet1[count1] = name;
                                    linkSet1[count1] = post2.getValue().toString();
                                    count1++;
                                }
                                else
                                {
                                    dataSet2[count2] = name;
                                    linkSet2[count2] = post2.getValue().toString();
                                    count2++;
                                }
                            }
                            i++;
                        }

                        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
                        BrandAdapter adapter = new BrandAdapter(linkSet1, dataSet1, linkSet2, dataSet2, getActivity());
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                            }
                        });
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
