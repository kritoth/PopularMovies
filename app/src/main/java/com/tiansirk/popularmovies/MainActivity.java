package com.tiansirk.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_INTENT = "CHOSEN_MOVIE";
    private static final String DEFAULT_USER_PREFERENCE = "popular"; // or it can be: top_rated

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
        FetchMovieTask task = new FetchMovieTask();
        task.execute(usersPreference);
        /*
        mLoadingIndicator.setVisibility(View.VISIBLE);
        ArrayList<Movie> movies = Repository.getMovies(usersPreference);
        if (!movies.isEmpty()) {
            showDataView();
            mAdapter.setMovieData(movies);
        } else showErrorMessage();
        */

    }

    @Override
    public void onClick(Movie clickedMovie) {
        Log.d(TAG, "Movie item clicked");

        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra(KEY_ACTIVITY_INTENT, clickedMovie);
        startActivity(activityIntent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {

            String receivedQueryParam = strings[0];

            URL url = MoviesUtils.buildUrl(receivedQueryParam, MainActivity.this);

            try {
                String jsonResponse = MoviesUtils.getResponseFromWeb(url);
                //Log.v(TAG, "jsonResponse: " + jsonResponse);

                ArrayList<Movie> moviesFetchedFromJson = MoviesUtils.getMoviesListFromJson(jsonResponse);
                Log.v(TAG, "Number of fetched movies: " + moviesFetchedFromJson.size() +
                        "\nFirst movie in the list: " + moviesFetchedFromJson.get(0).toString());

                return moviesFetchedFromJson;
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Problem with either reading from Internet connection or parsing JSON: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            if (!movies.isEmpty()) {

                showDataView();
                mAdapter.setMovieData(movies);
            } else showErrorMessage();
        }
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
                mAdapter = new MovieAdapter(this); // Töröld nehogy újracsinálja, így elkerülöd, hogy újra jelenjen meg
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "popular";
                loadMovieData();
                return true;

            case R.id.top_rated:
                Log.d(TAG, "Top rated menu item selected");
                mAdapter = new MovieAdapter(this); // Töröld nehogy újracsinálja, így elkerülöd, hogy újra jelenjen meg
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "top_rated";
                loadMovieData();
                return true;
                /*
            case R.id.favorite:
                Log.d(TAG, "Favorite menu item selected");
                mAdapter = new MovieAdapter(this);
                mRecyclerView.setAdapter(mAdapter);
                mUsersPreference = "favorite";
                loadMovieData();
                return true;
                */
        }
        return super.onOptionsItemSelected(item);
    }
}
