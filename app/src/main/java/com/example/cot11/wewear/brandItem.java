package com.example.cot11.wewear;

import android.graphics.drawable.Drawable;

/**
 * Created by cot11 on 2017-03-17.
 */

public class brandItem {

    private Drawable Logo1;
    private Drawable Logo2;

    private String Link1;
    private String Link2;

    private String category1;
    private String category2;

    private String content1;
    private String content2;


    public void setIcon1(Drawable icon) {
        Logo1 = icon ;
    }
    public void setIcon2(Drawable icon) {
        Logo2 = icon ;
    }
    public void setLink1(String link) {
        Link1 = link ;
    }
    public void setLink2(String link) {
        Link2 = link ;
    }
    public void setCategory1(String cate) {
        category1 = cate ;
    }
    public void setCategory2(String cate) {
        category2 = cate ;
    }
    public void setContent1(String con) {
        content1 = con ;
    }
    public void setContent2(String con) {
        content2 = con ;
    }


    public Drawable getIcon1() {
        return this.Logo1 ;
    }
    public Drawable getIcon2() {
        return this.Logo2 ;
    }
    public String getLink1() {
        return this.Link1 ;
    }
    public String getLink2() {
        return this.Link2 ;
    }
    public String getCategory1() {
        return this.category1 ;
    }
    public String getCategory2() {
        return this.category2 ;
    }
    public String getContent1() {
        return this.content1 ;
    }
    public String getContent2() {
        return this.content2 ;
    }

}
