package com.example.cot11.wewear;

/**
 * Created by cot11 on 2017-03-30.
 */

public class Brandlist {

    String name;
    String link;
    String logo;

    Brandlist()
    {
    }

    Brandlist(String Name, String Link, String Logo)
    {
        this.name = Name;
        this.link = Link;
        this.logo = Logo;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getRogo()
    {
        return this.logo;
    }


    public void setName(String Name)
    {
        this.name = Name;
    }

    public void settLink(String Link)
    {
        this.link = Link;
    }

    public void setRogo(String Logo)
    {
        this.logo = Logo;
    }

}
