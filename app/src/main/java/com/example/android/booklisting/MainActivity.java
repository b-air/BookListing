package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>{

    //Variables
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int BOOKS_LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private EditText mSearchEditText;
    private ImageView mSearch;

    private TextView mEmptyView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /**
         * Set empty view
         */
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        mLoadingIndicator.setVisibility(GONE);
        mSearchEditText = (EditText) findViewById(R.id.edit_text_search);
        mSearch = (ImageView) findViewById(R.id.search);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new adapter that takes an empty list of book as input
                mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>(), new BookAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Book book) {
                        String url = book.getLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                // Set the adapter on the {@link RecyclerView}
                // so the list can be populated in the user interface
                mRecyclerView.setAdapter(mAdapter);

                // Get a reference to the ConnectivityManager to check state of network connectivity
                final ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.restartLoader(BOOKS_LOADER_ID, null, MainActivity.this);
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    mLoadingIndicator.setVisibility(GONE);

                    mRecyclerView.setVisibility(GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    // Update empty state with no connection error message
                    mEmptyView.setText(R.string.no_internet_connection);
                }
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(GONE);

        String searchInput = mSearchEditText.getText().toString();

        if (searchInput.length() == 0) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.search_nothing), Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        searchInput = searchInput.replace(" ", "+");
        String query = BOOKS_URL + searchInput;

        Uri baseUri = Uri.parse(query);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //Loading in background
        return new BooksLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> book) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(GONE);
        mEmptyView.setVisibility(View.VISIBLE);

        // Set empty state text to display "No books found."
        mEmptyView.setText(R.string.no_books);

        // Clear the adapter of previous news data
        mAdapter.clear();

        if (book != null && !book.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(GONE);
            mAdapter.addAll(book);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
