package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by bruno on 24/07/2017.
 */

public class BooksLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BooksLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructor
     */
    public BooksLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // On Background Thread
    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        /**
         * Network request, parsing, extraction
         */

        List<Book> books = QueryUtils.fetchData(mUrl);
        return books;
    }
}
