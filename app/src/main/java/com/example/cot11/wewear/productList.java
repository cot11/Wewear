package com.example.cot11.wewear;

/**
 * Created by 이언우 on 2017-03-26.
 */

public class productList{
    String name;
    String link;
    String code;
    String price;
    String like;
    String split;
    String img;
    String[] color;
    String[][] size;

    public productList()
    {

    }

    public productList(String name, String link, String code) {
        this.name = name;
        this.link = link;
        this.code = code;
    }

    public int getSizeCount()
    {
        return size.length;
    }

    public int getColorCount()
    {
        return color.length;
    }

    public String getLink() {
        return this.link;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getPrice() {
        return this.price;
    }

    public String getLike() {
        return this.like;
    }

    public String getSplit() {
        return this.split;
    }

    public String getImg() {
        return this.img;
    }

    public String getColor(int n) {
        return this.color[n];
    }

    public String getSize(int n) {
        return (this.size[n][0] + "/" + this.size[n][1]);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImg(String Img) {
        this.img = Img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void newColor(int num)
    {
        color = new String[num];
    }

    public void newSize(int num)
    {
        size = new String[num][2];
    }

    public void setColor(int n, String value)
    {
        color[n] = value;
    }
    public void setSize(int n, String s, String value)
    {
        size[n][0] = s;
        size[n][1] = value;
    }
}
