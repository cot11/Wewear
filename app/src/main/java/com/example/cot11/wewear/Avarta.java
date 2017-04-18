package com.example.cot11.wewear;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 이언우 on 2017-03-13.
 */


public class Avarta extends Fragment {

    public Avarta() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.avarta_fragment, container, false);
        final ImageView linearLayout = (ImageView)v.findViewById(R.id.Mainbody);
        ((AvartaMain) getActivity()).AvartaSet(v);
        return v;
    }
}

