package com.tiansirk.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
     * Starts loading the data of the movies
     */
    private void loadMovieData(){
        //ArrayList<Movie> movies = MoviesUtils.getDummyMoviesList();
        //mAdapter.setMovieData(movies);

        String usersPreference = "popular"; // or: top_rated

        FetchMovieTask task = new FetchMovieTask();
        task.execute(usersPreference);
    }

    @Override
    public void onClick(Movie clickedMovie) {
        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra("CHOSEN_MOVIE", clickedMovie);
        startActivity(activityIntent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>>{

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
                Log.v(TAG, "jsonResponse: " + jsonResponse);

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
            if(!movies.isEmpty()) {

                showDataView();
                mAdapter.setMovieData(movies);
            }
            else showErrorMessage();
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
        /* Then, make sure the weather data is visible */
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
}
