package com.example.android.booklisting;

/**
 * Created by bruno on 14/07/2017.
 */

public class Book {

    // variables
    private String mTitle;
    private String mAuthor;
    private String mCover;
    private String mLink;

    //Constructor
    public Book(String title, String author, String cover, String link){
        mTitle = title;
        mAuthor = author;
        mCover = cover;
        mLink = link;
    }

    //Get
    public String getTitle(){ return mTitle;}
    public String getAuthor(){ return mAuthor;}
    public String getCover(){ return mCover;}
    public String getLink(){ return mLink;}
}
