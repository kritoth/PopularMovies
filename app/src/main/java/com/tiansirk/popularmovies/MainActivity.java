package com.tiansirk.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiansirk.popularmovies.data.MoviesUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";
    private static final String DEFAULT_USER_PREFERENCE = "popular"; // or it can be: top_rated
    private static final String KEY_BUNDLE_FOR_LOADER = "USERS_PREFERENCE";
    private static final int ID_LOADER = 33;

    private String mUsersPreference;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mErrorMessage = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadMovieData();
    }

    /**
     * Starts loading the data of the movies, depending on the user's preference selected in Settings menu
     * Default preference is "popular"
     */
    private void loadMovieData() {
        String usersPreference = "";
        if (mUsersPreference == null || mUsersPreference.isEmpty()) {
            usersPreference = DEFAULT_USER_PREFERENCE;
        } else {
            usersPreference = mUsersPreference;
        }

        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_FOR_LOADER, usersPreference);
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(ID_LOADER, bundle, this);

        Log.d(TAG, "\nLoader initiated!!!!!\n");
    }

    /**
     * Loader for loading Movies from network API in the background
     * @param id The Loader's ID
     * @param args A Bundle which will be used for performing the network request and loading the response
     * @return ArrayList of Movie objects
     */
    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
            /**
             * If the user navigates out from the Activity then returns, but didn't make any new request,
             * then the previous result should be shown instead of loading a new request. For that
             * the already loaded list is cached and delivered as result instead of initiating a loadInBackground.
             */
            ArrayList<Movie> mDisplayedMovieList;

            /**
             * At the start of loading first check if there is anything to be loaded, ie. Bundle is not null.
             * If load is needed, then show the loading indicator.
             * Check if there is a cached list and if so, deliver that instead of a new load
             */
            @Override
            protected void onStartLoading() {
                Log.d(TAG, "\nonStartLoading initiated");
                if(args == null) return;

                if(mDisplayedMovieList != null) {
                    deliverResult(mDisplayedMovieList);
                }
                else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * Performs the load in the background thread. The load consist of (1) building a URL,
             * (2) performing network request with it, (3) parsing JSON received from network,
             * (4) fetching the JSON to Movie objects and (5) storing those into an ArrayList
             * @return The ArrayList of Movie objects
             */
            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {
                Log.d(TAG, "\nLoadInBackground initiated");
                /* If the starting Bundle is null, nothing is to be loaded, sor return early */
                if (args == null) {
                    return null;
                }
                /* Get the user's preference passed as @param, by using the KEY for it */
                String receivedQueryParam = args.getString(KEY_BUNDLE_FOR_LOADER);
                /* Build the URL needed for web request */
                URL url = MoviesUtils.buildUrl(receivedQueryParam, MainActivity.this);
                /* Try the network request and the JSON parsing with the help of the utility class */
                try {
                    String jsonResponse = MoviesUtils.getResponseFromWeb(url);
                    //Log.d(TAG, "jsonResponse: " + jsonResponse);

                    ArrayList<Movie> moviesFetchedFromJson = MoviesUtils.getMoviesListFromJson(jsonResponse);
                    Log.d(TAG, "\nNumber of fetched movies: " + moviesFetchedFromJson.size() +
                            "\nFirst movie in the list: " + moviesFetchedFromJson.get(0).toString());
                    return moviesFetchedFromJson;

                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Problem with either reading from Internet connection or parsing JSON: ", e);
                    return null;
                }
            }

            /**
             * If the load is done cache it to have it redelivered when no new load is needed
             * @param data The loaded ArrayList of Movie objects
             */
            @Override
            public void deliverResult(@Nullable ArrayList<Movie> data) {
                Log.d(TAG, "\ndeliverResult initiated");

                mDisplayedMovieList = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * When the load is finished set the loaded data into the RecyclerView.Adapter object
     * @param loader
     * @param data The loaded ArrayList of Movie object
     */
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        Log.d(TAG, "\nonLoadFinished initiated");
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if (data == null || data.isEmpty()) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showErrorMessage();
        } else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showDataView();
            mAdapter.setMovieData(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }

    /**
     * This method will make the RecyclerView visible and hide the error message
     */
    private void showDataView() {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(View.INVISIBLE);
        /* Then hide loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the RecyclerView
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra(KEY_ACTIVITY_INTENT, clickedMovie);
        startActivity(activityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    /**
     * Menu for choosing request terms: "popular" or "top_rated"
     * After choocing the Loadermanager restarted to reuse the existing loader
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItem = item.getItemId();
        Bundle bundle = new Bundle();

        switch (selectedItem) {
            case R.id.popular:
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "popular";

                bundle.putString(KEY_BUNDLE_FOR_LOADER, mUsersPreference);
                LoaderManager.getInstance(this).restartLoader(ID_LOADER, bundle, this);
                Log.d(TAG, "\nLoader restarted!!!!!\n\"popular\" selected");

                return true;

            case R.id.top_rated:
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "top_rated";

                bundle.putString(KEY_BUNDLE_FOR_LOADER, mUsersPreference);
                LoaderManager.getInstance(this).restartLoader(ID_LOADER, bundle, this);
                Log.d(TAG, "\nLoader restarted!!!!!\n\"top_rated\" selected");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
