package com.example.cot11.wewear;

import android.graphics.drawable.Drawable;

/**
 * Created by cot11 on 2017-03-17.
 */

public class shoppingitem {
    private Drawable iconDrawable1;
    private Drawable iconDrawable2;
    private Drawable iconDrawable3;
    private Drawable iconDrawable4;

    public void setIcon1(Drawable icon) {
        iconDrawable1 = icon ;
    }
    public void setIcon2(Drawable icon) {
        iconDrawable2 = icon ;
    }
    public void setIcon3(Drawable icon) {
        iconDrawable3 = icon ;
    }
    public void setIcon4(Drawable icon) {
        iconDrawable4 = icon ;
    }

    public Drawable getIcon1() {
        return this.iconDrawable1 ;
    }
    public Drawable getIcon2() {
        return this.iconDrawable2 ;
    }
    public Drawable getIcon3() {
        return this.iconDrawable3 ;
    }
    public Drawable getIcon4() {
        return this.iconDrawable4 ;
    }
}
