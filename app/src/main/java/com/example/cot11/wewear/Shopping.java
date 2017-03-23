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



public class Shopping extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private String[] dataSet1;
    private String[] linkSet1;
    private String[] dataSet2;
    private String[] linkSet2;
    private View v;
    public String BrandName = "";
    int count1 = 0;
    int count2 = 0;

    public Shopping() {

    }

    public static Shopping newInstance(String brandName)
    {
        Shopping shopping = new Shopping();
        Bundle args = new Bundle();
        args.putString("Brand",brandName);
        return shopping;
    }
    public interface BrandSend{
        public void send(String brand);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Storage 이미지 다운로드 경로
        mDatabase.child("Clothes").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value

                        BrandName = getArguments().getString("Brand");
                        System.out.println("Brand : " + BrandName);
                        dataSet1 = new String[(int)dataSnapshot.getChildrenCount()/2 + 1];
                        linkSet1 = new String[(int)dataSnapshot.getChildrenCount()/2 + 1];
                        dataSet2 = new String[(int)dataSnapshot.getChildrenCount()/2];
                        linkSet2 = new String[(int)dataSnapshot.getChildrenCount()/2];
                        int i = 0;

                        for(DataSnapshot post : dataSnapshot.getChildren() ){
                            String[] name = post.getKey().split("!");

                            for(DataSnapshot post2 : post.getChildren())
                            {

                                if(i % 2 == 0)
                                {
                                    dataSet1[count1] = name[1];
                                    linkSet1[count1] = post2.getValue().toString();
                                    count1++;
                                }
                                else
                                {
                                    dataSet2[count2] = name[1];
                                    linkSet2[count2] = post2.getValue().toString();
                                    count2++;
                                }
                            }
                            i++;
                        }

                        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
                        ProductAdapter adapter = new ProductAdapter(linkSet1, dataSet1, linkSet2, dataSet2, getActivity());
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

        v =  inflater.inflate(R.layout.shopping_itemlist, container, false);
        ButterKnife.bind(v);

        return v;

    }
}
