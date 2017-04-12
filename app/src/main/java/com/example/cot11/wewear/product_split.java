package com.example.cot11.wewear;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by cot11 on 2017-04-12.
 */

public class product_split {

    private ArrayList<Bitmap> product = new ArrayList<>();

    public void setImage(Bitmap bitmap)
    {
        product.add(bitmap);
    }
    public Bitmap getImage(int position)
    {
        return product.get(position);
    }
}
