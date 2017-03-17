package com.example.cot11.wewear;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Shopping extends Fragment {

    shoppingAdapter shoppingAdapter;

    public Shopping() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        shoppingAdapter = new shoppingAdapter();
        View v =  inflater.inflate(R.layout.shopping_fragment, container, false);
        ListView listview = (ListView) v.findViewById(R.id.shopping_list);
        shoppingAdapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.background),ContextCompat.getDrawable(getActivity(), R.drawable.bear),ContextCompat.getDrawable(getActivity(), R.drawable.bee),ContextCompat.getDrawable(getActivity(), R.drawable.cat));
        listview.setAdapter(shoppingAdapter);
        //return super.onCreateView(inflater, container, savedInstanceState);
        return v;

    }
}
