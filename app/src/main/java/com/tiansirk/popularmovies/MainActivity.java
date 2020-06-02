package com.tiansirk.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiansirk.popularmovies.data.Movie;
import com.tiansirk.popularmovies.ui.MovieAdapter;
import com.tiansirk.popularmovies.util.AppExecutors;
import com.tiansirk.popularmovies.util.MoviesUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";
    private static final String DEFAULT_USER_PREFERENCE = "popular"; // or it can be: favorites, top_rated, upcoming, latest, now_playing

    private String mUsersPreference;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadMovieDataFromNetwork();
    }

    /**
     * Starts loading the data of the movies, depending on the user's preference selected in Settings menu
     * Default preference is "popular"
     */
    private void loadMovieDataFromNetwork() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if (mUsersPreference == null || mUsersPreference.isEmpty()) {
            mUsersPreference = DEFAULT_USER_PREFERENCE;
        }
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                URL url = MoviesUtils.buildListUrl(mUsersPreference, MainActivity.this);
                try {
                    String jsonResponse = MoviesUtils.getResponseFromWeb(url);
                    //Log.d(TAG, "jsonResponse: " + jsonResponse);
                    final ArrayList<Movie> moviesFetchedFromJson = MoviesUtils.getMoviesListFromJson(jsonResponse, MainActivity.this);
                    Log.d(TAG, "Number of fetched movies: " + moviesFetchedFromJson.size() +
                            "\nFirst movie in the list: " + moviesFetchedFromJson.get(0).toString());

                    //UI related process, ie. showing the result of network call, can be made in UI thread only
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!moviesFetchedFromJson.isEmpty()) {
                                showDataView();
                                mAdapter.setMovieData(moviesFetchedFromJson);
                            } else {
                                showErrorMessage();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Problem with either reading from Internet connection or parsing JSON: ", e);
                }
            }
        });
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
        /* Then hide loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie clickedMovie) {
        //Log.d(TAG, "Movie item clicked: " + clickedMovie.toString());

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.popular:
                Log.d(TAG, "Popular menu item selected");
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "popular";
                loadMovieDataFromNetwork();
                return true;
            case R.id.top_rated:
                Log.d(TAG, "Top rated menu item selected");
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "top_rated";
                loadMovieDataFromNetwork();
                return true;
            case R.id.now_playing:
                Log.d(TAG, "Now playing menu item selected");
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "now_playing";
                loadMovieDataFromNetwork();
                return true;
            case R.id.upcoming:
                Log.d(TAG, "Upcoming menu item selected");
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "upcoming";
                loadMovieDataFromNetwork();
                return true;
            case R.id.favorite:
                Log.d(TAG, "Favorite menu item selected");
                Intent activityIntent = new Intent(this, FavoriteActivity.class);
                startActivity(activityIntent);
                //Toast.makeText(MainActivity.this, "Favorites selected", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mErrorMessage = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
    }
}
