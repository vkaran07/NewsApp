package com.example.karan.newsapp;


/**
 * Created by karan on 4/11/2017.
 */
public class News {

    private String mTitle;
    private String msection;
    private String murl;

    public News(String title, String section, String url) {
        mTitle = title;

        msection = section;
        murl = url;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getsec() {
        return msection;
    }

    public String getWebUrl() {
        return murl;
    }

}
