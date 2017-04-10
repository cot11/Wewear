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
    private View v;

    public Brand() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v =  inflater.inflate(R.layout.brand, container, false);
         ((AvartaMain) getActivity()).init_Brand(v);
        // firebase Storage 읽기

        return v;
    }
}
